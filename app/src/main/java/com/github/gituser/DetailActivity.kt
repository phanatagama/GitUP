package com.github.gituser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.gituser.databinding.ActivityDetailBinding
import com.github.gituser.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}