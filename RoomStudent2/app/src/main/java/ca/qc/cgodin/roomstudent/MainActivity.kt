package ca.qc.cgodin.roomstudent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), StudentListAdapter.OnUpdateClickListener,
    StudentListAdapter.OnDeleteClickListener {
    private lateinit var recyclerview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var searchView: SearchView
    private val newStudentActivityRequestCode = 1
    private val studentViewModel: StudentViewModel by lazy {
        ViewModelProvider(
            this,
            StudentViewModelFactory(application)
        ).get(StudentViewModel::class.java)
    }
    private lateinit var adapter: StudentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        searchView = findViewById(R.id.searchView)

        // Initialize adapter within onCreate
        adapter = StudentListAdapter(this)
        recyclerview.adapter = adapter

        // Set click listeners after initializing the adapter
        adapter.updateClickListener = this
        adapter.deleteClickListener = this

        studentViewModel.allStudents.observe(this, Observer { students ->
            students?.let { adapter.setStudents(it) }
        })

        fab.setOnClickListener {
            // Launch NewStudentActivity for adding new student
            val intent = Intent(this@MainActivity, NewStudentActivity::class.java)
            startActivityForResult(intent, newStudentActivityRequestCode)
        }

        // Initialize search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Call the searchStudents function on text change
                searchStudents(newText.orEmpty())
                return true
            }
        })
    }
    private fun update(student: Student) {
        studentViewModel.update(student)
    }

    private fun delete(student: Student) {
        studentViewModel.delete(student)
    }
    private fun searchStudents(query: String) {
        studentViewModel.getStudentsByName("%$query%").observe(this, Observer { students ->
            students?.let { adapter.setStudents(it) }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newStudentActivityRequestCode && resultCode == RESULT_OK) {
            intentData?.let { data ->
                if (data.hasExtra(NewStudentActivity.EXTRA_STUDENT)) {
                    // Adding or updating student
                    val student = data.getParcelableExtra<Student>(NewStudentActivity.EXTRA_STUDENT)
                    if (student != null) {
                        if (student.id == 0) {
                            // Adding a new student
                            studentViewModel.insert(student)
                        } else {
                            // Updating an existing student
                            studentViewModel.update(student)
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }




    override fun onUpdateClick(student: Student) {
        val intent = Intent(this@MainActivity, NewStudentActivity::class.java)
        intent.putExtra(NewStudentActivity.EXTRA_STUDENT, student)
        startActivityForResult(intent, newStudentActivityRequestCode)

    }

    override fun onDeleteClick(student: Student) {
        // Mettez en œuvre la logique de suppression ici
        // Appelez la fonction delete dans votre ViewModel
        delete(student)
    }
}


