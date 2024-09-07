package com.robot.service;

import com.robot.repository.GridRepository;

public interface PositionService {
    /**
     * Executing the commands in the script
     * @param script to be executed which can have multiple commands
     * @return the final position grid of the robot
     */
    GridRepository executeScript(String script);
}
