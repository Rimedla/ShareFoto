package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        ///////////////////actionbar gizleme//////////////////////////////
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        /////////////////////////////////////////////////////////////////

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()
        val guncelkullanıcı=auth.currentUser
        if(guncelkullanıcı!=null){
            val intent=Intent(this,Postactivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun girisYap(view:View){
        auth.signInWithEmailAndPassword(emailtext.text.toString(),passwordtext.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val guncelkullanıcı=auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldin:${guncelkullanıcı}",Toast.LENGTH_LONG).show()

                val intent=Intent(applicationContext,Postactivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exeption ->
            Toast.makeText(this,exeption.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
    fun kayıtOl(view: View){
        val email=emailtext.text.toString()
        val sifre=passwordtext.text.toString()
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->
            //asekron çalışıyor.
            if(task.isSuccessful){
                //diğer aktiviteye git
                val intent=Intent(applicationContext,Postactivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exeption ->
            Toast.makeText(applicationContext,exeption.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
}