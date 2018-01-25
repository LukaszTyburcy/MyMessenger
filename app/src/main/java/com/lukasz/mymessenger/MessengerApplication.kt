package com.lukasz.mymessenger

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase



/**
 * Created by Lukasz on 2017-12-24.
Upload Picture
 */
class MessengerApplication : Application() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        mDatabase = FirebaseDatabase.getInstance()
        if (mDatabase == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }
}
