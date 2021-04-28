package com.example.pricechecker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pricechecker.ui.login.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.*
import java.util.*


/**]
 * Register activity handles user registration process.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "=======Register Activity======="
    private val db = Firebase.firestore
    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
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

    /**
     * Selects an image from users device
     * @param requestCode – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode – The integer result code returned by the child activity through its setResult().
     * @param data – An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
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

    /**
     * Uploads the image, sets photoUri parameter and adds a reference in the Firestore
     */
    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = auth.currentUser.uid
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        runBlocking {
            GlobalScope.launch(Dispatchers.Main) {
                ref.putFile(selectedPhotoUri!!)
                    .addOnSuccessListener { it ->
                        Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                        ref.downloadUrl.addOnSuccessListener {
                            Log.d(TAG, "File Location: $it")

                            // Sets photoUri parameter
                            val profileUpdates = UserProfileChangeRequest.Builder().apply {
                                photoUri = it
                            }.build()
                            auth.currentUser.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User profile updated.")
                                    }
                                }
                            // Adds a custom field "profileImageURL" in the database
                            val dataRef = db.collection("user_data").document(auth.currentUser.uid)
                            dataRef
                                .update("profileImageURL", it.toString())
                                .addOnSuccessListener {
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot successfully updated!"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        TAG,
                                        "Error updating document",
                                        e
                                    )
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                    }
            }
        }
    }

    /**
     * SignUpUser takes parameters from the context and creates an instance of a user in Firebase.
     */
    private fun signUpUser() {
        val userName = username.text.toString().trim { it <= ' ' }
        val email: String = email.text.toString().trim { it <= ' ' }
        val password: String = password.text.toString().trim { it <= ' ' }
        val confirmPassword: String = confirm_password.text.toString().trim { it <= ' ' }

        if (!checkUserCredentials(userName, email, password, confirmPassword)) {
            Toast.makeText(
                baseContext, "Please enter valid credentials.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult?> { task ->
                        if (!task.isSuccessful) {
                            try {
                                throw task.exception!!
                            } // if user enters wrong email.
                            catch (weakPassword: FirebaseAuthWeakPasswordException) {
                                Toast.makeText(
                                    baseContext, weakPassword.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(TAG, weakPassword.message.toString())
                            } // if user enters wrong password.
                            catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    baseContext, malformedEmail.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (existEmail: FirebaseAuthUserCollisionException) {
                                Toast.makeText(
                                    baseContext, existEmail.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e(TAG,existEmail.message.toString())
                            } catch (e: java.lang.Exception) {
                                Log.e(TAG, "onComplete: " + e.message)
                            }
                            return@OnCompleteListener
                        } else {

                            createUserData(userName, email)
                            startActivity(
                                Intent(this, LoginActivity::class.java)
                                    .putExtra("previousActivity", "RegisterActivity")
                            )
                            finish()
                        }
                    }
                )
        }
    }

    /**
     * Creates initial user data in Firebase Authentication and Firestore
     * @param userName is a value given in username EditText field,
     * @param userEmail is a value given in email EditText field,
     */
    private fun createUserData(userName: String, userEmail: String) {
        val userData = hashMapOf(
            "user_name" to userName,
            "user_email" to userEmail
        )
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build()
        // Update current user display name variable
        auth.currentUser.updateProfile(profileUpdates)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            })
        // Add a new document with a generated User ID to Firestore database
        db.collection("user_data").document(auth.currentUser.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
        uploadImageToFirebaseStorage()
    }

    /**
     * Checks if user credentials are correct
     * @param userName cannot be empty
     * @param emailAddress must match email pattern and must not exist in the database
     * @param userPassword must be longer than 6 characters
     * @param userConfirmPassword must be identical as @userPassword
     * @return true if
     */
    private fun checkUserCredentials(
        userName: String,
        emailAddress: String,
        userPassword: String,
        userConfirmPassword: String
    ): Boolean {
        if (userName.isEmpty() || userName.length < 5) {
            username.error = "Please enter a valid username"
            username.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return false
        }

        if (userPassword.isEmpty() || userPassword.length < 6) {
            password.error =
                "The given password is invalid. [Password should be at least 6 characters]"
            password.requestFocus()
            return false
        }
        if (userPassword != userConfirmPassword) {
            password.error = "Entered passwords don't match"
            password.requestFocus()
            return false
        }
        return true
    }

}

