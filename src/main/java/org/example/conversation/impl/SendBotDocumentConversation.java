package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SendBotDocumentConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.SEND_BOT_DOCUMENT;
    private static final String FINISH_MESSAGE = "Ваш документ был отправлен ученику";

    public SendBotDocumentConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.SEND_BOT_DOCUMENT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
