package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.UserCourseService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class FinishedStudentListCommand extends BotCommand {
    private final UserCourseService userCourseService;
    private final BotUserService botUserService;

    public FinishedStudentListCommand(UserCourseService userCourseService, BotUserService botUserService) {
        super("finished_student_list", "Finished student list command");
        this.userCourseService = userCourseService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            MessageUtil.sendMessageText(chat.getId(), userCourseService.getFinishedUserListText(), absSender);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}