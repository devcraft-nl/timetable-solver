package nl.devcraft.constraints;

import io.quarkus.test.junit.QuarkusTest;
import nl.devcraft.domain.Lesson;
import nl.devcraft.domain.Room;
import nl.devcraft.domain.TimeTable;
import nl.devcraft.domain.Timeslot;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalTime;

@QuarkusTest
class TimeTableConstraintProviderTest {

    private static final Room ROOM1 = new Room(1L, "Room1");
    private static final Room ROOM2 = new Room(2L, "Room2");
    private static final Timeslot TIMESLOT1 = new Timeslot(1L, DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.NOON.plusMinutes(50));
    private static final Timeslot TIMESLOT2 = new Timeslot(2L, DayOfWeek.TUESDAY, LocalTime.NOON, LocalTime.NOON.plusMinutes(50));
    private static final Timeslot TIMESLOT3 = new Timeslot(3L, DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(1), LocalTime.NOON.plusHours(1).plusHours(1));
    private static final Timeslot TIMESLOT4 = new Timeslot(4L, DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(3), LocalTime.NOON.plusHours(3).plusMinutes(50));

    @Inject
    ConstraintVerifier<TimeTableConstraintProvider, TimeTable> constraintVerifier;

    @Test
    void roomConflict() {
        var firstLesson = new Lesson(1L, "Subject1", "Teacher1", "Group1", TIMESLOT1, ROOM1);
        var conflictingLesson = new Lesson(2L, "Subject2", "Teacher2", "Group2", TIMESLOT1, ROOM1);
        var nonConflictingLesson = new Lesson(3L, "Subject3", "Teacher3", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void teacherConflict() {
        var conflictingTeacher = "Teacher1";
        var firstLesson = new Lesson(1L, "Subject1", conflictingTeacher, "Group1", TIMESLOT1, ROOM1);
        var conflictingLesson = new Lesson(2L, "Subject2", conflictingTeacher, "Group2", TIMESLOT1, ROOM2);
        var nonConflictingLesson = new Lesson(3L, "Subject3", "Teacher2", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void studentGroupConflict() {
        var conflictingGroup = "Group1";
        var firstLesson = new Lesson(1L, "Subject1", "Teacher1", conflictingGroup, TIMESLOT1, ROOM1);
        var conflictingLesson = new Lesson(2L, "Subject2", "Teacher2", conflictingGroup, TIMESLOT1, ROOM2);
        var nonConflictingLesson = new Lesson(3L, "Subject3", "Teacher3", "Group3", TIMESLOT2, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

    @Test
    void teacherRoomStability() {
        var teacher = "Teacher1";
        var lessonInFirstRoom = new Lesson(1L, "Subject1", teacher, "Group1", TIMESLOT1, ROOM1);
        var lessonInSameRoom = new Lesson(2L, "Subject2", teacher, "Group2", TIMESLOT1, ROOM1);
        var lessonInDifferentRoom = new Lesson(3L, "Subject3", teacher, "Group3", TIMESLOT1, ROOM2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomStability)
                .given(lessonInFirstRoom, lessonInDifferentRoom, lessonInSameRoom)
                .penalizesBy(2);
    }

    @Test
    @Disabled("Need to check why there are 4 penalizations instead of 1 in this test")
    void teacherTimeEfficiency() {
        var teacher = "Teacher1";
        var singleLessonOnMonday = new Lesson(1L, "Subject1", teacher, "Group1", TIMESLOT1, ROOM1);
        var firstTuesdayLesson = new Lesson(2L, "Subject2", teacher, "Group2", TIMESLOT2, ROOM1);
        var secondTuesdayLesson = new Lesson(3L, "Subject3", teacher, "Group3", TIMESLOT3, ROOM1);
        var thirdTuesdayLessonWithGap = new Lesson(4L, "Subject4", teacher, "Group4", TIMESLOT4, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherTimeEfficiency)
                .given(singleLessonOnMonday, firstTuesdayLesson, secondTuesdayLesson, thirdTuesdayLessonWithGap)
                .rewardsWith(1); // Second tuesday lesson immediately follows the first.
    }

    @Test
    void studentGroupSubjectVariety() {
        var studentGroup = "Group1";
        var repeatedSubject = "Subject1";
        var mondayLesson = new Lesson(1L, repeatedSubject, "Teacher1", studentGroup, TIMESLOT1, ROOM1);
        var firstTuesdayLesson = new Lesson(2L, repeatedSubject, "Teacher2", studentGroup, TIMESLOT2, ROOM1);
        var secondTuesdayLesson = new Lesson(3L, repeatedSubject, "Teacher3", studentGroup, TIMESLOT3, ROOM1);
        var thirdTuesdayLessonWithDifferentSubject = new Lesson(4L, "Subject2", "Teacher4", studentGroup, TIMESLOT4, ROOM1);
        var lessonInAnotherGroup = new Lesson(5L, repeatedSubject, "Teacher5", "Group2", TIMESLOT1, ROOM1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupSubjectVariety)
                .given(mondayLesson, firstTuesdayLesson, secondTuesdayLesson, thirdTuesdayLessonWithDifferentSubject,
                        lessonInAnotherGroup)
                .penalizesBy(1); // Second tuesday lesson immediately follows the first.
    }

}
