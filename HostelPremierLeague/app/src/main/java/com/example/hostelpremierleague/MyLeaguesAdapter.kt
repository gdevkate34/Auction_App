package com.example.hostelpremierleague

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.my_leagues_list.view.*

class MyLeaguesAdapter : RecyclerView.Adapter<CustomViewHolder>(){
    val user = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val list = listOf<String>("gaju")
    var leagueIds = mutableListOf<String>()
    var leagueNames = mutableListOf<String>()
    var leagueDates = mutableListOf<String>()
    var organisers = mutableListOf<String>()
    val ref = FirebaseDatabase.getInstance().getReference("/players/$user/myLeagues")
var countLeague = ""

    override fun getItemCount(): Int {
        val ref =FirebaseDatabase.getInstance().getReference("/players/$user/myLeagues")
        ref.addListenerForSingleValueEvent(object  :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("bhbb","countl ${p0.childrenCount}")
                countLeague = countLeague + p0.childrenCount.toString()
                Log.d("bhbb","countl $countLeague")
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        return 2
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellRow = layoutInflater.inflate(R.layout.my_leagues_list, p0, false)
        return  CustomViewHolder(cellRow)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    if(it.key =="Organiser")
                    {
                        organisers.add(it.value.toString())
                        Log.d("org","$organisers")
                        val organiser = organisers.get(position)
                        holder.view.textViewOrganiser.text = organiser
                    }
                    else
                    {
                        leagueIds.add(it.value.toString())
                        val leagueId  = leagueIds.get(position)
                        FirebaseDatabase.getInstance().getReference("/leagues").addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                               p0.children.forEach {
                                   if(it.key == leagueId)
                                   {
                                       val leagueName =it.child("leagueName").value.toString()
                                       val leagueDate =it.child("leagueDate").value.toString()
                                       val organiser =it.child("lorganiser").value.toString()
                                       holder.view.textViewMyLeagueName.text = leagueName
                                       holder.view.textViewDate.text = leagueDate
                                       holder.view.textViewOrganiser.text = organiser
                                   }
                               }
                            }
                        })

                        Log.d("ids","$leagueIds and ${leagueIds.count()}")
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }


}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)