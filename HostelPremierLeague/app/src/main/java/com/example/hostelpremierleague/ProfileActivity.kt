package com.example.hostelpremierleague

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
 var firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var current_user = firebaseAuth.currentUser
        var uid= current_user!!.uid

        val ref =FirebaseDatabase.getInstance().getReference("/players")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    if(it.key.toString()==uid)
                    {
                        val intent = Intent(applicationContext, TimelineActivity::class.java)
                        startActivity(intent)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
       current_user?.let {
           val email = current_user.email

           val welcomeText = "Welcome " + email +", You need to fill the foolwing details to participate nand get further Notifications"
           textViewWelcome.text = welcomeText
       }
        buttonsaveDetails.setOnClickListener {
        saveToDatabase()
        }

    }


 private fun saveToDatabase()
 {
     //progress bar to show progress
     var progress = ProgressDialog(this)
     progress.setMessage("Saving Details")
     progress.setCancelable(false)
     progress.show()

     var name = findViewById<EditText>(R.id.editTextName).text.toString()
     var yearNbranch = findViewById<EditText>(R.id.editTextYearNbranch).text.toString()
     var position = findViewById<EditText>(R.id.editTextPosition).text.toString()
     var phone = findViewById<EditText>(R.id.editTextPhone).text.toString()
     var uid = FirebaseAuth.getInstance().uid
     val email = FirebaseAuth.getInstance().currentUser?.email.toString()
     val ref = FirebaseDatabase.getInstance().getReference("players/$uid")
     val player = Player(uid,email , name , yearNbranch,position , phone ,0)
     ref.setValue(player).addOnCompleteListener {
         if(it.isSuccessful) {

             Log.d("save","saved")
             Toast.makeText(this, "Saved Successfully !", Toast.LENGTH_SHORT).show()
             val intent = Intent(this, TimelineActivity::class.java)
             this.finish()
             startActivity(intent)
         }
         else{
             Toast.makeText(this,"failed to save",Toast.LENGTH_SHORT).show()
             val intent1 = Intent(this, ProfileActivity::class.java)
             this.finish()
             startActivity(intent1)
         }
     }

 }

}
