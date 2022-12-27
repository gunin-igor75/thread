package ru.hogwarts.hogwarts.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.hogwarts.exception_handling.NoSuchFacultyException;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.repository.FacultyRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImp implements FacultyService {
    private final FacultyRepository facultyRepository;

    private static final Logger log = LoggerFactory.getLogger(FacultyServiceImp.class);

    public FacultyServiceImp(FacultyRepository facultyRepository) {
        log.debug("Initialization facultyRepository");
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        log.debug("create faculty {}", faculty);
        if (faculty.getId() != null) {
            log.error("faculty {} exist", faculty);
            throw new RuntimeException("Id must by empty");
        }
        log.info("faculty {} created", faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        log.debug("Search faculty by id {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            log.error("Faculty by id {} not found", id);
            throw new NoSuchFacultyException("There is no faculty with ID = " + id +
                    " in Database");
        }
        log.info("Faculty {} by id {} found",faculty, id);
        return faculty;
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        log.debug("edite faculty");
        log.debug("Search faculty by id {}", faculty.getId());
        Faculty newFaculty = findFaculty(faculty.getId());
        if (newFaculty == null) {
            log.error("Faculty not found {}", faculty);
            throw new RuntimeException("Id must by empty");
        }
        log.info("editing completed successfully");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        log.debug("delete faculty by id {}", id);
        log.debug("search faculty by id {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            log.error("faculty with id {} not found", id);
            throw new NoSuchFacultyException("There is no faculty with ID = " + id +
                    " in Database");
        }
        facultyRepository.deleteById(id);
        log.info("faculty {} delete successfully", faculty);
        return faculty;
    }

    @Override
    public List<Faculty> getFacultyByColorOrName(String name, String color) {
        log.debug("Search faculty by color {} or name {}", color, name);
        List<Faculty> facultyList = facultyRepository.
                getFacultyByColorIgnoreCaseOrNameIgnoreCase(name, color);
        if (facultyList == null) {
            log.warn("Collection empty faculty not fount by name {} or color {}", name, color);
            return Collections.emptyList();
        }
        log.info("providing list faculty by name {} or color {}", name, color);
        return facultyList;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        log.debug("Search faculty all");
        log.info("providing list faculty");
        return facultyRepository.findAll();
    }

    @Override
    public List<Student> findStudents(long id) {
        log.debug("Search faculty By ID {}", id);
        Faculty faculty = findFaculty(id);
        log.info("providing list student by id faculty {}", id);
        return faculty.getStudents();
    }

    @Override
    public String getNameFacultyMaxLength() {
        List<Faculty> listFaculty = facultyRepository.findAll();
        return listFaculty.stream()
                .map(Faculty::getName)
                .sorted((o1, o2) -> o2.length() - o1.length())
                .limit(1L).toList()
                .get(0);
    }
}
