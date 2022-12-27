package ru.hogwarts.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);
//        studentService.updateStudent(newStudent);
        return ResponseEntity.ok(newStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findStudent(id));
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

    @GetMapping(params = "age")
    public ResponseEntity<List<Student>> getAllStudentByAge(@RequestParam(value = "age", required = false)
                                                            Integer age) {
        return ResponseEntity.ok(studentService.getAllStudentEqualsAge(age));
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public ResponseEntity<List<Student>> getAllStudentBetweenAge(
            @RequestParam(value = "minAge", required = false) Integer minAge,
            @RequestParam(value = "maxAge", required = false) Integer maxAge) {
        return ResponseEntity.ok(studentService.findAllByAgeBetween(minAge, maxAge));
    }


    @GetMapping
    public ResponseEntity<List<Student>> getAllFaculty() {
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/count-student")
    public int getCountStudent() {
        return studentService.getCountStudent();
    }

    @GetMapping("/avg-age-student")
    public int getAvgAgeStudent() {
        return studentService.getAvgAgeStudent();
    }

    @GetMapping("/count-student-number/{number}")
    public ResponseEntity<List<Student>> getCountStudentNumber(@PathVariable int number) {
        return ResponseEntity.ok(studentService.getCountStudentNumber(number));
    }

    @GetMapping("/faculty/{id}")
    public ResponseEntity<Faculty> getFacultyStudent(@PathVariable long id) {
        return ResponseEntity.ok(studentService.findFaculty(id));
    }

    @GetMapping("/get-students-A/{letter}")
    public ResponseEntity<List<String>> getStudentsA(@PathVariable char letter) {
        return ResponseEntity.ok(studentService.getStudentA(letter));
    }
}
