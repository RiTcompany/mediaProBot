package org.example.steps.impl.student;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.AbstractException;
import org.example.services.DocumentService;
import org.example.steps.FileSendStep;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

@Component
@RequiredArgsConstructor
public class HomeworkSendStep extends FileSendStep {
    private final DocumentService documentService;
    private static final String PREPARE_MASSAGE_TEXT = "Отправьте сюда ваше домашнее задание в формате pdf";
    private static final long MAX_DOCUMENT_SIZE_KB = 500;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MASSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        return 0;
    }

    @Override
    protected ResultDto isValidFile(MessageDto messageDto, AbsSender sender) {
        return ValidUtil.isValidFile(messageDto, MAX_DOCUMENT_SIZE_KB);
    }

    @Override
    protected File download(MessageDto messageDto, AbsSender sender) {
        return MessageUtil.downloadFile(messageDto.getDocument().getFileId(), sender);
    }

    @Override
    protected void saveDocument(long chatId, String path) {
        documentService.setPath(chatId, path);
    }

    @Override
    protected String getAnswerMessageText() {
        return null;
    }
}
