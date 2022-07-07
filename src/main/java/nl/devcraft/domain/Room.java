package nl.devcraft.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Room {
    @PlanningId
    private Long id;
    private String name;
}
