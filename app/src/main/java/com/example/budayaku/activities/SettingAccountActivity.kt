package com.example.budayaku.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.MainActivity
import com.example.budayaku.R
import com.example.budayaku.databases.Module
import com.example.budayaku.databases.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_setting_account.*
import java.io.IOException

class SettingAccountActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage.reference

    private val listDaerah = ArrayList<String>()

    private var selectedDaerah: String = ""

    val currentUser = FirebaseAuth.getInstance().currentUser
    var default =
        "https://firebasestorage.googleapis.com/v0/b/budayaku-6298f.appspot.com/o/User%2Fdefault-profile.png?alt=media&token=06808e07-8e24-467c-9c91-0fd85f54b3e4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_account)

        iv_backSetting.setOnClickListener { onBackPressed() }

        FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid).get()
            .addOnSuccessListener {
                //                val data: User? = it.toObject(User::class.java)
                Glide.with(this).load(default)
                    .apply(RequestOptions())
                    .into(civ_userPhoto)
            }

        fab_edtPhotoAvatar.setOnClickListener {
            chooseImage()
        }

        btn_setAccount.setOnClickListener {
            updateUserProfile()
        }

        showListDaerah()
    }

    private fun showListDaerah() {
        db.collection("daerah").orderBy("name").get()
            .addOnSuccessListener {
                for (doc in it.documents) {
                    val daerah: Module = doc.toObject()!!

                    listDaerah.add(daerah.name)
                }

                selectedDaerah = listDaerah[0]

                setupDaerahSpinner()
            }
    }

    private fun setupDaerahSpinner() {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listDaerah)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_setLocation.apply {
            adapter = arrayAdapter

            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedDaerah = listDaerah[position]
                    }

                }
        }
    }

    private fun updateUserProfile() {
        val username = et_setUsername.text.toString()
        val phone = et_setPhone.text.toString()
        val location = selectedDaerah

        if (username.isEmpty()) et_setUsername.error = "Username tidak boleh kosong"
        if (phone.isEmpty()) et_setPhone.error = "No. Telpon tidak boleh kosong"
        if (username.isEmpty() || phone.isEmpty()) return

        if (filePath != null) {
            set_block.visibility = View.VISIBLE
            set_load.visibility = View.VISIBLE

            val ref = storage.child("User/${currentUser?.uid}.jpg")

            ref.putFile(filePath!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val updateName = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .setPhotoUri(it)
                        .build()

                    currentUser!!.updateProfile(updateName).addOnSuccessListener {
                        Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Photo Profile Updated", Toast.LENGTH_SHORT).show()

                        db.collection("users").document(currentUser.uid)
                            .set(
                                User(
                                    username,
                                    phone,
                                    location,
                                    currentUser.photoUrl.toString()
                                )
                            ).addOnSuccessListener {
                                set_block.visibility = View.GONE
                                set_load.visibility = View.GONE

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }.addOnFailureListener { e ->
                                set_block.visibility = View.GONE
                                set_load.visibility = View.GONE

                                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please choose your photo profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        val mimeType = arrayOf("image/jpeg", "image/png")

        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)

        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                civ_userPhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
