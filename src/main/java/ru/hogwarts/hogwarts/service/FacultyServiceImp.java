package ru.hogwarts.hogwarts.service;


import org.springframework.stereotype.Service;
import ru.hogwarts.hogwarts.exception_handling.NoSuchFacultyException;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.repository.FacultyRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FacultyServiceImp implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImp(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        if (faculty.getId() != null) {
            throw new RuntimeException("Id must by empty");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            throw new NoSuchFacultyException("There is no faculty with ID = " + id +
                    " in Database");
        }
        return faculty;
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        Faculty newFaculty = findFaculty(faculty.getId());
        if (newFaculty == null) {
            throw new RuntimeException("Id must by empty");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            throw new NoSuchFacultyException("There is no faculty with ID = " + id +
                    " in Database");
        }
        facultyRepository.deleteById(id);
        return faculty;
    }

    @Override
    public List<Faculty> getFacultyByColorOrName(String name, String color) {
        List<Faculty> facultyList = facultyRepository.
                getFacultyByColorIgnoreCaseOrNameIgnoreCase(name, color);
        if (facultyList == null) {
            return Collections.emptyList();
        }
        return facultyList;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Student> findStudents(long id) {
        Faculty faculty = findFaculty(id);
        return faculty.getStudents();
    }
}
