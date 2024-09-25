package org.example.configs;

import org.example.conversation.AConversation;
import org.example.conversation.impl.CheckHomeworkConversation;
import org.example.conversation.impl.SendBotDocumentConversation;
import org.example.conversation.impl.SendHomeworkConversation;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.steps.ConversationStep;
import org.example.steps.impl.moderator.HomeworkCommentAndMarkInputStep;
import org.example.steps.impl.student.HomeworkChoiceStep;
import org.example.steps.impl.student.HomeworkSendStep;
import org.example.steps.impl.writer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConversationConfig {
    @Bean
    public Map<EConversation, AConversation> conversationMap(
            @Autowired SendHomeworkConversation sendHomeworkConversation,
            @Autowired CheckHomeworkConversation checkHomeworkConversation,
            @Autowired SendBotDocumentConversation sendBotDocumentConversation
    ) {
        Map<EConversation, AConversation> conversationMap = new HashMap<>();
        conversationMap.put(EConversation.SEND_HOMEWORK, sendHomeworkConversation);
        conversationMap.put(EConversation.CHECK_HOMEWORK, checkHomeworkConversation);
        conversationMap.put(EConversation.SEND_BOT_DOCUMENT, sendBotDocumentConversation);
        return conversationMap;
    }

    @Bean
    public Map<EConversationStep, ConversationStep> conversationStepMap(
            @Autowired HomeworkChoiceStep homeworkChoiceStep,
            @Autowired HomeworkSendStep homeworkSendStep,
            @Autowired HomeworkCommentAndMarkInputStep homeworkCommentAndMarkInputStep,
            @Autowired TextInputStep textInputStep,
            @Autowired ButtonAddChoiceStep buttonAddChoiceStep,
            @Autowired ButtonInputStep buttonInputStep,
            @Autowired SendDocumentStep sendDocumentStep
    ) {
        return new HashMap<>() {{
            put(EConversationStep.HOMEWORK_CHOICE, homeworkChoiceStep);
            put(EConversationStep.HOMEWORK_SEND, homeworkSendStep);
            put(EConversationStep.HOMEWORK_COMMENT_AND_MARK_INPUT, homeworkCommentAndMarkInputStep);
            put(EConversationStep.BOT_MESSAGE_TEXT_INPUT, textInputStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE, buttonAddChoiceStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_INPUT, buttonInputStep);
            put(EConversationStep.SEND_BOT_DOCUMENT, sendDocumentStep);
        }};
    }
}
