package nl.devcraft.constraints;

import io.quarkus.test.junit.QuarkusTest;
import nl.devcraft.domain.Lesson;
import nl.devcraft.domain.Room;
import nl.devcraft.domain.TimeTable;
import nl.devcraft.domain.Timeslot;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalTime;

@QuarkusTest
class TimeTableConstraintProviderTest {

    private static final Room ROOM = new Room("Room1");
    private static final Timeslot TIMESLOT1 = new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.NOON);
    private static final Timeslot TIMESLOT2 = new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9,0), LocalTime.NOON);

    @Inject
    ConstraintVerifier<TimeTableConstraintProvider, TimeTable> constraintVerifier;

    @Test
    void roomConflict() {
        Lesson firstLesson = new Lesson(1L, "Subject1", "Teacher1", "Group1",TIMESLOT1, ROOM);
        Lesson conflictingLesson = new Lesson(2L, "Subject2", "Teacher2", "Group2",TIMESLOT1,ROOM);
        Lesson nonConflictingLesson = new Lesson(3L, "Subject3", "Teacher3", "Group3",TIMESLOT2,ROOM);

        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstLesson, conflictingLesson, nonConflictingLesson)
                .penalizesBy(1);
    }

}
