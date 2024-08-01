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
import com.example.singupdatabase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signButton.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.atPasswrod.text.toString().trim()
            var uniqueId = binding.etUniqueId.text.toString().trim()

            // Sanitize the uniqueId to remove any invalid characters
            uniqueId = sanitizeFirebasePath(uniqueId)

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && uniqueId.isNotEmpty()) {
                val user = User(name, email, password, uniqueId)
                database = FirebaseDatabase.getInstance().getReference("User")
                database.child(uniqueId).setValue(user).addOnSuccessListener {
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                    clearFields()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignIn.setOnClickListener{
            val  intent = Intent(this,SignIn::class.java)
            startActivity(intent)
        }
    }

    private fun sanitizeFirebasePath(path: String): String {
        return path.replace(".", "")
            .replace("#", "")
            .replace("$", "")
            .replace("[", "")
            .replace("]", "")
    }
    private fun clearFields() {
        binding.etName.text.clear()
        binding.etEmail.text.clear()
        binding.atPasswrod.text.clear()
        binding.etUniqueId.text.clear()
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
        return super.dispatchTouchEvent(ev) /// mkikk
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}