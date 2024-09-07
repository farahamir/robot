package com.robot.service;

import com.robot.model.Direction;
import com.robot.repository.GridRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {

    @Test
    void when_wait_command_then_nothing_changed() {
        GridRepository gridRepository = new GridRepository();
        PositionServiceImpl positionService = new PositionServiceImpl(gridRepository);
        String script = "WAIT";
        var gridResult = positionService.executeScript(script);
        assertEquals(0,gridResult.getPosition().x());
        assertEquals(0,gridResult.getPosition().y());
        assertEquals(Direction.EAST,gridResult.getPosition().direction());
    }
    @Test
    void when_executeScript_command_then_grid_changed() {
        GridRepository gridRepository = new GridRepository();
        PositionServiceImpl positionService = new PositionServiceImpl(gridRepository);
        String script = "POSITION 1 3 SOUTH";
        var gridResult = positionService.executeScript(script);
        assertEquals(1,gridResult.getPosition().x());
        assertEquals(3,gridResult.getPosition().y());
        assertEquals(Direction.SOUTH,gridResult.getPosition().direction());
    }

    @Test
    void when_multiple_command_then_grid_changed() {
        GridRepository gridRepository = new GridRepository();
        PositionServiceImpl positionService = new PositionServiceImpl(gridRepository);
        String script = "POSITION 1 3 SOUTH\nFORWARD 1\nRIGHT\nFORWARD 2";
        var gridResult = positionService.executeScript(script);
        assertEquals(0,gridResult.getPosition().x());
        assertEquals(4,gridResult.getPosition().y());
        assertEquals(Direction.WEST,gridResult.getPosition().direction());
    }

    @Test
    void when_multiple_command_with_bad_command_then_grid_not_changed() {
        GridRepository gridRepository = new GridRepository();
        PositionServiceImpl positionService = new PositionServiceImpl(gridRepository);
        String script = "POSITION 1 3 SOUTH\nFORWARD 1\nBADCOMMAND\nFORWARD 2";
        positionService.executeScript(script);
        assertEquals(0, gridRepository.getPosition().x());
        assertEquals(0, gridRepository.getPosition().y());
        assertEquals(Direction.EAST, gridRepository.getPosition().direction());
    }
}