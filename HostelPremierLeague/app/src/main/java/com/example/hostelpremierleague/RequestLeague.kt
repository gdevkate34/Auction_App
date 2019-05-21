package com.example.hostelpremierleague

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
import kotlinx.android.synthetic.main.request_league.*

class RequestLeague : Fragment(){

    val uid = firebaseAuth.currentUser!!.uid
    var captainId = ""
    var captainEmail : String =""
    var captainCount1 :Long = 0
    var numberOfCaptains : Long=1
    //leagueid created here
    val numbers = listOf<Int>(0,1,2,3,4,5,6,7,8,9)
    val alphabets  = listOf<String>("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","x","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z")
    val leagueId ="league"+uid+numbers.random().toString()+alphabets.random()+numbers.random().toString()+alphabets.random()+numbers.random().toString()+alphabets.random()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = firebaseAuth.currentUser!!.uid
        Log.d("user" , " $uid" )



        buttonRequest.setOnClickListener {
           requestLeague()
        }
        buttonDone.setOnClickListener {
                submitCaptains()
        }
        buttonSearchCaptain.setOnClickListener {
            searchCaptain()
        }
        buttonAppoint.setOnClickListener {
            appointCaptain()
        }

    }
    private fun requestLeague(){
        var lorganiser = firebaseAuth.currentUser!!.email.toString()
        val leagueName  = editTextNameOfLeague.text.toString()
        val sportType  = editTextType.text.toString()
        val leagueDate = editTextDateOfLeague.text.toString()
        val league = League(leagueName,sportType,leagueDate, lorganiser)

//        ref.addListenerForSingleValueEvent(object: ValueEventListener{
//            override fun onDataChange(p0: DataSnapshot) {
//
//                leagueId =""+p0.childrenCount +"League"+uid
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })


        val ref1= FirebaseDatabase.getInstance().getReference("/leagues/$leagueId")
        ref1.setValue(league).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(context, "Requested",Toast.LENGTH_SHORT).show()

                textViewAppointCaptian.visibility = View.VISIBLE
                editTextNumberOfCaptains.visibility = View.VISIBLE
                buttonDone.visibility = View.VISIBLE
            }
            else{
                Toast.makeText(context, "Failed to request",Toast.LENGTH_SHORT).show()
                Log.d("user" ,"failed")


            }
        }
    }
    private fun submitCaptains()
    {
        val ediTextNumber = editTextNumberOfCaptains.text.toString()
        numberOfCaptains = ediTextNumber.toLong()
        Log.d("Number of captains","captains = $numberOfCaptains")
        editTextCaptain.visibility = View.VISIBLE
        buttonSearchCaptain.visibility =View.VISIBLE
    }
    private fun searchCaptain(){

        val captain = editTextCaptain.text.toString()
        val ref =FirebaseDatabase.getInstance().getReference("/players")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("leagueCode", "L : ${it.value}")

                    if(it.child("email").value==captain)
                    {
                        val childEmail = it.child("email").value.toString()
                        val childName = it.child("name").value.toString()
                        val totalDetails = childName + "\n" +childEmail
                        captainId = it.child("uid").value.toString()
                        captainEmail = childEmail
                        textViewCaptainResult.visibility =View.VISIBLE
                        textViewCaptain.text = totalDetails
                        textViewCaptain.visibility = View.VISIBLE
                        buttonAppoint.visibility = View.VISIBLE
                        Log.d("child","child = $totalDetails")
                    }
                }
            }


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    private fun appointCaptain()
    {
        Log.d("checkCaptain","$numberOfCaptains")
        Log.d("captainEmail" ,"captain Email = $captainEmail")

        Log.d("sdd","CapId : $captainId")



        val ref = FirebaseDatabase.getInstance().getReference("/leagues/$leagueId/captains")
        val ref1 = FirebaseDatabase.getInstance().getReference("/players/$captainId/myLeagues")
        var captainList = mutableListOf<String>()
                    ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

               captainCount1 = p0.childrenCount
                Log.d("key"," captain Count = $captainCount1")
                p0.children.forEach {
                    captainList.add(it.value.toString())
                    Log.d("tjj","$captainList")
                }
                if(captainList.contains(captainEmail))
                {
                    Toast.makeText(context,"Already Added !",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var leagueCount = 0
                    ref.child("captain$captainCount1").setValue(captainEmail)

                    val ref2 = FirebaseDatabase.getInstance().getReference("/players/$captainId")
                    ref2.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            p0.children.forEach {

                                if(it.key =="leagueCount")
                                {
                                    val temp = it.value.toString()
                                    leagueCount = leagueCount + temp.toInt() + 1
                                    Log.d("error","error $leagueCount")
                                    ref1.child("league$leagueCount").setValue(leagueId)
                                    FirebaseDatabase.getInstance().getReference("/players/$captainId").child("leagueCount").setValue(leagueCount)
                                }
                            }

                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })


                    Toast.makeText(context,"Added !",Toast.LENGTH_SHORT).show()
                    var captainCount2: Long = 0
                    ref.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            captainCount2 = p0.childrenCount
                            if (captainCount2 == numberOfCaptains) {
                                Toast.makeText(context, "All captains added !", Toast.LENGTH_SHORT).show()

                                textViewAppointCaptian.visibility = View.INVISIBLE
                                editTextNumberOfCaptains.visibility = View.INVISIBLE
                                buttonDone.visibility = View.INVISIBLE
                                editTextCaptain.visibility = View.INVISIBLE
                                buttonSearchCaptain.visibility = View.INVISIBLE
                                textViewCaptainResult.visibility = View.INVISIBLE
                                textViewCaptain.visibility = View.INVISIBLE
                                buttonAppoint.visibility = View.INVISIBLE

                            }
                            Log.d("log2","count 2 : $captainCount2")
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })

                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        textViewCaptainResult.visibility = View.INVISIBLE
        textViewCaptain.visibility = View.INVISIBLE
        buttonAppoint.visibility = View.INVISIBLE



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.request_league,container,false)
    }
}
