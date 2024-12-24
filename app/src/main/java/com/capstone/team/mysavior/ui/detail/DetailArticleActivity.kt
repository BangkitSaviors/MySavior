package com.capstone.team.mysavior.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Terima data dari Intent
        val title = intent.getStringExtra("TITLE")
        val description = intent.getStringExtra("DESCRIPTION")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val content = intent.getStringExtra("CONTENT")
        val publishedAt = intent.getStringExtra("PUBLISHED_AT")
        val author = intent.getStringExtra("AUTHOR")

        // Set data ke view using binding
        binding.textViewDetailTitle.text = title
        binding.textViewDetailDesc.text = description
        binding.textViewDetailContent.text = content
        binding.textViewDetailPublishedAt.text = publishedAt ?: "Unknown"
        binding.textViewDetailAuthor.text = author ?: "Anonymous"

        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageViewDetailCover)
    }
}