package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.example.exceptions.AbstractException;
import org.example.services.DocumentService;
import org.example.services.UserCourseService;
import org.example.steps.FileSendStep;
import org.example.utils.DiplomaUtil;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SendDocumentStep extends FileSendStep {
    private final DocumentService documentService;
    private final UserCourseService userCourseService;
    private static final String PREPARE_MESSAGE_TEXT = "Отправьте документ, который вы хотите отправить ученику и напишите к нему строку из списка";
    private static final String STUDENT_MESSAGE_TEXT = "Ваш диплом!";
    private static final Pattern pattern = Pattern.compile("^Студент: [a-zA-Zа-яА-яЁё ]+, Курс: [a-zA-Zа-яА-яЁё 0-9:]+, tgId: \\d+$");
    private static final Long MAX_FILE_SIZE_KB = 500L;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws AbstractException {
        ResultDto result = isValidFile(messageDto, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        downloadFile(DiplomaUtil.getChatId(messageDto.getData()), messageDto, sender);
        return finishStep(chatHash, sender, getAnswerMessageText());
    }

    @Override
    protected ResultDto isValidFile(MessageDto messageDto, AbsSender sender) {
//        TODO : add check tgId
        if (messageDto.getData() == null) {
            return new ResultDto(false, "Вы не ввели текст. Скопируйте строку из списка и вставьте её");
        }

        if (!pattern.matcher(messageDto.getData()).matches()) {
            return new ResultDto(false, "Неверный формат текста. Скопируйте строку из списка и вставьте её");
        }

        return ValidUtil.isValidFile(messageDto, MAX_FILE_SIZE_KB);
    }

    @Override
    protected File download(MessageDto messageDto, AbsSender sender) {
        userCourseService.setHasDiploma(messageDto.getData());
        return MessageUtil.downloadFile(messageDto.getDocument().getFileId(), sender);
    }

    @Override
    protected void saveDocument(long chatId, String path) {
        documentService.create(chatId, path, EDocument.DIPLOMA);
    }

    @Override
    protected String getAnswerMessageText() {
        return null;
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        List<DocumentToCheck> newDiplomaList = documentService.getNewDiplomaList();
        newDiplomaList.forEach(documentToCheck -> {
            documentToCheck.setStatus(ECheckStatus.ACCEPTED);
            MessageUtil.sendFile(
                    documentToCheck.getChatId(),
                    new File(documentToCheck.getPath()),
                    STUDENT_MESSAGE_TEXT,
                    sender
            );
        });
        documentService.saveList(newDiplomaList);
        return 0;
    }
}
