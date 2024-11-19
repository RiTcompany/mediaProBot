package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.EConversation;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.UserCourseService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class SendBotDocumentCommand extends BotCommand {
    private final ConversationService conversationService;
    private final UserCourseService userCourseService;
    private final BotUserService botUserService;
    private static final String NO_STUDENT_MESSAGE = "Сейчас список пуст";

    public SendBotDocumentCommand(
            ConversationService conversationService,
            UserCourseService userCourseService,
            BotUserService botUserService
    ) {
        super("send_document", "Send bot document to user");
        this.conversationService = conversationService;
        this.userCourseService = userCourseService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_MODERATOR);
            String message = userCourseService.getFinishedUserListText();
            MessageUtil.sendMessageText(chat.getId(), message, absSender);
            if (!NO_STUDENT_MESSAGE.equals(message)) {
                conversationService.startConversation(
                        chat.getId(), EConversation.SEND_BOT_DOCUMENT, absSender
                );
            }
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}