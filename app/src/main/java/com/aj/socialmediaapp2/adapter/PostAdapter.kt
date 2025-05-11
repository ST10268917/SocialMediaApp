package com.aj.socialmediaapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPost: ImageView = itemView.findViewById(R.id.imageViewPostItem)
        val textViewCaption: TextView = itemView.findViewById(R.id.textViewCaption)
        val btnLike: ImageView = itemView.findViewById(R.id.btnLike)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val tvComments: TextView = itemView.findViewById(R.id.tvComments)
        val etCommentInput: EditText = itemView.findViewById(R.id.etCommentInput)
        val btnSubmitComment: Button = itemView.findViewById(R.id.btnSubmitComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.imageViewPost.setImageURI(post.imageUri)
        holder.textViewCaption.text = post.caption
        holder.tvLikeCount.text = "${post.likeCount} Likes"

        // Show comments
        holder.tvComments.text = if (post.comments.isNotEmpty())
            post.comments.joinToString("\n") else "No comments yet"

        // Like button
        holder.btnLike.setOnClickListener {
            post.likeCount++
            notifyItemChanged(position)
        }

        // Submit comment
        holder.btnSubmitComment.setOnClickListener {
            val comment = holder.etCommentInput.text.toString().trim()
            if (comment.isNotEmpty()) {
                post.comments.add(comment)
                holder.etCommentInput.text.clear()
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = posts.size
}
