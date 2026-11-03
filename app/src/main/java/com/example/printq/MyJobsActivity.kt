package com.example.printq

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Screen 4. A student sees only their own jobs, split into Active and History.
class MyJobsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var btnActive: Button
    private lateinit var btnHistoryTab: Button
    private var showActive = true // which tab is selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_jobs)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnActive = findViewById(R.id.btnActiveTab)
        btnHistoryTab = findViewById(R.id.btnHistoryTab)

        btnActive.setOnClickListener {
            showActive = true
            showList()
        }
        btnHistoryTab.setOnClickListener {
            showActive = false
            showList()
        }
    }

    override fun onResume() {
        super.onResume()
        showList()
    }

    private fun showList() {
        val mine = MockData.jobsOf(MockData.CURRENT_STUDENT)
        val jobs = if (showActive) mine.filter { it.isActive } else mine.filter { !it.isActive }

        recyclerView.adapter = PrintJobAdapter(jobs) { job ->
            val intent = Intent(this, JobDetailsActivity::class.java)
            intent.putExtra(Extras.JOB, job)
            intent.putExtra(Extras.ROLE, Extras.STUDENT)
            startActivity(intent)
        }

        tvEmpty.text = if (showActive) "You have no active jobs." else "No completed or cancelled jobs yet."
        tvEmpty.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE

        // The selected tab is disabled so it looks pressed.
        btnActive.isEnabled = !showActive
        btnHistoryTab.isEnabled = showActive
    }
}
