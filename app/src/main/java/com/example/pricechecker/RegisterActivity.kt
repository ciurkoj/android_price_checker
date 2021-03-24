package com.example.pricechecker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.pricechecker.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG = "=======DocSnippets======="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Log.i("============error1", "=========================>")

        auth = FirebaseAuth.getInstance()

//        register.setOnClickListener{
//            signUpUser()
////        }
        register.setOnClickListener {
           signUpUser()
        }
//
//        setSupportActionBar(findViewById(R.id.toolbar))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
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

        Log.i("============error1", username.text.toString())
        Log.i("============error1", username.text.toString())
        Log.i("============error1", email.text.toString())
        Log.i("============error1", password.text.toString())
        Log.i("============error1", repeat_password.text.toString())

        val email: String = email.text.toString().trim{it<=' '}
        val password: String = password.text.toString().trim{it<=' '}
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "user_name" to username.text.toString(),
                            "user_email" to email,
                            "born" to 1815
                        )

// Add a new document with a generated ID
                        db.collection("user_data").document(email)
                            .set(user)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e)
                            }.addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}