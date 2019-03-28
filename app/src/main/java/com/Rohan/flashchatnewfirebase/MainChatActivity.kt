package com.Rohan.flashchatnewfirebase

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainChatActivity : AppCompatActivity() {

    // TODO: Add member variables here:
    private var mDisplayName: String? = null
    private var mChatListView: ListView? = null
    private var mInputText: EditText? = null
    private var mSendButton: ImageButton? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAdapter: ChatListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)

        // TODO: Set up the display name and get the Firebase reference
        setUpDisplayName()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        // Link the Views in the layout to the Java code
        mInputText = findViewById<View>(R.id.messageInput) as EditText
        mSendButton = findViewById<View>(R.id.sendButton) as ImageButton
        mChatListView = findViewById<View>(R.id.chat_list_view) as ListView

        // TODO: Send the message when the "enter" button is pressed
        mInputText!!.setOnEditorActionListener { v, actionId, event ->
            sendMessage()
            true
        }

        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton!!.setOnClickListener { sendMessage() }
    }

    // TODO: Retrieve the display name from the Shared Preferences
    private fun setUpDisplayName() {
        val prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, Context.MODE_PRIVATE)
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null)
        if (mDisplayName == null)
            mDisplayName = "Anonymous"
    }

    private fun sendMessage() {
        Log.d("FlashChat", "I sent something")
        // TODO: Grab the text the user typed in and push the message to Firebase
        val input = mInputText!!.text.toString()
        if (input != "") {
            val chat = InstantMessage(input, mDisplayName!!)
            mDatabaseReference!!.child("messages").push().setValue(chat)
            mInputText!!.setText("")

        }
    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.
    public override fun onStart() {

        super.onStart()

        mAdapter = ChatListAdapter(this, mDatabaseReference!!, mDisplayName!!)
        mChatListView!!.adapter = mAdapter
    }

    public override fun onStop() {
        super.onStop()

        // TODO: Remove the Firebase event listener on the adapter.
        mAdapter!!.cleanup()
    }

}
