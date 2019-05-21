package com.example.hostelpremierleague

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
var firebaseAuth = FirebaseAuth.getInstance()
class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        //when already user is clicked
        textViewAlreadyUser.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        //when signup is clicked
        buttonRegister.setOnClickListener {

            var emailRegister = editTextEmailSignUp.text.toString()
            var passwordRegister = editTextPasswordSignUp.text.toString()

            if(emailRegister.isEmpty() || passwordRegister.isEmpty())
            {
                Toast.makeText(this,"Please fill aal fields",Toast.LENGTH_SHORT).show()
                var intent = Intent(this, SignupActivity::class.java)
                this.finish()
                startActivity(intent)
            }

            var progress = ProgressDialog(this)
            progress.setMessage("Registering ..")
            progress.setCancelable(false)
            progress.show()
            createAccount(emailRegister,passwordRegister)
        }
    }
private  fun createAccount(email : String ,password:String)
{
    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
        if(task.isSuccessful)
        {
            Toast.makeText(this,"Registerd Successfully ",Toast.LENGTH_LONG).show()
            var intent = Intent(this ,ProfileActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        else{
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            val intent1 = Intent(this,SignupActivity::class.java)
            startActivity(intent1)
        }
    }
}
}