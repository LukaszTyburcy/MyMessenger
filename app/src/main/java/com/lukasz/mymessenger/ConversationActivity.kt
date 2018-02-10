package com.lukasz.mymessenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.chat_conversation.*

/**
 * Created by Lukasz on 2018-02-09.
Upload Picture
 */
class ConversationActivity : AppCompatActivity() {

    private lateinit var mApp: MessengerApplication
    lateinit var ref2 : DatabaseReference
    lateinit var ref1 :DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_conversation)

        mApp = application as MessengerApplication
        val USER_ID : String =mApp.mAuth.currentUser?.email.toString().replace("@","").replace(".","")
        //mApp.mDatabase.reference.child()
        mApp.mDatabase.reference.keepSynced(true)

        ref1 = FirebaseDatabase.getInstance().reference.child("Chat").child(USER_ID).child(intent.getStringExtra("email").replace(".","").replace(".",""))
        ref1.keepSynced(true)

        ref2 = FirebaseDatabase.getInstance().reference.child("Chat").child(intent.getStringExtra("email").replace(".","").replace(".","")).child(USER_ID)
        ref2.keepSynced(true)


        //if(actionBar != null){
        //    actionBar.setDisplayHomeAsUpEnabled(true)
        //    actionBar.title = Html.fromHtml("<font color=#FFFFFF>" + intent.getStringExtra("name") + "</font>")
       // }

        sendIV.setOnClickListener{
            val messageText : String = messageET.text.toString().trim()
            if(!messageText.equals("")){
               val map = mapOf<String,String>("message" to messageText ,"sender" to mApp.mAuth.currentUser?.email.toString())
                ref1.push().setValue(map)
                ref2.push().setValue(map)
                messageET.text.clear()

                    conversationRecycler.postDelayed({
                        conversationRecycler.smoothScrollToPosition(conversationRecycler.adapter.itemCount-1)
                    }, 500)

                }
            }
        }

    }
