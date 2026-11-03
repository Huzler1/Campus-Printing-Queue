package com.example.printq

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Screen 6. Shows one job. The buttons you see depend on your role and on the job's status.
class JobDetailsActivity : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        // Receive the data sent by the previous screen.
        val job = intent.getSerializableExtra(Extras.JOB) as PrintJob
        val role = intent.getStringExtra(Extras.ROLE) ?: Extras.STUDENT

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvQueueNumber = findViewById<TextView>(R.id.tvQueueNumber)
        val tvPosition = findViewById<TextView>(R.id.tvPosition)
        val tvDocument = findViewById<TextView>(R.id.tvDocument)
        val tvStudent = findViewById<TextView>(R.id.tvStudent)
        val tvPrintInfo = findViewById<TextView>(R.id.tvPrintInfo)
        val tvNotes = findViewById<TextView>(R.id.tvNotes)
        val tvCancelReason = findViewById<TextView>(R.id.tvCancelReason)
        val etReason = findViewById<EditText>(R.id.etReason)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnComplete = findViewById<Button>(R.id.btnComplete)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // Show the job's data.
        tvQueueNumber.text = "Queue #%03d".format(job.queueNumber)
        tvStatus.text = job.status.label
        tvStatus.setBackgroundColor(statusColor(job.status))
        tvDocument.text = "Document: ${job.documentName}"
        tvStudent.text = "Student: ${job.studentName}"
        tvPrintInfo.text =
            "${job.pages} pages, ${job.copies} copies, ${job.colorMode}, ${job.paperSize}"
        tvNotes.text = "Notes: " + job.notes.ifEmpty { "none" }

        // Active jobs show their position; finished jobs are no longer in the queue.
        tvPosition.text = if (job.isActive) {
            "Position in the active queue: ${MockData.positionOf(job)}"
        } else {
            "Not in the active queue"
        }

        if (job.status == JobStatus.CANCELLED) {
            tvCancelReason.visibility = View.VISIBLE
            tvCancelReason.text = "Reason: ${job.cancelReason}"
        }

        // Decide which buttons to show.
        if (role == Extras.STAFF) {
            btnStart.visibility = if (job.status == JobStatus.WAITING) View.VISIBLE else View.GONE
            btnComplete.visibility = if (job.status == JobStatus.PROCESSING) View.VISIBLE else View.GONE
            btnCancel.visibility = if (job.isActive) View.VISIBLE else View.GONE
            etReason.visibility = if (job.isActive) View.VISIBLE else View.GONE
        } else {
            // A student may cancel only their own job, and only while it is Waiting.
            val canCancel = job.status == JobStatus.WAITING && job.studentName == MockData.CURRENT_STUDENT
            btnCancel.visibility = if (canCancel) View.VISIBLE else View.GONE
        }

        btnStart.setOnClickListener {
            if (MockData.canStart(job)) {
                MockData.updateStatus(job.id, JobStatus.PROCESSING)
                Toast.makeText(this, "Job started", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Only the first waiting job can start, and only when nothing is processing",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnComplete.setOnClickListener {
            MockData.updateStatus(job.id, JobStatus.COMPLETED)
            Toast.makeText(this, "Job completed and removed from the queue", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnCancel.setOnClickListener {
            if (role == Extras.STAFF) {
                val reason = etReason.text.toString().trim()
                if (reason.length < 5) {
                    etReason.error = "Enter a reason of at least 5 characters"
                    return@setOnClickListener
                }
                MockData.updateStatus(job.id, JobStatus.CANCELLED, reason)
            } else {
                MockData.updateStatus(job.id, JobStatus.CANCELLED, "Cancelled by student")
            }
            Toast.makeText(this, "Job cancelled and removed from the queue", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
