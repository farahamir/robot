package com.robot.controller;

import com.robot.model.Direction;
import com.robot.model.RobotCommandRequest;
import com.robot.repository.GridRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RobotControllerITest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void resettingGrid() {
        var robotCommandRequest = new RobotCommandRequest("POSITION 0 0 EAST");
        var request = new HttpEntity<>(robotCommandRequest);

        this.restTemplate.postForObject("http://localhost:" + port + "/robot/place", request, GridRepository.class);
    }

    @Test
    void when_sending_wait_command_then_nothing_change() {
        var robotCommandRequest = new RobotCommandRequest("WAIT");
        var request = new HttpEntity<>(robotCommandRequest);

        var result = this.restTemplate.postForObject("http://localhost:" + port + "/robot/place",
                request, GridRepository.class);
        assertThat(result).isNotNull();
        assertThat(result.getPosition().x()).isEqualTo(0);
        assertThat(result.getPosition().y()).isEqualTo(0);
        assertThat(result.getPosition().direction()).isEqualTo(Direction.EAST);
    }

    @Test
    void when_sending_bad_script_then_robot_position_not_changes() {
        var robotCommandRequest = new RobotCommandRequest("POSITION 1 3 EAST\nFORWARD 3\nWAIT\nBAD COMMAND\nFORWARD 3\nFORWARD 3\nRIGHT");
        var request = new HttpEntity<>(robotCommandRequest);

        var result = (this.restTemplate.postForObject("http://localhost:" + port + "/robot/place",
                request, GridRepository.class));
        assertThat(result.getPosition().x()).isEqualTo(0);
        assertThat(result.getPosition().y()).isEqualTo(0);
        assertThat(result.getPosition().direction()).isEqualTo(Direction.EAST);
    }

    @Test
    void when_script_sent_then_robot_position_changes() {
        var robotCommandRequest = new RobotCommandRequest("POSITION 1 3 EAST\nFORWARD 3\nWAIT\nFORWARD 3\nTURNAROUND\nFORWARD 3\nRIGHT");
        var request = new HttpEntity<>(robotCommandRequest);

        var result = (this.restTemplate.postForObject("http://localhost:" + port + "/robot/place",
                request, GridRepository.class));
        assertThat(result.getPosition().x()).isEqualTo(1);
        assertThat(result.getPosition().y()).isEqualTo(3);
        assertThat(result.getPosition().direction()).isEqualTo(Direction.NORTH);
    }

    @Test
    void when_sending_multiple_scripts_sent_then_robot_position_changes() {
        var robotCommandRequest = new RobotCommandRequest("POSITION 1 3 EAST\nFORWARD 3\nWAIT\nFORWARD 3\nTURNAROUND\nFORWARD 3\nRIGHT");
        var request = new HttpEntity<>(robotCommandRequest);

        var result = (this.restTemplate.postForObject("http://localhost:" + port + "/robot/place",
                request, GridRepository.class));
        assertThat(result.getPosition().x()).isEqualTo(1);
        assertThat(result.getPosition().y()).isEqualTo(3);
        assertThat(result.getPosition().direction()).isEqualTo(Direction.NORTH);

        robotCommandRequest = new RobotCommandRequest("FORWARD 3\nRIGHT");
        request = new HttpEntity<>(robotCommandRequest);

        result = (this.restTemplate.postForObject("http://localhost:" + port + "/robot/place",
                request, GridRepository.class));
        assertThat(result.getPosition().x()).isEqualTo(1);
        assertThat(result.getPosition().y()).isEqualTo(0);
        assertThat(result.getPosition().direction()).isEqualTo(Direction.EAST);


    }
}