package com.example.hostelpremierleague

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.select_photo.*

class SelectPhoto : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    var PICK_IMAGE = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_photo)
        //when clicked on select photo button
        buttonSelectPhoto.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           if(intent.resolveActivity(packageManager) !=null)
           {
               startActivityForResult(intent , 1)
           }
            Log.d("imageerror","success")
        }
    }
    lateinit var selectedPhoto:Uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        for(fragment : Fragment in supportFragmentManager.fragments)
//        {
//            fragment.onActivityResult(requestCode, resultCode , data)
//        }
        Log.d("imageErr","success")
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data !=null)
        {
            selectedPhoto = data.data

            Log.d("image", "Photo selected ")
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhoto)
            selectPhotoImageView.setImageBitmap(bitmap)
            buttonSelectPhoto.alpha = 0f
        }
    }
}

