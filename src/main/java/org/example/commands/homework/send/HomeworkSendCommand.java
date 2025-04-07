package org.example.commands.homework.send;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class HomeworkSendCommand extends BotCommand {
    private final ConversationService conversationService;
    private final DocumentService documentService;
    private final BotUserService botUserService;

    public HomeworkSendCommand(
            ConversationService conversationService,
            DocumentService documentService,
            BotUserService botUserService
    ) {
        super("send_homework", "Send homework to check");
        this.conversationService = conversationService;
        this.documentService = documentService;
        this.botUserService = botUserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_USER);
            if (documentService.maySendHomework(botUser.getTgId(), EDocument.HOMEWORK)) {
                conversationService.startConversation(
                        chat.getId(), EConversation.SEND_HOMEWORK, absSender
                );
            } else {
                MessageUtil.sendMessageText(chat.getId(), "У вас нет доступных домашних заданий", absSender);
            }
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав.", absSender);
        } catch (AbstractException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Что-то пошло не так, обратитесь в поддержку", absSender);
        }
    }
}
