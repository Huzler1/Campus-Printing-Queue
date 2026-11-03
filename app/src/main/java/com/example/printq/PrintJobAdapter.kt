package com.example.printq

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Color used for the status badge. Used by the list and by the details screen.
fun statusColor(status: JobStatus): Int {
    val hex = when (status) {
        JobStatus.WAITING -> "#F57C00"
        JobStatus.PROCESSING -> "#1565C0"
        JobStatus.COMPLETED -> "#2E7D32"
        JobStatus.CANCELLED -> "#757575"
    }
    return Color.parseColor(hex)
}

// The adapter connects a list of PrintJob objects to the RecyclerView rows (item_print_job.xml).
class PrintJobAdapter(
    private val jobs: List<PrintJob>,
    private val onItemClick: (PrintJob) -> Unit
) : RecyclerView.Adapter<PrintJobAdapter.JobViewHolder>() {

    // A ViewHolder keeps references to the views of one row.
    class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQueueNumber: TextView = view.findViewById(R.id.tvQueueNumber)
        val tvDocument: TextView = view.findViewById(R.id.tvDocument)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    // Called when the RecyclerView needs a new empty row.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_print_job, parent, false)
        return JobViewHolder(view)
    }

    // Called to fill a row with the data of the job at this position.
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.tvQueueNumber.text = "#%03d".format(job.queueNumber)
        holder.tvDocument.text = job.documentName
        holder.tvDetails.text =
            "${job.studentName} - ${job.pages} pages, ${job.copies} copies, ${job.colorMode}"
        holder.tvStatus.text = job.status.label
        holder.tvStatus.setBackgroundColor(statusColor(job.status))
        holder.itemView.setOnClickListener { onItemClick(job) }
    }

    override fun getItemCount(): Int = jobs.size
}
