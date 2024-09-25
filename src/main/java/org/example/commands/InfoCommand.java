package org.example.commands;

import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class InfoCommand extends BotCommand {
    private final String MESSAGE = """
            <b>Краткая инструкция по учебному плану:</b>
                1) После прохождения урока в мобильном приложении, вы переходите в бота по кнопке, которая расположена в уроке
                2) После её нажатия вам становится доступна возможность сдать домашнее задание по данному уроку.
                3) Команда /send_homework позволит вам отправить домашнее задание в формате PDF
                4) После отправки пожалуйста дождитесь, когда модератор всё проверит и вышлет вам комментарий
                5) Если вас попросили переделать домашнее задание - пожалуйста учтите все коммантарии и отправтье документ снова
                6) После прохождения курса, мы начнём готовить ваш диплом и вышлем его в этом же чате
            
            P.S. Ссылка на общий чат: https://t.me/+PSVuM3t36OFiZWM6   
            ️☀️ Хорошего обучения ☀️
            """;

    public InfoCommand() {
        super("info", "Information command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        MessageUtil.sendMessageText(chat.getId(), MESSAGE, absSender);
    }
}
