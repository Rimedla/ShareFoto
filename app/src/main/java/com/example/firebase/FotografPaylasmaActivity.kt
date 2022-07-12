package com.example.firebase

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

class FotografPaylasmaActivity : AppCompatActivity() {
     var secilengorsel: Uri? = null
     var secilenbitmap:Bitmap? = null
    private lateinit var storage :FirebaseStorage
    private lateinit var auth    :FirebaseAuth
    private lateinit var database:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        storage=FirebaseStorage.getInstance()
        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()
        //actionbar geri tuşu ayarlama
        supportActionBar?.setTitle("Fotograf Paylaş")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    //actionbar geri tuşu ayarlama
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun paylas(view: View){
        //Depo işemleri
        val uuid=UUID.randomUUID()
        val gorselismi="${uuid}.jpg"
        val referance=storage.reference

        val gorselReference=referance.child("images").child(gorselismi)
        if(secilengorsel!=null){
            gorselReference.putFile(secilengorsel!!).addOnSuccessListener {
                val yükelenengorselreferans=FirebaseStorage.getInstance().reference.child("images").child(gorselismi)
                yükelenengorselreferans.downloadUrl.addOnSuccessListener {
                    val downloadUrl=it.toString()
                    val gunceljullanıcı=auth.currentUser!!.email.toString()
                    val kullanıcıyorumu=yorumtext.text.toString()
                    val tarih=com.google.firebase.Timestamp.now()

                    //veritabanı işlemleri
                    val postmap= hashMapOf<String ,Any>()
                    postmap.put("gorselurl",downloadUrl)
                    postmap.put("kullaniciemail",gunceljullanıcı)
                    postmap.put("kullaniciyorum",kullanıcıyorumu)
                    postmap.put("yuklemetarih",tarih)

                    database.collection("Post").add(postmap).addOnCompleteListener {
                        if(it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }


    }
    fun gorselSec(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            // İzin alınmadı ise
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val galeriintent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriintent,2)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val galeriintent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriintent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data !=null){
            secilengorsel=data.data

            if(secilengorsel!=null){
                if(Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilengorsel!!)
                    secilenbitmap=ImageDecoder.decodeBitmap(source)
                    imageView3.setImageBitmap(secilenbitmap)

                }else{
                    secilenbitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilengorsel)
                    imageView3.setImageBitmap(secilenbitmap)
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}