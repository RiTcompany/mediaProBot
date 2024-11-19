package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.bots.MyLongPoolingBot;
import org.example.dto.HomeworkDto;
import org.example.dto.RegisterDto;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final MyLongPoolingBot bot;
    private final DocumentService documentService;
    private final BotUserService botUserService;

    @PostMapping("/add_homework")
    public void addHomework(@RequestBody HomeworkDto homeworkDto) {
        documentService.addHomework(homeworkDto, bot);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterDto registerDto) {
        botUserService.register(registerDto);
    }

}