package com.example.singupdatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.singupdatabase.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mail = intent.getStringExtra(SignIn.KEY1)
        val name = intent.getStringExtra(SignIn.KEY2)
        val userId = intent.getStringExtra(SignIn.KEY3)

        binding.tvWelcome.text = "Welcome : $name"
        binding.tvMail.text = "Mail : $mail"
        binding.tvUnique.text = "UserId : $userId"
    }
}