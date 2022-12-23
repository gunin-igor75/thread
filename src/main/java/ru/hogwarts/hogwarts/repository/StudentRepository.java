package ru.hogwarts.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.hogwarts.model.Student;

import javax.transaction.Transactional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query("select pos from student pos  where pos.age = :age")
    List<Student> findFacultyAge(Integer age);

    List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "select count(*)  from student", nativeQuery = true)
    int getCountStudents();

    @Query(value = "select round(avg(age)) from  student", nativeQuery = true)
    int getAvgAgeStudent();


    @Query(value = "select * from student order by id desc limit ?1 ", nativeQuery = true)
    List<Student> getCountStudentNumber(int num);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update student set faculty_id = ?1 where id = ?2", nativeQuery = true)
    void updateStudent(int faculty_id, long studentId);
}
