package com.robot.helper;

import com.robot.model.Command;
import com.robot.model.Direction;
import com.robot.model.Position;
import com.robot.repository.GridRepository;
import org.apache.commons.lang3.EnumUtils;

import java.util.EnumSet;
import java.util.regex.Pattern;

import static com.robot.model.Command.*;

public class ServiceHelper {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Execute single command and apply to the grid
     *
     * @param commandLine        String of single command
     * @param tempGridRepository A temporary grid of the robot
     */
    public static void executeCommand(String commandLine, GridRepository tempGridRepository) {
        var commandParts = commandLine.split(" ");
        Command command = EnumUtils.getEnum(Command.class, commandParts[0]);
        handleCommand(tempGridRepository, command, commandParts);
    }


    /**
     * Handle the command according to its type
     *
     * @param tempGridRepository A temporary grid of the robot
     * @param command            the command to handle
     * @param commandParts       parts of a single command
     */
    private static void handleCommand(GridRepository tempGridRepository, Command command, String[] commandParts) {
        switch (command) {
            case WAIT -> {//do nothing
            }
            case FORWARD -> {
                handleForwardCommand(tempGridRepository, commandParts);
            }
            case POSITION -> {
                handlePositionCommand(tempGridRepository, commandParts);
            }
            case TURNAROUND -> {//update only direction 180 degrees
                handleTurnAroundCommand(tempGridRepository);
            }
            case RIGHT -> {
                handleRightCommand(tempGridRepository);
            }
            default -> throw new IllegalArgumentException(Constants.BAD_COMMAND + commandParts[0]);
        }
    }

    /**
     * Handling the RIGHT command and applying it to the temporary grid by moving one direction
     * @param tempGridRepository temporary grid
     */
    private static void handleRightCommand(GridRepository tempGridRepository) {
        var gridDirection = tempGridRepository.getPosition().direction();
        switch (gridDirection) {
            case EAST ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.SOUTH));
            case WEST ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.NORTH));
            case NORTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.EAST));
            case SOUTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.WEST));
        }
    }

    /**
     * Handling the RIGHT command and applying it to the temporary grid by moving two directions or 180 degrees
     * @param tempGridRepository temporary grid
     */
    private static void handleTurnAroundCommand(GridRepository tempGridRepository) {
        var gridDirection = tempGridRepository.getPosition().direction();
        switch (gridDirection) {
            case EAST ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.WEST));
            case WEST ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.EAST));
            case NORTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.SOUTH));
            case SOUTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), tempGridRepository.getPosition().y(), Direction.NORTH));
        }
    }

    /**
     * Applying the POSITION command  to the temp grid
     * @param tempGridRepository temporary grid
     * @param commandParts parts of the POSITION command as x y and direction
     */
    private static void handlePositionCommand(GridRepository tempGridRepository, String[] commandParts) {
        var commandDirection = Direction.valueOf(commandParts[3]);
        int x = Integer.parseInt(commandParts[1]);
        int y = Integer.parseInt(commandParts[2]);
        tempGridRepository.setPosition(new Position(x, y, commandDirection));
    }

    /**
     * Moving forward the robot x steps
     * @param tempGridRepository temporary grid
     * @param commandParts parts of the FORWARD command as x steps
     */
    private static void handleForwardCommand(GridRepository tempGridRepository, String[] commandParts) {
        var gridDirection = tempGridRepository.getPosition().direction();
        if (commandParts.length != 2) {
            throw new IllegalArgumentException(Constants.WRONG_NUMBER_OF_ARGUMENTS);
        }
        int steps = Integer.parseInt(commandParts[1]);
        switch (gridDirection) {
            case NORTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), Math.max(tempGridRepository.getPosition().y() - steps, 0), gridDirection));
            case EAST ->
                    tempGridRepository.setPosition(new Position(Math.min(tempGridRepository.getPosition().x() + steps, Constants.GRID_SIZE - 1), tempGridRepository.getPosition().y(), gridDirection));
            case SOUTH ->
                    tempGridRepository.setPosition(new Position(tempGridRepository.getPosition().x(), Math.min(tempGridRepository.getPosition().y() + steps, Constants.GRID_SIZE - 1), gridDirection));
            case WEST ->
                    tempGridRepository.setPosition(new Position(Math.max(tempGridRepository.getPosition().x() - steps, 0), tempGridRepository.getPosition().y(), gridDirection));
            default -> throw new IllegalArgumentException(Constants.UNKNOWN_DIRECTION + gridDirection);
        }
    }

    /**
     * Validating whether the command is a valid pattern, according to the description of the assignment
     *
     * @param command String of a single command
     * @return true or false if the command is valid
     */
    public static boolean isValidCommand(String command) {
        if (command == null || command.isEmpty()) {
            return false;
        } else {
            String[] commandParts = command.split(" ");
            if (commandParts.length >= Constants.GRID_SIZE || commandParts.length == 3) {
                return false;
            }
            if (commandParts.length == Constants.GRID_SIZE - 1 && EnumUtils.getEnum(Command.class, commandParts[0]).equals(POSITION)
                    && NUMBER_PATTERN.matcher(commandParts[1]).matches() && NUMBER_PATTERN.matcher(commandParts[2]).matches()
                    && EnumUtils.isValidEnum(Direction.class, commandParts[3])) {
                return true;
            }
            if (commandParts.length == 2 && commandParts[0].equals(FORWARD.toString()) && NUMBER_PATTERN.matcher(commandParts[1]).matches()) {
                return true;
            }
            var enumSet = EnumSet.of(WAIT, TURNAROUND, RIGHT);
            return enumSet.contains(EnumUtils.getEnum(Command.class, commandParts[0]));
        }
    }
}
