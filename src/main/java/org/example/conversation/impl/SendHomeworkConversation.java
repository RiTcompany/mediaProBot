package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SendHomeworkConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.HOMEWORK_CHOICE;
    private static final String FINISH_MESSAGE = "Ваше домашнее задание отправлено на проверку, ожидайте ответа";

    public SendHomeworkConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.HOMEWORK_CHOICE, new ArrayList<>() {{
                add(EConversationStep.HOMEWORK_SEND);
            }});
            put(EConversationStep.HOMEWORK_SEND, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
