package com.example.hostelpremierleague

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_timeline.*
import kotlinx.android.synthetic.main.app_bar_timeline.*
import kotlinx.android.synthetic.main.nav_header_timeline.*

class TimelineActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var PICK_IMAGE = 1
    var firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        setSupportActionBar(toolbar)
        val ref  = FirebaseDatabase.getInstance().getReference()


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        displayScreen(-1)

//        buttonSelectPhoto.setOnClickListener {
//            var intent = Intent(Intent.ACTION_PICK)
//            intent.setType("image/*")
//            startActivityForResult(Intent.createChooser(intent, "Select pic"), PICK_IMAGE)
//        }

    }
//    lateinit var selectedPhoto: Uri
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null)
//        {
//            selectedPhoto = data.data
//            Log.d("image", "Photo selected ")
//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhoto)
//            ImageViewProfile.setImageBitmap(bitmap)
//            buttonSelectPhoto.alpha = 0f
//        }
//    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.signOutAppbar -> {
                firebaseAuth.signOut()
                val intent =Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.timeline, menu)
        return super.onCreateOptionsMenu(menu)
    }


   fun displayScreen(id :Int)
   {
        val fragment =when(id)
        {
            R.id.cureentMatches -> {
                            CurrentMatchesActivity()
            }
            R.id.auctions -> {
                            AuctionsForCaptainsActivity()
            }
            R.id.myLeagues -> {
                            MyLeaguesActivity()
            }
            R.id.requestForLeague ->{
                         RequestLeague()
            }
            R.id.about -> {
                            AboutUsActivity()
            }
            R.id.invitePeople -> {
                            InvitePeopleActivity()
            }
            R.id.helpNfeedback -> {
                            HelpAndFeedbackActiviy()
            }
            R.id.signOut -> {
                SignOutActivity()

            }
            R.id.editProfile -> {
                EditProfileFrag()
            }
            else -> {
                            CurrentMatchesActivity()
            }
        }
       supportFragmentManager
           .beginTransaction()
           .replace(R.id.relativeLayout , fragment)
           .commit()
   }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        displayScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
