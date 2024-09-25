package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversation;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ConversationService;
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

    public SendBotDocumentCommand(ConversationService conversationService) {
        super("send_document", "Send bot document to user");
        this.conversationService = conversationService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            conversationService.startConversation(
                    chat.getId(), EConversation.SEND_BOT_DOCUMENT, absSender
            );
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}