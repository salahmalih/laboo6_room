package ca.qc.cgodin.roomstudent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentListAdapter constructor(
    context: Context
) : RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var students = emptyList<Student>() // Cached copy of students

    var updateClickListener: OnUpdateClickListener? = null
    var deleteClickListener: OnDeleteClickListener? = null

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameItemView: TextView = itemView.findViewById(R.id.fullNameView)
        val phoneNumberItemView: TextView = itemView.findViewById(R.id.phoneNumberView)
        val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        init {
            btnUpdate.setOnClickListener {
                updateClickListener?.onUpdateClick(students[adapterPosition])
            }

            btnDelete.setOnClickListener {
                deleteClickListener?.onDeleteClick(students[adapterPosition])
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            StudentViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)

        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = students[position]
        holder.fullNameItemView.text = "${current.firstName}${current.lastName}"
        holder.phoneNumberItemView.text = "${current.phoneNumber}"
        holder.btnUpdate.setOnClickListener {
            updateClickListener?.onUpdateClick(students[position])
        }

        holder.btnDelete.setOnClickListener {
            deleteClickListener?.onDeleteClick(students[position])
        }

    }

    fun setStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
    }

    override fun getItemCount() = students.size


    interface OnUpdateClickListener {
        fun onUpdateClick(student: Student)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(student: Student)
    }

}
