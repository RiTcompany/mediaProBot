package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckHomeworkConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.HOMEWORK_COMMENT_AND_MARK_INPUT;
    private static final String FINISH_MESSAGE = "Проверка документа завершена";

    public CheckHomeworkConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.HOMEWORK_COMMENT_AND_MARK_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
