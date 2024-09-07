package com.robot.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RobotCommandRequest {

    @Schema(description = "Script of the commands", example = "POSITION 1 3 EAST\\nFORWARD 3\\nWAIT\\nFORWARD 3\\nTURNAROUND\\nFORWARD 3\\nRIGHT" )
    @NotNull
    @NotEmpty
    private String script;
}
