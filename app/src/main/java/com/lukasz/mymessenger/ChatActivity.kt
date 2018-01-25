package com.lukasz.mymessenger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * Created by Lukasz on 2017-12-22.
Upload Picture
 */
class ChatActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mApp = application as MessengerApplication
        setContentView(R.layout.activity_chat)

        logoutBTN.setOnClickListener{
            mApp.mAuth.signOut()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }


}

