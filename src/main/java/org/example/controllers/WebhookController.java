package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.bots.MyWebHookBot;
import org.example.dto.HomeworkDto;
import org.example.services.DocumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final MyWebHookBot bot;
    private final DocumentService documentService;

    @PostMapping("/")
    public void onUpdateReceived(@RequestBody Update update) {
        bot.onWebhookUpdateReceived(update);
    }

    @PostMapping("/add_homework")
    public void addHomework(@RequestBody HomeworkDto homeworkDto) {
        documentService.addHomework(homeworkDto, bot);
    }
}