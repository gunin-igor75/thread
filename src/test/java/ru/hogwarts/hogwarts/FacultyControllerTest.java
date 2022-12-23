package ru.hogwarts.hogwarts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.hogwarts.controller.FacultyController;
import ru.hogwarts.hogwarts.model.Faculty;
import ru.hogwarts.hogwarts.repository.FacultyRepository;
import ru.hogwarts.hogwarts.service.FacultyService;
import ru.hogwarts.hogwarts.service.FacultyServiceImp;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImp facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findFacultyTest() throws Exception {
        long id = 1L;
        String name = "Hufflepuff";
        String color = "yellow";
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);
        facultyObject.put("id", id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void getAllFacultyByColorOrNameTest() throws Exception {
        long id_1 = 1L;
        String name_1 = "Hufflepuff";
        String color_1 = "yellow";
        Faculty faculty_1 = new Faculty(name_1, color_1);
        faculty_1.setId(id_1);
        long id_2 = 2L;
        String name_2 = "Gryffindor";
        String color_2 = "red";
        Faculty faculty_2 = new Faculty(name_2, color_2);
        faculty_2.setId(id_2);
        when(facultyRepository.getFacultyByColorIgnoreCaseOrNameIgnoreCase(name_1, name_1)).thenReturn(List.of(faculty_1));
        when(facultyRepository.getFacultyByColorIgnoreCaseOrNameIgnoreCase(color_2, color_2)).thenReturn(List.of(faculty_2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .queryParam("colorName", name_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty_1))));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .queryParam("colorName", color_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty_2))));
    }

    @Test
    public void getAllFacultyTest() throws Exception {
        long id_1 = 1L;
        String name_1 = "Hufflepuff";
        String color_1 = "yellow";
        Faculty faculty_1 = new Faculty(name_1, color_1);
        faculty_1.setId(id_1);
        long id_2 = 2L;
        String name_2 = "Gryffindor";
        String color_2 = "red";
        Faculty faculty_2 = new Faculty(name_2, color_2);
        faculty_2.setId(id_2);
        when(facultyRepository.findAll()).thenReturn(List.of(faculty_1, faculty_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty_1, faculty_2))));
    }

    @Test
    public void createFacultyTest() throws Exception {
        long id = 1L;
        String name = "Hufflepuff";
        String color = "yellow";
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);
        JSONObject facultyObj = new JSONObject();
        facultyObj.put("name", name);
        facultyObj.put("color", color);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void editFacultyTest() throws Exception {
        long id = 1L;
        String name = "Hufflepuff";
        String color = "yellow";
        Faculty oldFaculty = new Faculty(name, color);
        oldFaculty.setId(id);
        String newName = "Ravenclaw";
        String newColor = "blue";
        JSONObject facultyObj = new JSONObject();
        facultyObj.put("id", id);
        facultyObj.put("name", newName);
        facultyObj.put("color", newColor);
        Faculty newFaculty = new Faculty(newName, newColor);
        newFaculty.setId(id);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(newFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        long id = 1L;
        String name = "Hufflepuff";
        String color = "yellow";
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        verify(facultyRepository, atLeastOnce()).deleteById(id);
    }
}
