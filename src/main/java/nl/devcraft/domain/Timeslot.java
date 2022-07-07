package nl.devcraft.domain;

import lombok.*;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Timeslot {
    @PlanningId
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
