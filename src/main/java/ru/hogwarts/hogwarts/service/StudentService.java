package ru.hogwarts.hogwarts.service;

import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student findStudent(Long id);

    Student editStudent(Student student);

    Student deleteStudent(Long id);

    List<Student> getAllStudent();

    List<Student> getAllStudentEqualsAge(int age);

    int getCountStudent();

    int getAvgAgeStudent();

    List<Student> getCountStudentNumber(int number);

    List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge);

    Faculty findFaculty(long id);

    void updateStudent(Student student);

    List<String> getStudentA(char letter);
}
