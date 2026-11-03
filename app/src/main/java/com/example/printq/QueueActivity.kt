package com.example.printq

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Screen 2. Shows the active queue (Waiting and Processing jobs only) to both roles.
class QueueActivity : AppCompatActivity() {

    private lateinit var role: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)

        // Read the role that LoginActivity sent us.
        role = intent.getStringExtra(Extras.ROLE) ?: Extras.STUDENT

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvEmpty = findViewById(R.id.tvEmpty)

        val tvRole = findViewById<TextView>(R.id.tvRole)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnMyJobs = findViewById<Button>(R.id.btnMyJobs)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        // Each role sees only its own buttons.
        if (role == Extras.STUDENT) {
            tvRole.text = "Signed in as Student: ${MockData.CURRENT_STUDENT}"
            btnSubmit.visibility = View.VISIBLE
            btnMyJobs.visibility = View.VISIBLE
            btnHistory.visibility = View.GONE
        } else {
            tvRole.text = "Signed in as Printing Staff"
            btnSubmit.visibility = View.GONE
            btnMyJobs.visibility = View.GONE
            btnHistory.visibility = View.VISIBLE
        }

        btnSubmit.setOnClickListener {
            startActivity(Intent(this, SubmitJobActivity::class.java))
        }
        btnMyJobs.setOnClickListener {
            startActivity(Intent(this, MyJobsActivity::class.java))
        }
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra(Extras.ROLE, role)
            startActivity(intent)
        }
    }

    // onResume runs every time we come back to this screen, so the list is always up to date.
    override fun onResume() {
        super.onResume()
        showQueue()
    }

    private fun showQueue() {
        val jobs = MockData.activeJobs()
        recyclerView.adapter = PrintJobAdapter(jobs) { job -> openDetails(job) }
        tvEmpty.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE
    }

    // Sends the tapped job and the role to the details screen.
    private fun openDetails(job: PrintJob) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(Extras.JOB, job)
        intent.putExtra(Extras.ROLE, role)
        startActivity(intent)
    }
}
