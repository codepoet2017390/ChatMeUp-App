package com.Rohan.flashchatnewfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    // TODO: Add member variables here:
    // UI references.
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mEmailView = findViewById<View>(R.id.login_email) as AutoCompleteTextView
        mPasswordView = findViewById<View>(R.id.login_password) as EditText

        mPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        // TODO: Grab an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
    }

    // Executed when Sign in button pressed
    fun signInExistingUser(v: View) {
        // TODO: Call attemptLogin() here
        attemptLogin()

    }

    // Executed when Register button pressed
    fun registerNewUser(v: View) {
        val intent = Intent(this, com.Rohan.flashchatnewfirebase.RegisterActivity::class.java)
        finish()
        startActivity(intent)
    }

    // TODO: Complete the attemptLogin() method
    private fun attemptLogin() {
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        if (email == "" || password == "") {
            return
        } else {
            Toast.makeText(this, "LOG IN IN PROGRESS", Toast.LENGTH_SHORT).show()
        }


        // TODO: Use FirebaseAuth to sign in with email & password
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            Log.d("FlashChat", "Singning in with email : " + task.isSuccessful)
            if (!task.isSuccessful) {
                Log.d("FlashChat", "failed login : " + task.exception!!)
                ShowErrorDialogue("LOGIN FAILED")
            } else {
                val intent = Intent(this@LoginActivity, MainChatActivity::class.java)
                finish()
                startActivity(intent)

            }
        }


    }

    // TODO: Show error on screen with an alert dialog
    private fun ShowErrorDialogue(message: String) {
        AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, null)
                .show()

    }

}