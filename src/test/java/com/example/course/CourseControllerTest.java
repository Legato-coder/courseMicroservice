package com.example.course;

import com.example.course.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerTest {

    private static final String BASE_URL = "http://localhost:8081/api/courses";

    @BeforeEach
    public void setup() {
        // Optional: You can set up any prerequisites here
    }

    @Test
    public void testGetAllCourses() {
        given()
            .when()
            .get(BASE_URL)
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0))); // Adjust as per your expected size
    }

    @Test
    public void testAddCourse() {
        Course newCourse = new Course();
        newCourse.setCourseName("Biology 101");
        newCourse.setCourseCode("BIO101");

        given()
            .contentType("application/json")
            .body(newCourse)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("courseName", equalTo("Biology 101"));
    }

    @Test
    public void testDeleteCourse() {
        // First, add a course to delete
        Course courseToDelete = new Course();
        courseToDelete.setCourseName("Chemistry 101");
        courseToDelete.setCourseCode("CHEM101");

        ResponseEntity<Course> response = 
            new TestRestTemplate().postForEntity(BASE_URL, courseToDelete, Course.class);

        Long courseId = response.getBody().getId();

        // Now, delete the course
        given()
            .when()
            .delete(BASE_URL + "/" + courseId)
            .then()
            .statusCode(204); // 204 No Content
    }
}
