package com.example.hostelpremierleague

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private  val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(firebaseAuth.currentUser !=null){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


        //when clicked on new user new page lfor signup will open
        textViewNewUser.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
            //when clicked on login button
            buttonLogin.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password : String = editTextPassword.text.toString()
                var progress = ProgressDialog(this)
                progress.setMessage("Logging In")
                progress.setCancelable(false)
                progress.show()
                signIn(email,password)
        }



    }
    //method for signin
    private  fun signIn(email:String , password:String)
    {
        val ref2 =FirebaseDatabase.getInstance().getReference("players/")

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please fill aal fields",Toast.LENGTH_SHORT).show()
        }
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                    Toast.makeText(this,"Logged in Successfully !",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,ProfileActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Failed ! Try again !",Toast.LENGTH_SHORT).show()
                val intent1 = Intent(this,MainActivity::class.java)
                startActivity(intent1)
            }
        }
    }
}
