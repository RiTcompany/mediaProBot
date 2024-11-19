package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.BotMessage;
import org.example.enums.EBotMessage;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.BotMessageMapper;
import org.example.repositories.BotMessageRepository;
import org.example.services.BotMessageButtonService;
import org.example.services.BotMessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotMessageServiceImpl implements BotMessageService {
    private final BotMessageButtonService botMessageButtonService;
    private final BotMessageRepository botMessageRepository;
    private final BotMessageMapper botMessageMapper;

    @Override
    public void create(long botUserId) {
        BotMessage botMessage = botMessageMapper.botMessage(botUserId);
        botMessageRepository.saveAndFlush(botMessage);
    }

    @Override
    public BotMessage getProcessedMessageByUserId(long botUserId) throws EntityNotFoundException {
        return botMessageRepository.findByWriterIdAndStatus(botUserId, EBotMessage.WRITING)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует сообщения с writerId = %d".formatted(botUserId)
                ));
    }
}
