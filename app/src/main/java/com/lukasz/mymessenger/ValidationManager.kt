package com.lukasz.mymessenger

import android.text.TextUtils
import android.widget.EditText


/**
 * Created by Lukasz on 2018-03-06.
Validate data sign in or sign up
 */
class ValidationManager {

    fun validate(emailEditText : EditText, passwordEditText : EditText ) : Boolean{
        var error = true
        if (TextUtils.isEmpty(emailEditText.text.toString())){
            emailEditText.error = "Email can't be empty"
            error = false
        }
        if (TextUtils.isEmpty(passwordEditText.text.toString())){
            passwordEditText.error = "Password can't be empty"
            error = false
        }
        return error
    }

    fun validate(emailEditText : EditText, passwordEditText : EditText, nameEditText : EditText): Boolean {
        var error = true
        if (TextUtils.isEmpty(emailEditText.text.toString())){
            emailEditText.error = "Email can't be empty"
            error = false
        }
        if (TextUtils.isEmpty(passwordEditText.text.toString())){
            passwordEditText.error = "Password can't be empty"
            error = false
        }

        if(TextUtils.isEmpty(nameEditText.text.toString())){
            nameEditText.error = "Name can't be empty"
            error = false
        }
        return error
    }
}