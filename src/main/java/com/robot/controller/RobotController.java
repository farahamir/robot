package com.robot.controller;

import com.robot.model.RobotCommandRequest;
import com.robot.repository.GridRepository;
import com.robot.service.PositionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/robot")
public class RobotController {

    private final PositionService positionService;

    @Autowired
    public RobotController(PositionService positionService) {
        this.positionService = positionService;
    }

    @Tag(name = "placeRobot", description = "The API for a executing the script of the robot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GridRepository.class))})})

    @PostMapping("/place")
    public ResponseEntity<GridRepository> placeRobot(@NonNull @RequestBody RobotCommandRequest robotCommandRequest) {
        GridRepository grid = positionService.executeScript(robotCommandRequest.getScript());
        return new ResponseEntity<>(grid, HttpStatus.CREATED);
    }
}
