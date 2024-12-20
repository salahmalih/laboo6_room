package ca.qc.cgodin.roomstudent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewStudentActivity : AppCompatActivity() {
    private lateinit var buttonSave: Button
    private lateinit var editFirstNameView: EditText
    private lateinit var editLastNameView: EditText
    private lateinit var editPhoneNumberView: EditText
    private lateinit var editEmailView: EditText

    private var isEditing = false
    private lateinit var existingStudent: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_student)

        // Initialize views after setContentView
        buttonSave = findViewById(R.id.buttonSave)
        editFirstNameView = findViewById(R.id.editFirstNameView)
        editLastNameView = findViewById(R.id.editLastNameView)
        editPhoneNumberView = findViewById(R.id.editPhoneNumberView)
        editEmailView = findViewById(R.id.editEmailView)

        // Check if editing an existing student
        if (intent.hasExtra(EXTRA_STUDENT)) {
            isEditing = true
            existingStudent = intent.getParcelableExtra(EXTRA_STUDENT) as? Student
                ?: throw IllegalArgumentException("Invalid Student data")
            fillExistingStudentData()
        }

        buttonSave.setOnClickListener {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(editFirstNameView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val updatedStudent = getUpdatedStudent()

                if (isEditing) {
                    // If editing, include the existing student ID
                    updatedStudent.id = existingStudent.id
                }

                replyIntent.putExtra(EXTRA_STUDENT, updatedStudent)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

        private fun getUpdatedStudent(): Student {
            val firstName = editFirstNameView.text.toString()
            val lastName = editLastNameView.text.toString()
            val phoneNumber = editPhoneNumberView.text.toString()
            val email = editEmailView.text.toString()

            return Student(0, firstName, lastName, phoneNumber, email)
        }


        private fun fillExistingStudentData() {
        // Pre-fill UI components with existing data
        existingStudent?.let {
            editFirstNameView.setText(it.firstName)
            editLastNameView.setText(it.lastName)
            editPhoneNumberView.setText(it.phoneNumber)
            editEmailView.setText(it.email)
        }
    }

    companion object {
        const val EXTRA_FIRSTNAME = "FIRSTNAME"
        const val EXTRA_LASTNAME = "LASTNAME"
        const val EXTRA_PHONENUMBER = "PHONENUMBER"
        const val EXTRA_EMAIL = "EMAIL"
        const val EXTRA_STUDENT = "STUDENT"
        const val EXTRA_ID = "ID"
    }
}
