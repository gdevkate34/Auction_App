package com.example.hostelpremierleague

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile_edit_activity.*

class EditProfileFrag :Fragment(){
    val userId = firebaseAuth.currentUser!!.uid
    val ref = FirebaseDatabase.getInstance().getReference("players/$userId")
    var email = ""
    var name = ""
    var phone = ""
    var position= ""
    var course = ""
    var leagueCount = ""
    override  fun onViewCreated(view:View ,savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    if(it.key.toString()=="name")
                    {
                        name = it.value.toString()
                        textViewName.text = name
                    }
                    if(it.key.toString()=="email")
                    {
                         email = it.value.toString()
                        textViewEmail.text = email
                    }
                    if(it.key.toString()=="phone_number")
                    {
                        phone = it.value.toString()
                        textViewPhoneNumber.text =phone
                    }
                    if(it.key.toString()=="yearNbranch")
                    {
                        course = it.value.toString()
                        textViewCourse.text = course
                    }
                    if(it.key.toString()=="position")
                    {
                         position =  it.value.toString()
                        textViewPositon.text =position
                    }
                    if(it.key.toString()=="leagueCount")
                    {
                        leagueCount = it.value.toString()
                        textViewEmail.text = leagueCount
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
            Log.d("check","$userId , $name , $email")
            buttonUpdate.setOnClickListener {
                val newPhone : String = editTextPhone.text.toString()
                val newCourse : String = editTextCourse.text.toString()
                val newPosition : String = editTextPosition.text.toString()
                val ref1 = FirebaseDatabase.getInstance().getReference("players/$userId")
                val player = Player(userId,email , name , newCourse,newPosition , newPhone,leagueCount.toInt() )
                ref1.setValue(player).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        Toast.makeText(context , "Updated ", Toast.LENGTH_SHORT).show()
                        CurrentMatchesActivity()
                    }
                    else{
                        Toast.makeText(context , "Failed to upadate !", Toast.LENGTH_SHORT).show()
                        CurrentMatchesActivity()
                    }
                }
            }

        }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.profile_edit_activity, container ,false
        )

    }


}
