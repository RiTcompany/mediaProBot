package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.example.bots.MyLongPoolingBot;
import org.example.dto.HomeworkDto;
import org.example.dto.RegisterDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.NoTgIdException;
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

    @Operation(summary = "Добавление домашнего задания",
            description = "Добавляет новое домашнее задание через Telegram-бота")
    @ApiResponses({
            @ApiResponse(responseCode = "406", description = "Telegram ID не указан"),
            @ApiResponse(responseCode = "404", description = "Сущность не найдена")
    })
    @PostMapping("/add_homework")
    public void addHomework(@RequestBody HomeworkDto homeworkDto) throws NoTgIdException, EntityNotFoundException {
        documentService.addHomework(homeworkDto, bot);
    }

    @Operation(summary = "Регистрация пользователя",
            description = "Регистрирует нового пользователя в системе")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Сущность не найдена")
    })
    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterDto registerDto) throws EntityNotFoundException {
        botUserService.register(registerDto);
    }
}