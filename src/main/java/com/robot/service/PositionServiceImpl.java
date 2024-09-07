package com.robot.service;

import com.robot.helper.Constants;
import com.robot.helper.ServiceHelper;
import com.robot.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService {

    private final GridRepository gridRepository;

    @Autowired
    public PositionServiceImpl(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }
    @Override
    public synchronized GridRepository executeScript(String script) {
        var tempGrid = new GridRepository(gridRepository);
        var commands = script.split(Constants.LINE_BREAK);
        for (String command : commands) {
            if (ServiceHelper.isValidCommand(command)){
                ServiceHelper.executeCommand(command, tempGrid);
            }else {
                return gridRepository;
            }

        }
        gridRepository.setPosition(tempGrid.getPosition());
        return gridRepository;
    }
}
