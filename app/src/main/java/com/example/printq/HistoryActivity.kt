package com.example.printq

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Screen 5 (staff). Lists the Completed and Cancelled jobs that left the active queue.
class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvEmpty = findViewById(R.id.tvEmpty)
    }

    override fun onResume() {
        super.onResume()
        val jobs = MockData.historyJobs()
        recyclerView.adapter = PrintJobAdapter(jobs) { job ->
            val intent = Intent(this, JobDetailsActivity::class.java)
            intent.putExtra(Extras.JOB, job)
            intent.putExtra(Extras.ROLE, Extras.STAFF)
            startActivity(intent)
        }
        tvEmpty.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE
    }
}
