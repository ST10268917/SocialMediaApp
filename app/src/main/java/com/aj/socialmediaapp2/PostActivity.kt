package com.aj.socialmediaapp2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostActivity : AppCompatActivity() {

    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var fabAddPost: FloatingActionButton
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<Post>()

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1002
    private var imageViewInDialog: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts)
        fabAddPost = findViewById(R.id.fabAddPost)

        recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postList)
        recyclerViewPosts.adapter = postAdapter

        fabAddPost.setOnClickListener {
            showAddPostDialog()
        }
    }

    private fun showAddPostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_post, null)
        val editTextCaption = dialogView.findViewById<EditText>(R.id.etPostCaption)
        val imageView = dialogView.findViewById<ImageView>(R.id.ivPostImage)
        imageViewInDialog = imageView
        val btnSave = dialogView.findViewById<Button>(R.id.btnSavePost)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            val caption = editTextCaption.text.toString().trim()

            if (caption.isNotEmpty()) {
                val imageUri = selectedImageUri ?: Uri.parse("android.resource://${packageName}/${R.drawable.ic_launcher_background}")
                val post = Post(caption, imageUri)
                postList.add(post)
                postAdapter.notifyItemInserted(postList.size - 1)
                dialog.dismiss()
                selectedImageUri = null
            } else {
                Toast.makeText(this, "Please enter a caption", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            imageViewInDialog?.setImageURI(selectedImageUri)
        }
    }
}
