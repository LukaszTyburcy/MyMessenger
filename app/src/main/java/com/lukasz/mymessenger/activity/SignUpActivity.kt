package com.lukasz.mymessenger.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.lukasz.mymessenger.R
import com.lukasz.mymessenger.ValidationManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Created by Lukasz on 2018-03-05.
   Sign Up Activity
 */

class SignUpActivity : Activity() {

    var validationManager = ValidationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreen()
        signUpBTN.setOnClickListener{
            if(validationManager.validate(emailSignUpET,passwordSignUpET,nameSignUpET)) {
                signUp(emailSignUpET.text.toString(), passwordSignUpET.text.toString())
            }
        }

    }

    private fun signUp(email: String, password: String){
        MainActivity.Application.mApp.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task: Task<AuthResult> ->
            if (!task.isSuccessful) {
                toast("Unable to add user")
            } else {
                userProfile()
                val user = MainActivity.Application.mApp.mAuth.currentUser
                val userID = user?.email?.replace("@","")?.replace(".","")
                val mRootReference = FirebaseDatabase.getInstance().reference
                val ref1 = mRootReference.child("Users").child(userID)

                ref1.child("Name").setValue(nameSignUpET.text.toString().trim())
                ref1.child("Email").setValue(emailET.text.toString().trim())
                ref1.child("Image_Url").setValue(null)
                startActivity(Intent(this, UserListActivity::class.java))
            }
        }
    }

    private fun userProfile(){
        val user = MainActivity.Application.mApp.mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(emailET.text.toString()).build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener{
            toast("Add new user")
        }
    }

    private fun setScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_signup)
    }

    private fun toast(text: String){
        Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
    }
}