package com.aj.socialmediaapp2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        imageView = findViewById(R.id.imageViewProfilePic)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank() || selectedImageUri == null) {
                Toast.makeText(this, "All fields and image required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user!!.uid
                    uploadProfilePictureAndSaveUser(uid, name, email)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Registration Failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun uploadProfilePictureAndSaveUser(uid: String, name: String, email: String) {
        val filename = "profile_pics/$uid.jpg"
        val ref = FirebaseStorage.getInstance().reference.child(filename)

        selectedImageUri?.let { uri ->
            ref.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                ref.downloadUrl
            }.addOnSuccessListener { downloadUrl ->
                val userMap = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "profilePic" to downloadUrl.toString()
                )
                FirebaseFirestore.getInstance().collection("users").document(uid).set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)
        }
    }
}
