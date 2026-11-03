package com.example.printq

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Screen 3. A student fills in a form to add a new print job to the local list.
class SubmitJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_job)

        val etDocName = findViewById<EditText>(R.id.etDocName)
        val etPages = findViewById<EditText>(R.id.etPages)
        val etCopies = findViewById<EditText>(R.id.etCopies)
        val rbColor = findViewById<RadioButton>(R.id.rbColor)
        val spPaper = findViewById<Spinner>(R.id.spPaper)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitJob)

        // Fill the drop-down with the paper sizes.
        val paperSizes = listOf("A4", "Letter", "Legal")
        spPaper.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paperSizes)

        btnSubmit.setOnClickListener {
            val name = etDocName.text.toString().trim()
            val pages = etPages.text.toString().toIntOrNull()
            val copies = etCopies.text.toString().toIntOrNull()

            // Validation: stop at the first problem and show a message on that field.
            if (name.isEmpty()) {
                etDocName.error = "Document name is required"
                return@setOnClickListener
            }
            if (pages == null || pages !in 1..200) {
                etPages.error = "Pages must be a number from 1 to 200"
                return@setOnClickListener
            }
            if (copies == null || copies !in 1..20) {
                etCopies.error = "Copies must be a number from 1 to 20"
                return@setOnClickListener
            }
            // Business rule: a student may have at most 3 active jobs.
            if (MockData.activeCountOf(MockData.CURRENT_STUDENT) >= 3) {
                Toast.makeText(this, "You already have 3 active jobs", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val colorMode = if (rbColor.isChecked) "Color" else "B/W"
            val job = MockData.addJob(
                MockData.CURRENT_STUDENT, name, pages, copies,
                colorMode, spPaper.selectedItem.toString(), etNotes.text.toString().trim()
            )
            Toast.makeText(
                this, "Submitted. Your queue number is #%03d".format(job.queueNumber), Toast.LENGTH_LONG
            ).show()
            finish() // close this screen and go back to the queue
        }
    }
}
