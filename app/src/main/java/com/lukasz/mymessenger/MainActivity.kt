package com.lukasz.mymessenger

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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var mApp: MessengerApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApp = application as MessengerApplication
        if (mApp.mAuth.currentUser != null) {
            startActivity(Intent(applicationContext, UserListActivity::class.java))
            finish()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        signInBTN.setOnClickListener{
            signIn(emailET.text.toString(),passwordET.text.toString())

        }

        signUpBTN.setOnClickListener{
            signUp(emailET.text.toString(),passwordET.text.toString())
        }
    }


    private fun signUp(email: String, password: String){
        mApp.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task: Task<AuthResult> ->
            if (!task.isSuccessful) {
                toast("Nie udało sie dodać użytkownika")
            } else {
                userProfile()
                val user = mApp.mAuth.currentUser
                val userID = user?.email?.replace("@","")?.replace(".","")
                val mRootReference = FirebaseDatabase.getInstance().getReference()
                val ref1 = mRootReference.child("Users").child(userID)

                ref1.child("Name").setValue(nameET.text.toString().trim())
                ref1.child("Email").setValue(emailET.text.toString().trim())
                ref1.child("Image_Url").setValue(null)
                startActivity(Intent(this, UserListActivity::class.java))
            }
        }
    }

    private fun userProfile(){
        val user = mApp.mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(emailET.text.toString()).build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener{
            toast("Dodano nowe konto")
        }
    }

    private fun signIn(email: String, password: String){
        mApp.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("Zalogowano")
                finish()
                startActivity(Intent(applicationContext, UserListActivity::class.java))
            } else {
                toast("Błąd logowania")
            }
        }
    }


    private fun toast(text: String){
        Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
    }
}
