package ru.hogwarts.hogwarts.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.hogwarts.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> getFacultyByColorIgnoreCaseOrNameIgnoreCase(String name, String color);
}
