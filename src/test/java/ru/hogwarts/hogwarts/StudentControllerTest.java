package ru.hogwarts.hogwarts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.hogwarts.model.Student;


import java.net.URI;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createStudentTest() {
        Student student = new Student("Igor", 25);
        System.out.println(student);
        ResponseEntity<Student> response =
                whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        System.out.println(response.getBody());
        thenStudentHasBeenCreated(response);
    }

    @Test
    void findStudentTest() {
        Student student = new Student("Igor", 25);
        ResponseEntity<Student> response =
                whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        thenStudentHasBeenCreated(response);
        Student newStudent = response.getBody();
        assert newStudent != null;
        Long id = newStudent.getId();
        thenStudentHasBeenFind(id, newStudent);
    }


    @Test
    void getStudentsByAgeTest() {
        Student student_1 = new Student("Oleg", 20);
        Student student_2 = new Student("Pety", 17);
        Student student_3 = new Student("Oly", 20);
        Student student_4 = new Student("Koly", 18);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_1);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_2);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_3);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_4);
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("age", "18");
        thenStudentHasBeenByCriteria(queryParams, student_4);
    }

    @Test
    void getStudentsBetweenAgeTest() {
        Student student_1 = new Student("Oleg", 20);
        Student student_2 = new Student("Pety", 17);
        Student student_3 = new Student("Oly", 20);
        Student student_4 = new Student("Koly", 18);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_1);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_2);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_3);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_4);
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("minAge", "19");
        queryParams.add("maxAge", "21");
        thenStudentHasBeenByCriteria(queryParams, student_1, student_3);
    }

    @Test
    void getAllStudentsTest() {
        Student student_1 = new Student("Oleg", 20);
        Student student_2 = new Student("Pety", 17);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_1);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_2);
        thenStudentHasBeenAll(student_1, student_2);
    }

    @Test
    void editStudentTest() {
        Student student = new Student("Igor", 25);
        ResponseEntity<Student> response =
                whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        Student oldStudent = response.getBody();
        String newName = "Oleg";
        int newAge = 30;
        assert oldStudent != null;
        whenUpdateStudent(oldStudent, newName, newAge);
        thenStudentHasBeenEdit(oldStudent, newName, newAge);

    }

    @Test
    void deleteStudentTest() {
        Student student = new Student("Amigo", 47);
        ResponseEntity<Student> response =
                whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        Student oldStudent = response.getBody();
        assert oldStudent != null;
        Long id = oldStudent.getId();
        whenStudentHasBeenDelete(id);
        thenStudentNotFound(id);
    }

    private void thenStudentNotFound(Long id) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(id).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void whenStudentHasBeenDelete(Long id) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(id).toUri();
        restTemplate.delete(uri);
    }

    private void thenStudentHasBeenEdit(Student oldStudent, String newName, int newAge) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(oldStudent.getId()).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);
        Student newStudent = response.getBody();
        assert newStudent != null;
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newStudent.getName()).isEqualTo(newName);
        assertThat(newStudent.getAge()).isEqualTo(newAge);
    }

    private void whenUpdateStudent(Student student, String newName, int newAge) {
        student.setAge(newAge);
        student.setName(newName);
        restTemplate.put(getUriBuilder().build().toUri(), student);
    }

    private void thenStudentHasBeenAll(Student...students) {
        URI uri = getUriBuilder().build().toUri();
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>(){
                });
        Collection<Student> actualResult = response.getBody();
        assert actualResult != null;
        resetId(actualResult);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResult).containsExactlyInAnyOrder(students);
    }


    private void thenStudentHasBeenByCriteria(MultiValueMap<String, String> queryParams, Student... students) {
        URI uri = getUriBuilder().queryParams(queryParams).build().toUri();
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> actualResult = response.getBody();
        resetId(actualResult);
        assertThat(actualResult).containsExactlyInAnyOrder(students);
    }

    private void resetId(Collection<Student> actualStudents) {
        actualStudents.forEach(e -> e.setId(null));
    }

    private void thenStudentHasBeenFind(Long id, Student newStudent) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(id).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);
        assertThat(response.getBody()).isEqualTo(newStudent);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private void thenStudentHasBeenCreated(ResponseEntity<Student> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    private ResponseEntity<Student> whenSendingCreateStudentRequest(URI uri, Student student) {
        return restTemplate.postForEntity(uri, student, Student.class);
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/student");
    }
}
