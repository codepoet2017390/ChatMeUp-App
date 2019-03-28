package com.Rohan.flashchatnewfirebase

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

import java.util.ArrayList

class ChatListAdapter(private val mActivity: Activity, ref: DatabaseReference, private val mDisplayName: String) : BaseAdapter() {
    private val mDatabaseReference: DatabaseReference
    private val mSnapshotList: ArrayList<DataSnapshot>
    private val mListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            mSnapshotList.add(dataSnapshot)
            notifyDataSetChanged()
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {

        mDatabaseReference = ref.child("messages")
        mDatabaseReference.addChildEventListener(mListener)
        mSnapshotList = ArrayList()
    }

    override fun getCount(): Int {

        return mSnapshotList.size
    }

    override fun getItem(position: Int): InstantMessage? {
        val snapshot = mSnapshotList[position]
        return snapshot.getValue<InstantMessage>(InstantMessage::class.java!!)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false)
            val holder = ViewHolder()
            holder.authorName = convertView!!.findViewById(R.id.author)
            holder.body = convertView.findViewById(R.id.message)
            holder.params = holder.authorName!!.layoutParams as LinearLayout.LayoutParams
            convertView.tag = holder
        }

        val message = getItem(position)
        val holder = convertView.tag as ViewHolder
        val author = message!!.author
        holder.authorName!!.text = author

        val isMe = message.author == mDisplayName
        setChatRowAppearance(isMe, holder)
        val msg = message.message
        holder.body!!.text = msg

        return convertView
    }


    private fun setChatRowAppearance(isItMe: Boolean, holder: ViewHolder) {
        if (isItMe) {
            holder.params!!.gravity = Gravity.END
            holder.authorName!!.setTextColor(Color.GREEN)
            holder.body!!.setBackgroundResource(R.drawable.bubble2)
        } else {
            holder.params!!.gravity = Gravity.START
            holder.authorName!!.setTextColor(Color.BLUE)
            holder.body!!.setBackgroundResource(R.drawable.bubble1)
        }
        holder.authorName!!.layoutParams = holder.params
        holder.body!!.layoutParams = holder.params

    }

    fun cleanup() {
        mDatabaseReference.removeEventListener(mListener)
    }

    internal class ViewHolder {
        var authorName: TextView? = null
        var body: TextView? = null
        var params: LinearLayout.LayoutParams? = null
    }
}
