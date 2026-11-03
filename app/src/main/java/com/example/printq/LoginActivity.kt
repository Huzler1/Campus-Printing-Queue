package com.example.printq

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// Screen 1. Phase 1 has no real login yet, so the user just picks a role.
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnStudent = findViewById<Button>(R.id.btnStudent)
        val btnStaff = findViewById<Button>(R.id.btnStaff)

        btnStudent.setOnClickListener { openQueue(Extras.STUDENT) }
        btnStaff.setOnClickListener { openQueue(Extras.STAFF) }
    }

    // Opens the Active Queue and passes the chosen role to it.
    private fun openQueue(role: String) {
        val intent = Intent(this, QueueActivity::class.java)
        intent.putExtra(Extras.ROLE, role)
        startActivity(intent)
    }
}
