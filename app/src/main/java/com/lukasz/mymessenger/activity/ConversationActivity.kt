package com.lukasz.mymessenger.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.lukasz.mymessenger.activity.MainActivity.Application.mApp
import com.lukasz.mymessenger.model.Message
import com.lukasz.mymessenger.R
import com.lukasz.mymessenger.activity.ConversationActivity.DeviceMetrics.device_width
import com.lukasz.mymessenger.activity.ConversationActivity.DeviceMetrics.metrics
import com.lukasz.mymessenger.activity.ConversationActivity.SenderName.sender_info
import com.lukasz.mymessenger.MessageNotification
import kotlinx.android.synthetic.main.chat_conversation.*


/**
 * Created by Lukasz on 2018-02-09.
   Conversation Activity
 */
class ConversationActivity : AppCompatActivity() {


    private lateinit var ref2 : DatabaseReference
    private lateinit var ref1 :DatabaseReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var mLinearLayoutManager2: LinearLayoutManager
    private lateinit var mFirebaseAdapter2: FirebaseRecyclerAdapter<Message, ChatConversationViewHolder>
    private var myTask: MessageNotification? = null

    object DeviceMetrics {
        lateinit var metrics : DisplayMetrics
        var device_width : Int = 0
    }

    object SenderName { var sender_info : String? = null }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sender_info = intent.getStringExtra("name")
        val USER_ID : String = mApp.mAuth.currentUser?.email.toString().replace("@","").replace(".","")
        val USER_NAME : String? = mApp.mAuth.currentUser?.displayName

        getDatabaseReference(USER_ID)
        setLayoutManager()
        setDeviceMetrics()



        sendIV.setOnClickListener{
            val messageText : String = messageET.text.toString().trim()
            if(!messageText.equals("")){
               val map = mapOf("message" to messageText ,"sender" to mApp.mAuth.currentUser?.email.toString())
                ref1.push().setValue(map)
                ref2.push().setValue(map)
                messageET.text.clear()

                myTask = MessageNotification()
                myTask!!.execute(USER_NAME)
                conversationRecycler.postDelayed({
                    conversationRecycler.smoothScrollToPosition(conversationRecycler.adapter.itemCount-1)
                }, 500)
            }
           myTask = null
        }
    }


    override fun onStart() {
        super.onStart()
        mFirebaseAdapter2 = object : FirebaseRecyclerAdapter<Message, ChatConversationViewHolder>(Message::class.java, R.layout.single_message, ChatConversationViewHolder::class.java,ref1){
            override fun populateViewHolder(viewHolder: ChatConversationViewHolder?, model: Message?, position: Int) {
                viewHolder?.getSender(model!!.sender)
                viewHolder?.getMessage(model!!.message)
            }
        }
        conversationRecycler.adapter = mFirebaseAdapter2
    }

    private fun getDatabaseReference(USER_ID : String){
        databaseRef = FirebaseDatabase.getInstance().reference.child("Chat")

        ref1 = FirebaseDatabase.getInstance().reference.child("Chat").child(USER_ID).child(intent.getStringExtra("email").replace(".","").replace("@",""))
        ref1.keepSynced(true)

        ref2 = FirebaseDatabase.getInstance().reference.child("Chat").child(intent.getStringExtra("email").replace(".","").replace("@","")).child(USER_ID)
        ref2.keepSynced(true)
    }

    private fun setLayoutManager(){
        setContentView(R.layout.chat_conversation)
        mLinearLayoutManager2 = LinearLayoutManager(this@ConversationActivity)
        conversationRecycler.layoutManager = mLinearLayoutManager2
        mLinearLayoutManager2.stackFromEnd = true
    }

    private fun setDeviceMetrics(){
        metrics = applicationContext.resources.displayMetrics
        device_width = metrics.widthPixels
    }


    class ChatConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val message: TextView = itemView.findViewById(R.id.chatMessageTV)
        private val sender: TextView = itemView.findViewById(R.id.chatSenderTV)
        private val chat_image_incoming: ImageView? = null
        private val chat_image_outcoming: ImageView? = null
        private var params = message.layoutParams as LinearLayout.LayoutParams
        private var text_params = message.layoutParams as LinearLayout.LayoutParams

        init {
            chat_image_incoming?.setImageResource(R.drawable.shape_incomming_message)
            chat_image_outcoming?.setImageResource(R.drawable.shape_outcomming_message)
        }

        fun getSender(title: String) {

            if (title.equals(mApp.mAuth.currentUser?.email.toString())) {
                params.setMargins( (device_width/3), 5, 10, 10)
                text_params.setMargins(15, 10, 0, 5)
                itemView.layoutParams = params
                sender.layoutParams = text_params
                itemView.setBackgroundResource(R.drawable.shape_outcomming_message)
                chat_image_outcoming?.visibility = View.VISIBLE
                chat_image_incoming?.visibility = View.GONE
                sender.text = "YOU"
                sender.gravity = Gravity.RIGHT
                message.gravity = Gravity.RIGHT
            } else {
                itemView.layoutParams = params
                itemView.setBackgroundResource(R.drawable.shape_incomming_message)
                sender.text = sender_info
                chat_image_outcoming?.visibility = View.GONE
                chat_image_incoming?.visibility = View.VISIBLE
                sender.gravity = Gravity.LEFT
                message.gravity = Gravity.LEFT
            }
        }

        fun getMessage (title : String){
            if(!sender.text.equals(SenderName.sender_info))
            {
                text_params.setMargins(15,10,22,15)
            }
            else
            {
                text_params.setMargins(65,10,22,15)
            }
            message.layoutParams = text_params
            message.text = title
            message.visibility = View.VISIBLE
            chat_image_incoming?.visibility = View.GONE
            chat_image_outcoming?.visibility = View.GONE
        }

    }
}
