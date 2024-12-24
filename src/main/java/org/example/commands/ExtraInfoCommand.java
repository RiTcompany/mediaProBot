package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class ExtraInfoCommand extends BotCommand {
    private final BotUserService botUserService;

    private final String MESSAGE = """
            <b>Ссылка на дополнительные материалы:</b>
            https://disk.yandex.ru/d/6i_TFMyQXOLo5g
            ️☀️ Хорошего обучения ☀️
            """;

    public ExtraInfoCommand(BotUserService botUserService) {
        super("extra_info", "Extra information command");
        this.botUserService = botUserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_USER);
            MessageUtil.sendMessageText(chat.getId(), MESSAGE, absSender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}
