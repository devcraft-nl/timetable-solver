package nl.devcraft.domain;

import lombok.*;
import nl.devcraft.domain.Lesson;
import nl.devcraft.domain.Room;
import nl.devcraft.domain.Timeslot;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class TimeTable {

    @ValueRangeProvider(id = "timeslotRange")
    @ProblemFactCollectionProperty
    @NonNull
    private List<Timeslot> timeslotList;
    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    @NonNull
    private List<Room> roomList;
    @PlanningEntityCollectionProperty
    @NonNull
    private List<Lesson> lessonList;

    @PlanningScore
    private HardSoftScore score;

}
