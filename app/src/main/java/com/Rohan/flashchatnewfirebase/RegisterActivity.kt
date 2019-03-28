package com.Rohan.flashchatnewfirebase

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    // TODO: Add member variables here:
    // UI references.
    private var mEmailView: AutoCompleteTextView? = null
    private var mUsernameView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mConfirmPasswordView: EditText? = null
    private var mAuth: FirebaseAuth? = null
    // Firebase instance variables


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mEmailView = findViewById<View>(R.id.register_email) as AutoCompleteTextView
        mPasswordView = findViewById<View>(R.id.register_password) as EditText
        mConfirmPasswordView = findViewById<View>(R.id.register_confirm_password) as EditText
        mUsernameView = findViewById<View>(R.id.register_username) as AutoCompleteTextView

        // Keyboard sign in action
        mConfirmPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                attemptRegistration()
                return@OnEditorActionListener true
            }
            false
        })

        // TODO: Get hold of an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

    }

    // Executed when Sign Up button is pressed.
    fun signUp(v: View) {
        attemptRegistration()
    }

    private fun attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView!!.error = null
        mPasswordView!!.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        // You can add more checking logic here.
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)

        val confirmPassword = mConfirmPasswordView!!.text.toString()
        return confirmPassword == password && password.length > 6

    }

    // TODO: Create a Firebase user
    private fun createFirebaseUser() {
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            Log.d("FlashChat", "create User OnComplete : " + task.isSuccessful)
            if (!task.isSuccessful) {
                Log.d("FlashChat", "user creation failed")
                showErrorDailog("Registration attempt failed")
            } else {

                saveDisplayName()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }


    // TODO: Save the display name to Shared Preferences

    private fun saveDisplayName() {
        val displayName = mUsernameView!!.text.toString()
        val prefs = getSharedPreferences(CHAT_PREFS, 0)
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply()
    }

    // TODO: Create an alert dialog to show in case registration failed
    private fun showErrorDailog(message: String) {
        AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()


    }

    companion object {

        // Constants
        val CHAT_PREFS = "ChatPrefs"
        val DISPLAY_NAME_KEY = "username"
    }


}
