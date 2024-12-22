package org.example.commands;

import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ExtraInfoCommand extends BotCommand {
    private final String MESSAGE = """
            <b>Ссылка на дополнительные материалы:</b>
            https://disk.yandex.ru/d/6i_TFMyQXOLo5g
            ️☀️ Хорошего обучения ☀️
            """;

    public ExtraInfoCommand() {
        super("extra_info", "Extra information command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        MessageUtil.sendMessageText(chat.getId(), MESSAGE, absSender);
    }
}
