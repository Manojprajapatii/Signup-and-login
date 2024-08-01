package com.example.singupdatabase

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.singupdatabase.databinding.ActivitySignInBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var databaseReference: DatabaseReference

    companion object {
        const val KEY1 = "com.example.singupdatabase.SignIn.mail"
        const val KEY2 = "com.example.singupdatabase.SignIn.name"
        const val KEY3 = "com.example.singupdatabase.SignIn.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingIn.setOnClickListener {
            val uniqueId = binding.userName.text.toString()
            if (uniqueId.isNotEmpty()) {
                readData(uniqueId)
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData(uniqueId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")

        databaseReference.child(uniqueId).get().addOnSuccessListener {
            if (it.exists()) {
                val email = it.child("email").value
                val name = it.child("name").value
                val userId = it.child("uniqueId").value

                val intentWelcome = Intent(this, WelcomeActivity::class.java)
                intentWelcome.putExtra(KEY1, email.toString())
                intentWelcome.putExtra(KEY2, name.toString())
                intentWelcome.putExtra(KEY3, userId.toString())
                startActivity(intentWelcome)
            } else {
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    hideKeyboard(this, v)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
