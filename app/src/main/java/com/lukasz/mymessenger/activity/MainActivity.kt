package com.lukasz.mymessenger.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.lukasz.mymessenger.*
import com.lukasz.mymessenger.activity.MainActivity.Application.mApp
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : Activity() {

    object Application { lateinit var mApp : MessengerApplication }
    var validationManager = ValidationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApp = application as MessengerApplication

        checkLogin()
        setScreen()

        signInBTN.setOnClickListener{
            if (validationManager.validate(emailET,passwordET)) {
                signIn(emailET.text.toString(), passwordET.text.toString())
            }
        }

        signupTV.setOnClickListener{
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String){
        mApp.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("Signed")
                finish()
                startActivity(Intent(applicationContext, UserListActivity::class.java))
            } else {
                toast("Login Error")
            }
        }
    }

    private fun checkLogin(){
        if (mApp.mAuth.currentUser != null) {
            startActivity(Intent(applicationContext, UserListActivity::class.java))
            finish()
        }
    }

    fun setScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
    }


    private fun toast(text: String){
        Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
    }


}

