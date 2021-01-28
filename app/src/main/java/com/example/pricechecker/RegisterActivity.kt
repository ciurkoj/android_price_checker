package com.example.pricechecker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.example.pricechecker.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.password
import kotlinx.android.synthetic.main.activity_register.username

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

//        register.setOnClickListener{
//            signUpUser()
//        }
        register.setOnClickListener {
            signUpUser()

        }

        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun signUpUser() {


        if (username.text.toString().isEmpty()) {
            print(username)
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
        val email: String = email.text.toString().trim{it<=' '}
        val password: String = password.text.toString().trim{it<=' '}
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}