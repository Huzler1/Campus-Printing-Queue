package com.example.printq

// Phase 1 has no Firebase yet, so this object plays the role of the database.
// It keeps the sample jobs in memory while the app is running.
object MockData {

    // The student we pretend is logged in.
    const val CURRENT_STUDENT = "Juan Dela Cruz"

    private var nextId = 9
    private var nextQueueNumber = 17

    private val jobs = mutableListOf(
        PrintJob(1, 9, "Juan Dela Cruz", "Syllabus Copy", 6, 1, "B/W", "A4", "", JobStatus.COMPLETED),
        PrintJob(2, 10, "Maria Santos", "Lab Manual", 12, 1, "B/W", "A4", "", JobStatus.COMPLETED),
        PrintJob(3, 11, "Pedro Reyes", "Enrollment Form", 2, 2, "B/W", "A4", "", JobStatus.CANCELLED, "Student left the line"),
        PrintJob(4, 12, "Juan Dela Cruz", "Thesis Chapter 3", 24, 2, "B/W", "A4", "Staple top-left", JobStatus.PROCESSING),
        PrintJob(5, 13, "Ana Lim", "Lab Report", 10, 1, "B/W", "A4", "", JobStatus.WAITING),
        PrintJob(6, 14, "Juan Dela Cruz", "Group Poster", 1, 1, "Color", "Legal", "Glossy if available", JobStatus.WAITING),
        PrintJob(7, 15, "Carlo Diaz", "Resume", 2, 3, "B/W", "Letter", "", JobStatus.WAITING),
        PrintJob(8, 16, "Ana Lim", "Reviewer Notes", 30, 1, "B/W", "A4", "", JobStatus.WAITING)
    )

    // Jobs still in the queue (Waiting or Processing), in queue-number order.
    fun activeJobs(): List<PrintJob> =
        jobs.filter { it.isActive }.sortedBy { it.queueNumber }

    // Jobs that left the queue (Completed or Cancelled), newest first.
    fun historyJobs(): List<PrintJob> =
        jobs.filter { !it.isActive }.sortedByDescending { it.queueNumber }

    // All jobs that belong to one student.
    fun jobsOf(student: String): List<PrintJob> =
        jobs.filter { it.studentName == student }

    fun activeCountOf(student: String): Int =
        jobs.count { it.studentName == student && it.isActive }

    // Position of a job in the active queue (1 = next or being printed).
    fun positionOf(job: PrintJob): Int =
        activeJobs().indexOfFirst { it.id == job.id } + 1

    // Creates a new Waiting job with the next queue number.
    fun addJob(
        student: String, documentName: String, pages: Int, copies: Int,
        colorMode: String, paperSize: String, notes: String
    ): PrintJob {
        val job = PrintJob(
            nextId, nextQueueNumber, student, documentName, pages, copies,
            colorMode, paperSize, notes, JobStatus.WAITING
        )
        nextId++
        nextQueueNumber++
        jobs.add(job)
        return job
    }

    // Replaces the job with a copy that has the new status.
    fun updateStatus(id: Int, newStatus: JobStatus, reason: String = "") {
        val index = jobs.indexOfFirst { it.id == id }
        if (index >= 0) {
            jobs[index] = jobs[index].copy(status = newStatus, cancelReason = reason)
        }
    }

    // Rule: only the first waiting job can start, and only if nothing is being processed.
    fun canStart(job: PrintJob): Boolean {
        val active = activeJobs()
        val nothingProcessing = active.none { it.status == JobStatus.PROCESSING }
        val firstWaiting = active.firstOrNull { it.status == JobStatus.WAITING }
        return job.status == JobStatus.WAITING && nothingProcessing && firstWaiting?.id == job.id
    }
}
