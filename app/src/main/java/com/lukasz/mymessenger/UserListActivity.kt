package com.lukasz.mymessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * Created by Lukasz on 2017-12-22.
Upload Picture
 */
class UserListActivity : AppCompatActivity(){

    private lateinit var mApp: MessengerApplication
    private lateinit var databaseRef: DatabaseReference
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<User, UserListViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_chat)

        mApp = application as MessengerApplication

        mLinearLayoutManager = LinearLayoutManager(this@UserListActivity)
        ShowChatRecyclerView.layoutManager = mLinearLayoutManager

        databaseRef = mApp.mDatabase.getReference("Users")
        databaseRef.keepSynced(true)

    }


    override fun onStart() {
        super.onStart()
        mProgressBar.visibility = ProgressBar.VISIBLE

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<User, UserListViewHolder>(User::class.java,R.layout.single_user, UserListViewHolder::class.java,databaseRef){
            override fun populateViewHolder(viewHolder: UserListViewHolder?, model: User?, position: Int) {

                mProgressBar.visibility = ProgressBar.INVISIBLE

                if (!(model?.Name.equals("Null") )) {
                    viewHolder?.Person_Name(model!!.Name)
                    viewHolder?.Person_Email(model!!.Email)
                }
                if(model?.Email.equals(mApp.mAuth.currentUser?.email.toString())){
                    viewHolder?.Layout_hide()
                }


                viewHolder?.itemView?.setOnClickListener {
                    val ref : DatabaseReference = mFirebaseAdapter.getRef(position)
                    val UserListener = object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                            //Toast.makeText(applicationContext,"Błąd", Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(p0: DataSnapshot?) {
                            val retrieve_name = p0?.child("Name")?.getValue(String::class.java)
                            val retrieve_Email = p0?.child("Email")?.getValue(String::class.java)
                            val retrieve_url = p0?.child("Image_URL")?.getValue(String::class.java)

                            val intent = Intent(applicationContext, ConversationActivity::class.java)
                            intent.putExtra("image_id", retrieve_url)
                            intent.putExtra("email", retrieve_Email)
                            intent.putExtra("name", retrieve_name)
                            startActivity(intent)
                        }
                    }
                    ref.addValueEventListener(UserListener)
                }
            }
        }
        ShowChatRecyclerView.adapter = mFirebaseAdapter
     }

    class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val person_name: TextView = itemView.findViewById(R.id.name_list)
        private val person_email: TextView = itemView.findViewById(R.id.email_list)
        private val layout: LinearLayout = itemView.findViewById(R.id.single_user_linear_layout)
        private val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        fun Person_Name(title: String) {
            person_name.text = title
        }

        fun Person_Email(title: String) {
            person_email.text = title
        }

        fun Layout_hide() {
            params.height = 0
            layout.layoutParams = params
        }
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

