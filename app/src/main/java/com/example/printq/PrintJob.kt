package com.example.printq

import java.io.Serializable

// The four statuses a print job can have.
// Only WAITING and PROCESSING jobs belong in the active queue.
enum class JobStatus(val label: String) {
    WAITING("Waiting"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

// One print request. "val" fields never change; to change a status we make a copy (see MockData).
// Serializable lets us pass the whole object from one screen to another inside an Intent.
data class PrintJob(
    val id: Int,
    val queueNumber: Int,
    val studentName: String,
    val documentName: String,
    val pages: Int,
    val copies: Int,
    val colorMode: String,   // "B/W" or "Color"
    val paperSize: String,   // "A4", "Letter" or "Legal"
    val notes: String,
    val status: JobStatus,
    val cancelReason: String = ""
) : Serializable {

    // True while the job is still in the active queue (business rule: completed and cancelled jobs leave it).
    val isActive: Boolean
        get() = status == JobStatus.WAITING || status == JobStatus.PROCESSING
}

// Names of the extras we put in an Intent, so we never mistype them.
object Extras {
    const val ROLE = "ROLE"
    const val JOB = "JOB"
    const val STUDENT = "STUDENT"
    const val STAFF = "STAFF"
}
