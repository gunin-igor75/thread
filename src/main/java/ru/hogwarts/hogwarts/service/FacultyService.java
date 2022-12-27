package ru.hogwarts.hogwarts.service;


import liquibase.pro.packaged.F;
import org.springframework.stereotype.Service;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;

import java.util.Collection;
import java.util.List;


public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(Long id);

    List<Faculty> getFacultyByColorOrName(String name, String color);

    List<Faculty> getAllFaculty();

    List<Student> findStudents(long id);

    String getNameFacultyMaxLength();
}
