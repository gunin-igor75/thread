package ru.hogwarts.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.hogwarts.exception_handling.NoSuchStudentException;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.repository.FacultyRepository;
import ru.hogwarts.hogwarts.repository.StudentRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentServiceImp implements StudentService{
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    private final List<Faculty> faculties;

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImp.class);

    public StudentServiceImp(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        log.debug("Initialization studentRepository facultyRepository and create list faculty");
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        faculties = facultyRepository.findAll();
    }

    @Override
    public Student createStudent(Student student) {
        log.debug("Save student");
        Student newStudent = studentRepository.save(student);
        log.info("Save student end successfully");
        return newStudent;
    }

    public void updateStudent(Student student) {
        log.debug("Update column table student by id faculty");
        int facultyId = new Random().nextInt(faculties.size() -1) + 1;
        studentRepository.updateStudent(facultyId, student.getId());
        log.info("Update colum table student end successfully");
    }

    @Override
    public List<String> getStudentA(char letter) {
        List<Student> listStudent = studentRepository.findAll();
        return listStudent.stream()
                .filter(st -> st.getName().toLowerCase().charAt(0) == Character.toLowerCase(letter))
                .map(st ->st.getName().toUpperCase())
                .map(st -> (st.charAt(0) + st.substring(1).toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Student findStudent(Long id) {
        log.debug("Search student by id {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            log.error("Student by id {} not found", id);
            throw new NoSuchStudentException("There is no student with ID = " + id +
                    " in Database");
        }
        log.info("Student by id {} found", id);
        return student;
    }

    @Override
    public Student editStudent(Student student) {
        log.debug("Edite student");
        Student newStudent = findStudent(student.getId());
        if (newStudent == null) {
            log.error("Student ot found") ;
            throw new RuntimeException("Id must by empty");
        }
        log.info("Student edite successfully");
        return studentRepository.save(student);
    }

    @Override
    public Student deleteStudent(Long id) {
        log.debug("Delete student by id {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            log.error("Student by id {} not found", id);
            throw new NoSuchStudentException("There is no student with ID = " + id +
                    " in Database");
        }
        log.info("Student delete successfully");
        studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> getAllStudent() {
        log.debug("Search student all");
        log.info("providing list student");
        return studentRepository.findAll();
    }

    public List<Student> getAllStudentEqualsAge(int age) {
        log.debug("Search students by age {}", age);
        log.info("providing list student by age {}", age);
        return studentRepository.findFacultyAge(age);
    }
    public Faculty findFaculty(long id) {
        log.debug("Search faculty by id {} student", id);
        Student student = findStudent(id);
        log.info("Providing faculty by id {} student", id);
        return student.getFaculty();
    }

    public int getCountStudent() {
        log.debug("Payment count students");
        int count = studentRepository.getCountStudents();
        log.info("Count students {}", count);
        return count;
    }

    public int getAvgAgeStudent() {
        log.debug("Payment avg age students");
        List<Student> listStudents = studentRepository.findAll();
        int avg = (int)listStudents.stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
        log.info("avg age students {}", avg);
        return avg;
    }

    public List<Student> getCountStudentNumber(int number) {
        log.debug("Formation list student {}", number);
        if (number > studentRepository.getCountStudents()) {
            log.error("No valid data {}", number);
            throw new RuntimeException("Invalid number entered");
        }
        log.info("Providing lis students {}", number);
        return studentRepository.getCountStudentNumber(number);
    }

    @Override
    public List<Student> findAllByAgeBetween(Integer minAge, Integer maxAge) {
        log.debug("Search students in between {} and {}", minAge, maxAge);
        log.info("Found students in between {} and {}", minAge, maxAge);
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }
}
