package org.example.commands;

import org.example.builders.MessageBuilder;
import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.services.BotUserService;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class StartCommand extends BotCommand {
    @Value("${app_link_tmp}")
    private String LINK;
    private final BotUserService botUserService;
    private static final String HELLO_MESSAGE = """
            Привет, дорогой друг!\s
            Я – бот Медиа Про. Здесь ты сможешь отправлять свои домашние задания и получать обратную связь!""";
    private static final String REGISTER_MESSAGE = """
            Привет, дорогой друг!\s
            Я – бот Медиа Про. Здесь ты сможешь отправлять свои домашние задания и получать обратную связь!
            Пожалуйста перейди по ссылке для окончания регистрации""";

    public StartCommand(BotUserService botUserService) {
        super("start", "Start command");
        this.botUserService = botUserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        long chatId = chat.getId();

        if (botUserService.existsByTgId(chatId)) {
            MessageUtil.sendMessageText(chat.getId(), HELLO_MESSAGE, absSender);
        } else {
            KeyboardDto keyboardDto = new KeyboardDto();
            keyboardDto.setButtonDtoList(List.of(
                    new ButtonDto("register_link", "Регистрация", LINK.concat(String.valueOf(chatId)))
            ));

            MessageUtil.sendMessage(
                    MessageBuilder.create()
                            .setText(REGISTER_MESSAGE)
                            .setInlineKeyBoard(keyboardDto)
                            .sendMessage(chatId),
                    absSender
            );
        }
    }
}
