package nl.devcraft.lambda;

import io.restassured.http.ContentType;
import nl.devcraft.domain.Lesson;
import nl.devcraft.domain.Room;
import nl.devcraft.domain.TimeTable;
import nl.devcraft.domain.Timeslot;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Timeout;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class LambdaHandlerTest {

    @Test
    @Timeout(600_000)
    public void testSimpleLambdaSuccess() throws Exception {
        // First invoke the lambda and receive a 200 response payload
        var response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(generateProblem())
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();
        // Then assert the content of the lessons
        var lessonList = response.jsonPath().getList("lessonList");
        assertAll(
                () -> assertEquals(lessonList.size(), 10),
                () -> assertEquals(lessonList.get(0).toString(), "{id=101, subject=Math, teacher=B. May, studentGroup=9th grade, timeslot={dayOfWeek=MONDAY, startTime=08:30:00, endTime=09:30:00}, room={name=Room A}}"),
                () -> assertEquals(lessonList.get(1).toString(), "{id=102, subject=Physics, teacher=M. Curie, studentGroup=9th grade, timeslot={dayOfWeek=MONDAY, startTime=09:30:00, endTime=10:30:00}, room={name=Room A}}"),
                () -> assertEquals(lessonList.get(2).toString(), "{id=103, subject=Geography, teacher=M. Polo, studentGroup=9th grade, timeslot={dayOfWeek=MONDAY, startTime=10:30:00, endTime=11:30:00}, room={name=Room A}}")
        );
    }

    private TimeTable generateProblem() {
        List<Timeslot> timeslotList = new ArrayList<>();
        timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslotList.add(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));

        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room("Room A"));
        roomList.add(new Room("Room B"));
        roomList.add(new Room("Room C"));

        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(new Lesson(101L, "Math", "B. May", "9th grade"));
        lessonList.add(new Lesson(102L, "Physics", "M. Curie", "9th grade"));
        lessonList.add(new Lesson(103L, "Geography", "M. Polo", "9th grade"));
        lessonList.add(new Lesson(104L, "English", "I. Jones", "9th grade"));
        lessonList.add(new Lesson(105L, "Spanish", "P. Cruz", "9th grade"));

        lessonList.add(new Lesson(201L, "Math", "B. May", "10th grade"));
        lessonList.add(new Lesson(202L, "Chemistry", "M. Curie", "10th grade"));
        lessonList.add(new Lesson(203L, "History", "I. Jones", "10th grade"));
        lessonList.add(new Lesson(204L, "English", "P. Cruz", "10th grade"));
        lessonList.add(new Lesson(205L, "French", "M. Curie", "10th grade"));
        return new TimeTable(timeslotList, roomList, lessonList);
    }

}
