package com.robot.helper;

import com.robot.model.Direction;
import com.robot.repository.GridRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceHelperTest {


    @Test
    void executeCommandWAITCommand() {
        var grid = new GridRepository();
        var command = "WAIT";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),0);
        assertEquals(grid.getPosition().y(),0);
        assertEquals(grid.getPosition().direction(), Direction.EAST);
    }

    @Test
    void executeCommandTURNAROUNDCommand() {
        var grid = new GridRepository();
        var command = "TURNAROUND";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),0);
        assertEquals(grid.getPosition().y(),0);
        assertEquals(grid.getPosition().direction(), Direction.WEST);
    }

    @Test
    void executeCommandRIGHTNDCommand() {
        var grid = new GridRepository();
        var command = "RIGHT";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),0);
        assertEquals(grid.getPosition().y(),0);
        assertEquals(grid.getPosition().direction(), Direction.SOUTH);
    }

    @Test
    void executeCommandFORWARD_1Command() {
        var grid = new GridRepository();
        var command = "FORWARD 1";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),1);
        assertEquals(grid.getPosition().y(),0);
        assertEquals(grid.getPosition().direction(), Direction.EAST);
    }

    @Test
    void executeCommandFORWARD_GRID_LIMIT_should_not_go_outside() {
        var grid = new GridRepository();
        var command = "FORWARD 6";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),4);
        assertEquals(grid.getPosition().y(),0);
        assertEquals(grid.getPosition().direction(), Direction.EAST);
    }

    @Test
    void positionExecuteCommand_COMMAND() {
        var grid = new GridRepository();
        var command = "POSITION 1 3 NORTH";
        ServiceHelper.executeCommand(command,grid);
        assertEquals(grid.getPosition().x(),1);
        assertEquals(grid.getPosition().y(),3);
        assertEquals(grid.getPosition().direction(), Direction.NORTH);
    }


    @Test
    void isValidWAITCommand() {
        var command = "WAIT";
        assertTrue(ServiceHelper.isValidCommand(command));
    }

    @Test
    void isValidRIGHTCommand() {
        var command = "RIGHT";
        assertTrue(ServiceHelper.isValidCommand(command));
    }
    @Test
    void isValidFORWARDCommand() {
        var command = "FORWARD 1";
        assertTrue(ServiceHelper.isValidCommand(command));
    }
    @Test
    void nonValidFORWARDCommand() {
        var command = "BADCOMMAND 1";
        assertFalse(ServiceHelper.isValidCommand(command));
    }
}