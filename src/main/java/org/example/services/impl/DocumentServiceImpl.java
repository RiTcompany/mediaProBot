package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.HomeworkDto;
import org.example.entities.BotUser;
import org.example.entities.DocumentToCheck;
import org.example.entities.Lesson;
import org.example.entities.UserLesson;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.DocumentMapper;
import org.example.repositories.DocumentRepository;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.services.LessonService;
import org.example.services.UserLessonService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private static final double MIN_GOOD_MARK = 4;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final BotUserService botUserService;
    private final LessonService lessonService;
    private final UserLessonService userLessonService;

    @Override
    public void create(long chaId, String path, EDocument eDocument) {
        DocumentToCheck documentToCheck = documentMapper.document(chaId, path, eDocument);
        documentRepository.saveAndFlush(documentToCheck);
    }

    @Override
    public DocumentToCheck getToCheck(EDocument eDocument) {
        return documentRepository.findFirstByStatusAndDocumentType(ECheckStatus.NEW, eDocument)
                .orElse(null);
    }

    @Override
    public DocumentToCheck getCheckingDocument(long moderatorId, EDocument eDocument) throws EntityNotFoundException {
        return documentRepository.findFirstByStatusAndModeratorIdAndDocumentType(
                        ECheckStatus.CHECKING, moderatorId, eDocument
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует документа (%s) с moderatorID = %d со статусом %s"
                                .formatted(eDocument, moderatorId, ECheckStatus.CHECKING)
                ));
    }

    @Override
    public DocumentToCheck saveResponse(long moderatorId, String message, EDocument eDocument) throws EntityNotFoundException {
        DocumentToCheck documentToCheck = getCheckingDocument(moderatorId, eDocument);

        String[] messagePartArray = message.split("\n\n");
        String comment = messagePartArray[0];
        double mark = Double.parseDouble(messagePartArray[1]);
        documentToCheck.setComment(comment);
        documentToCheck.setMark(mark);

        if (mark < MIN_GOOD_MARK) {
            documentToCheck.setStatus(ECheckStatus.FAILED);
            documentRepository.saveAndFlush(documentMapper.newDocument(documentToCheck));
        } else {
            documentToCheck.setStatus(ECheckStatus.ACCEPTED);
            userLessonService.saveCompleted(
                    botUserService.getByChatIdAndRole(documentToCheck.getChatId(), ERole.ROLE_USER).getId(),
                    documentToCheck.getLessonId()
            );
        }

        documentRepository.saveAndFlush(documentToCheck);
        return documentToCheck;
    }

    @Override
    public void setModerator(DocumentToCheck documentToCheck, long botUserId) {
        documentToCheck.setModeratorId(botUserId);
        documentToCheck.setStatus(ECheckStatus.CHECKING);
        documentRepository.saveAndFlush(documentToCheck);
    }

    @Override
    public boolean maySendHomework(long chatId, EDocument eDocument) {
        return documentRepository.isNotExistByChatIdAndDocumentTypeAndNotFinishedStatus(chatId, eDocument);
    }

    @Override
    public void addHomework(HomeworkDto homeworkDto, AbsSender sender) {
        documentRepository.saveAndFlush(documentMapper.document(homeworkDto));
        BotUser botUser = botUserService.getById(homeworkDto.getUserId());
        Lesson lesson = lessonService.getById(homeworkDto.getLessonId());
        MessageUtil.sendMessageText(
                botUser.getTgId(),
                "Поздравляю, ты можешь сдать домашнее задание по уроку \"%s\" из курса \"%s\""
                        .formatted(lesson.getName(), lesson.getCourse().getName()),
                sender
        );
    }

    @Override
    public boolean existsById(Long documentId) {
        return documentRepository.existsById(documentId);
    }

    @Override
    public List<DocumentToCheck> getHomeworkToSendListByUserId(Long userId) {
        return documentRepository.getHomeworkToSendList(botUserService.getById(userId).getTgId());
    }

    @Override
    public void setCheckStatus(long documentId) {
        DocumentToCheck documentToCheck = documentMapper.document(getById(documentId));
        documentRepository.saveAndFlush(documentToCheck);
    }

    @Override
    public DocumentToCheck getById(long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() ->
                new EntityNotFoundException("Не существует документа ID = ".concat(String.valueOf(documentId)))
        );
    }

    @Override
    public void setPath(long chatId, String path) {
        DocumentToCheck document = documentRepository.findByChatIdAndStatus(chatId, ECheckStatus.READY_TO_SAVE_FILE)
                .orElseThrow(() -> new EntityNotFoundException("Нельзя сохранить файл в документ CHAT_ID = ".concat(String.valueOf(chatId))));
        documentRepository.saveAndFlush(documentMapper.document(document, path));
    }

    @Override
    public List<DocumentToCheck> getNewDiplomaList() {
        return documentRepository.getNewDiplomaList();
    }

    @Override
    public void saveList(List<DocumentToCheck> documentToCheckList) {
        documentRepository.saveAllAndFlush(documentToCheckList);
    }
}
