package ca.qc.cgodin.roomstudent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Query("SELECT * from student_table ORDER BY firstName ASC")
    fun getStudents(): LiveData<List<Student>>
    @Query("SELECT * FROM student_table WHERE id=(:id)")
    fun getStudent(id: Int): LiveData<Student?>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(student: Student)
    @Update
    suspend fun update(student: Student)
    @Query("DELETE FROM student_table")
    fun deleteAll()
    @Delete
    suspend fun delete(student: Student)
    @Query("SELECT * FROM student_table WHERE firstName LIKE (:name) OR lastName LIKE (:name) ORDER BY firstName ASC")
    fun getStudentsByName(name: String): LiveData<List<Student>>


}