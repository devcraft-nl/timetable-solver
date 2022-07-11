package nl.devcraft.domain;

import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Lesson {

    @PlanningId
    @NonNull
    private Long id;

    @NonNull
    private String subject;
    @NonNull
    private String teacher;
    @NonNull
    private String studentGroup;

    @Setter
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;

    @Setter
    @PlanningVariable(valueRangeProviderRefs = "roomRange")
    private Room room;

}
