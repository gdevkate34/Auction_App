package com.example.hostelpremierleague

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import kotlinx.android.synthetic.main.current_matches_activity.*

class CurrentMatchesActivity : Fragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSearchLeague.setOnClickListener{
            textViewSearchResult.visibility = View.VISIBLE
            searchLeague(view)
        }
        buttonJoin.setOnClickListener {
            joinLeague(view)
        }
    }
    private fun searchLeague(view: View)
    {
     val snackbar=   Snackbar.make(view,"Searching",Snackbar.LENGTH_SHORT).setActionTextColor(Color.BLUE)

        snackbar.show()

        val leagueCode = editTextSearchLeague.text.toString()
        Log.d("kmkcd","league = $leagueCode")
        val ref =FirebaseDatabase.getInstance().getReference("/leagues")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {


                    val dummy =it.key.toString()
                    Log.d("leagueCode", "League codes : $dummy and $leagueCode")
                    if(dummy== leagueCode)
                    {
//                        Toast.makeText(context,"${it.key}",Toast.LENGTH_SHORT).show()
                        textViewResults.visibility = View.VISIBLE
                        textViewResults1.visibility = View.VISIBLE
                        textViewResults2.visibility = View.VISIBLE

                        FirebaseDatabase.getInstance().getReference("/leagues/${it.key}").addListenerForSingleValueEvent(
                            object: ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot) {
                                   p0.children.forEach {
                                       var name :String= ""
                                       var date :String =""
                                       var organiser:String = ""
                                       if(it.key=="leagueName")
                                       {
                                           name = ""+it.value
                                           textViewResults.text = name
                                       }
                                       if(it.key =="leagueDate")
                                       {
                                           date = "On : "+it.value
                                           textViewResults1.text = date
                                       }
                                       if(it.key =="lorganiser")
                                       {
                                           organiser = "Organised by : "+it.value
                                           textViewResults2.text = organiser
                                       }
                                       val details: String= name+date+organiser
                                       Log.d("bkbk","$details")


                                   }
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                            })
                        buttonJoin.visibility=View.VISIBLE
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private  fun joinLeague(view: View)
    {
        val trimStart = " https://console.firebase.google.com/u/1/project/hostelpremierleague-a6d38/database/hostelpremierleague-a6d38/data/leagues/".count()
        val leagueCode =editTextSearchLeague.text.toString()
        Log.d("kdsn","league id = $leagueCode")
        val applicant = firebaseAuth.currentUser!!.email.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/leagues/$leagueCode/applicants")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val applicantCount = p0.childrenCount
                var applicantsList= mutableListOf<String>()
                p0.children.forEach {
                    applicantsList.add(it.value.toString())
                }
                if(applicantsList.contains(applicant))
                {
                    Toast.makeText(context,"Already Joined !",Toast.LENGTH_SHORT).show()
                }
                else{
                    ref.child("applicant$applicantCount").setValue(applicant)
                    Toast.makeText(context,"Joined",Toast.LENGTH_SHORT).show()
                }
//                if(p0.value!=applicant) {
//                    ref.child("applicant$applicantCount").setValue(applicant)
//                }
//                else {
//                    Snackbar.make(view,"Aleady Joined",Snackbar.LENGTH_SHORT).show()
//                }
//                p0.children.forEach {
//                    if(it.value != applicant)
//                    {
//                        ref.child("applicant$applicantCount").setValue(applicant)
//                        Toast.makeText(context,"Joined a league successfully !",Toast.LENGTH_SHORT).show()
//                    }
//                    else
//                    {
//                        ref.child("applicant$applicantCount").setValue(applicant)
//                        Toast.makeText(context,"Joined a league successfully !",Toast.LENGTH_SHORT).show()
//                    }
//                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

//        ref.child("/applicants").setValue("$applicant")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener{
//            override fun onDataChange(p0: DataSnapshot) {
//                p0.children.forEach {
//                    ref.child("applicants/applicant").
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })

        textViewSearchResult.visibility = View.INVISIBLE
        textViewResults1.visibility = View.VISIBLE
        textViewResults2.visibility = View.VISIBLE
        textViewResults.visibility = View.INVISIBLE
        textViewResults1.visibility = View.INVISIBLE
        textViewResults2.visibility = View.INVISIBLE
        buttonJoin.visibility = View.INVISIBLE
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.current_matches_activity,null)
    }
}