package org.example.commands.homework.check.impl;

import org.example.commands.homework.check.CheckDocumentCommand;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.springframework.stereotype.Component;

@Component
public class CheckHomeworkCommand extends CheckDocumentCommand {
    private static final String COMMAND_MODIFIER = "check_homework";
    private static final String COMMAND_DESCRIPTION = "Check homework";
    private static final String NO_DOCS_MESSAGE_TEXT = "Сейчас нет документов для проверки";
    private static final EDocument DOCUMENT_TYPE = EDocument.HOMEWORK;
    private static final EConversation CONVERSATION_TYPE = EConversation.CHECK_HOMEWORK;

    public CheckHomeworkCommand(
            ConversationService conversationService,
            BotUserService botUserService,
            DocumentService documentService
    ) {
        super(
                COMMAND_MODIFIER,
                COMMAND_DESCRIPTION,
                conversationService,
                botUserService,
                documentService
        );
    }

    @Override
    protected String getNoDocsMessageText() {
        return NO_DOCS_MESSAGE_TEXT;
    }

    @Override
    protected EDocument getDocumentType() {
        return DOCUMENT_TYPE;
    }

    @Override
    protected EConversation getConversationType() {
        return CONVERSATION_TYPE;
    }
}
