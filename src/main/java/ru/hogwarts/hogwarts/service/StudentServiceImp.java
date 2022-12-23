package ru.hogwarts.hogwarts.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.hogwarts.exception_handling.NoSuchStudentException;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.repository.FacultyRepository;
import ru.hogwarts.hogwarts.repository.StudentRepository;

import java.util.List;
import java.util.Random;

@Service
public class StudentServiceImp implements StudentService{
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    private final List<Faculty> faculties;

    public StudentServiceImp(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        faculties = facultyRepository.findAll();
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public void updateStudent(Student student) {
        int facultyId = new Random().nextInt(faculties.size() -1) + 1;
        studentRepository.updateStudent(facultyId, student.getId());
    }

    @Override
    public Student findStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            throw new NoSuchStudentException("There is no student with ID = " + id +
                    " in Database");
        }
        return student;
    }

    @Override
    public Student editStudent(Student student) {
        Student newStudent = findStudent(student.getId());
        if (newStudent == null) {
            throw new RuntimeException("Id must by empty");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            throw new NoSuchStudentException("There is no student with ID = " + id +
                    " in Database");
        }
        studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<Student> getAllStudentEqualsAge(int age) {
        return studentRepository.findFacultyAge(age);
    }

    public List<Student> getAgeBetween(Integer minAge, Integer maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty(long id) {
        Student student = findStudent(id);
        return student.getFaculty();
    }

    public int getCountStudent() {
        return studentRepository.getCountStudents();
    }

    public int getAvgAgeStudent() {
        return studentRepository.getAvgAgeStudent();
    }

    public List<Student> getCountStudentNumber(int number) {
        if (number > studentRepository.getCountStudents()) {
            throw new RuntimeException("Invalid number entered");
        }
        return studentRepository.getCountStudentNumber(number);
    }

    @Override
    public List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }
}
