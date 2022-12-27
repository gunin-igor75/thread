package ru.hogwarts.hogwarts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.service.FacultyService;

import java.util.List;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    final private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.createFaculty(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.findFaculty(id));
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.editFaculty(faculty));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.deleteFaculty(id));
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }

    @GetMapping(params = "colorName")
    public ResponseEntity<List<Faculty>> getFacultyByColorOrName(@RequestParam
                            (value = "colorName", required = false) String colorName) {
        return ResponseEntity.ok(facultyService.getFacultyByColorOrName(colorName, colorName));
    }

    @GetMapping("/student{id}")
    public ResponseEntity<List<Student>> getStudentFaculty(@PathVariable long id) {
        return ResponseEntity.ok(facultyService.findStudents(id));
    }

    @GetMapping("faculty-max-length")
    public String getNameFacultyMaxLength() {
        return  facultyService.getNameFacultyMaxLength();
    }
}
