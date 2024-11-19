package org.example.services;

import org.example.entities.BotMessage;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface BotMessageService {
    void create(long botUserId);

    BotMessage getProcessedMessageByUserId(long botUserId) throws EntityNotFoundException;
}
