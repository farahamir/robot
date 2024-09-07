package com.robot.repository;

import com.robot.model.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import com.robot.model.Position;

@Setter
@Getter
@Repository
@NoArgsConstructor
@AllArgsConstructor
public class GridRepository {
    //initializing the robot grid position
    private Position position = new Position(0,0, Direction.EAST);

    public GridRepository(GridRepository gridRepository) {
        this.position = gridRepository.getPosition();
    }
}
