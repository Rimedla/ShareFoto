package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_postactivity.*

class Postactivity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth
    private lateinit var database :FirebaseFirestore
    private lateinit var recadaptor:Postadapter
    var postlistesi=ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postactivity)
        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()
        verilerial()
        var layoutmanager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutmanager
        recadaptor= Postadapter(postlistesi)
        recyclerView.adapter=recadaptor



    }
    fun verilerial(){
        database.collection("Post").orderBy("yuklemetarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if(exception!=null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                if(snapshot!=null){
                    if(snapshot.isEmpty==false){
                        val listdoc=snapshot.documents

                        postlistesi.clear()

                        for(document in listdoc){
                            val email=document.get("kullaniciemail") as String
                            val yorum=document.get("kullaniciyorum") as String
                            val gorsel=document.get("gorselurl") as String
                            val indirilenPost=Post(email,yorum,gorsel)
                            postlistesi.add(indirilenPost)

                        }
                        recadaptor.notifyDataSetChanged()
                    }
                }

            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuinflater=menuInflater
        menuinflater.inflate(R.menu.secenek_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.foto_share){
            //fotoğraf paylaşma aktivitesine git
            val intent=Intent(this,FotografPaylasmaActivity::class.java)
            startActivity(intent)


        }else if(item.itemId==R.id.cikis_yap){
            auth.signOut()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }



        return super.onOptionsItemSelected(item)
    }
}