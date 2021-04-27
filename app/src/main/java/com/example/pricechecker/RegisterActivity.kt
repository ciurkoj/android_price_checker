package com.example.pricechecker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.pricechecker.ui.login.LoginActivity
import com.google.android.material.theme.MaterialComponentsViewInflater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG = "=======DocSnippets======="
    private val PICK_IMAGE_REQUEST = 71
    var selectedPhotoUri: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
//    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        register.setOnClickListener {
           signUpUser()
        }


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        selectphoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha = 0f


        }
    }
    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = auth.currentUser.uid
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        runBlocking {
            GlobalScope.launch(Dispatchers.Main) {
                ref.putFile(selectedPhotoUri!!)
                    .addOnSuccessListener {
                        Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                        ref.downloadUrl.addOnSuccessListener {
                            Log.d(TAG, "File Location: $it")

                            val profileUpdates = UserProfileChangeRequest.Builder().apply {
                                photoUri = it
                            }.build()
                            auth.currentUser.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User profile updated.")
                                    }
                                }

                            saveUserToFirebaseDatabase(it.toString())
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                    }
            }
        }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username.text.toString(), profileImageUrl)


        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }

    private fun signUpUser() {


        if (username.text.toString().isEmpty()) {

            username.error = "Please enter username"
            username.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }
        if (password.text.toString() != repeat_password.text.toString()) {
            password.error = "Entered passwords don't match"
            password.requestFocus()
            return
        }

        val email: String = email.text.toString().trim{it<=' '}
        val password: String = password.text.toString().trim{it<=' '}
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser



                        val profileUpdates = UserProfileChangeRequest.Builder().apply {
                            displayName = username.text.toString()
                        }.build()
                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG,"User profile updated.")
                            }
                        }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        uploadImageToFirebaseStorage()
                        updateUI(user)
                    if (task.isSuccessful) {
                        val user1 = hashMapOf(
                            "user_name" to username.text.toString(),
                            "user_email" to email

                        )
                        val user = auth.currentUser

                        val profileUpdates = UserProfileChangeRequest.Builder().apply {
                            displayName = username.text.toString()
                        }.build()
                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User profile updated.")
                            }
                        }
// Add a new document with a generated ID
                        db.collection("user_data").document(user.email)
                            .set(user1)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)
                            }.addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }

//                        Firebase.auth.signOut()

                        startActivity(Intent(this, LoginActivity::class.java).putExtra("previousActivity", "RegisterActivity"))

                        finish()
                    } else {
                        Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                                Toast.LENGTH_SHORT).show()
                    }

                }


    }}

    private fun updateUI(user: FirebaseUser?) {

    }
}
class User(val uid: String, val username: String, val profileImageUrl: String)