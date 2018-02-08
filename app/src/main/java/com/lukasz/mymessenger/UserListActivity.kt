package com.lukasz.mymessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * Created by Lukasz on 2017-12-22.
Upload Picture
 */
class UserListActivity : AppCompatActivity(){

    private lateinit var mApp: MessengerApplication
    lateinit var databaseRef: DatabaseReference // z mApp
    lateinit var mLinearLayoutManager: LinearLayoutManager
    lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, UserList_ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mApp = application as MessengerApplication
        mLinearLayoutManager = LinearLayoutManager(this@UserListActivity)
        ShowChatRecyclerView.layoutManager = mLinearLayoutManager
        databaseRef = mApp.mDatabase.getReference("Users")
        databaseRef.keepSynced(true)

    }


    override fun onStart() {
        super.onStart()
        Toast.makeText(applicationContext, mApp.mAuth.currentUser?.email.toString(), Toast.LENGTH_SHORT).show()
        mProgressBar.visibility = ProgressBar.VISIBLE
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items,UserList_ViewHolder>(Show_Chat_Activity_Data_Items::class.java,R.layout.single_user,UserList_ViewHolder::class.java,databaseRef){
            override fun populateViewHolder(viewHolder: UserList_ViewHolder?, model: Show_Chat_Activity_Data_Items?, position: Int) {
                mProgressBar.visibility = ProgressBar.INVISIBLE
                if (!(model?.Name.equals("Null") )) {
                    viewHolder?.Person_Name(model!!.Name)
                    viewHolder?.Person_Email(model!!.Email)
                }
                if(model?.Email.equals(mApp.mAuth.currentUser?.email.toString())){
                    viewHolder?.Layout_hide()
                }
            }

        }
        ShowChatRecyclerView.adapter = mFirebaseAdapter
     }

    class UserList_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val person_name: TextView
        private val person_email: TextView
       // private val person_image: ImageView
        private val layout: LinearLayout
        internal val params: LinearLayout.LayoutParams

        init {
            person_name = itemView.findViewById(R.id.name_list)
            person_email = itemView.findViewById(R.id.email_list)
            //person_image = itemView.findViewById(R.id.image_list)
            layout = itemView.findViewById(R.id.single_user_linear_layout)
            params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }


        fun Person_Name(title: String) {
            // Log.d("LOGGED", "Setting Name: ");
            person_name.text = title
        }

        fun Person_Email(title: String) {
            person_email.text = title
        }

        fun Layout_hide() {
            params.height = 0
            layout.layoutParams = params
        }



        /* private fun Person_Image(url: String) {

             if (url != "Null") {
                 Glide.with(itemView.context)
                         .load(url)
                         .crossFade()
                         .thumbnail(0.5f)
                         .placeholder(R.drawable.loading)
                         .bitmapTransform(CircleTransform(itemView.context))
                         .diskCacheStrategy(DiskCacheStrategy.ALL)
                         .into(person_image)
             }*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id.logout){
            mApp.mAuth.signOut()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}

