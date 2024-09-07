package com.robot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robot.model.RobotCommandRequest;
import com.robot.repository.GridRepository;
import com.robot.service.PositionService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(RobotController.class)
class RobotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PositionService service;

    @Test
    void when_posting_valid_request_then_return_201() throws Exception {
        var grid = new GridRepository();
        when(service.executeScript("WAIT")).thenReturn(grid);
        this.mockMvc.perform(post("/robot/place")
                        .content(asJsonString(new RobotCommandRequest("WAIT")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").exists());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}