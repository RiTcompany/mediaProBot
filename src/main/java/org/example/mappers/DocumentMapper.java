package org.example.mappers;

import lombok.RequiredArgsConstructor;
import org.example.dto.HomeworkDto;
import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentMapper {
    private final BotUserService botUserService;

    public DocumentToCheck document(long chatId, String path, EDocument eDocument) {
        DocumentToCheck documentToCheck = new DocumentToCheck();
        documentToCheck.setChatId(chatId);
        documentToCheck.setPath(path);
        documentToCheck.setStatus(ECheckStatus.NEW);
        documentToCheck.setDocumentType(eDocument);
        return documentToCheck;
    }

    public DocumentToCheck document(HomeworkDto homeworkDto) {
        DocumentToCheck documentToCheck = new DocumentToCheck();
        documentToCheck.setChatId(botUserService.getByEmail(homeworkDto.getEmail()).getTgId());
        documentToCheck.setLessonId(homeworkDto.getLessonId());
        documentToCheck.setDocumentType(EDocument.HOMEWORK);
        documentToCheck.setStatus(ECheckStatus.READY_TO_SEND);
        return documentToCheck;
    }

    public DocumentToCheck document(DocumentToCheck document) {
        document.setStatus(ECheckStatus.READY_TO_SAVE_FILE);
        return document;
    }

    public DocumentToCheck document(DocumentToCheck document, String path) {
        document.setPath(path);
        document.setStatus(ECheckStatus.NEW);
        return document;
    }

    public DocumentToCheck newDocument(DocumentToCheck document) {
        DocumentToCheck documentToCheck = new DocumentToCheck();
        documentToCheck.setChatId(document.getChatId());
        documentToCheck.setLessonId(document.getLessonId());
        documentToCheck.setDocumentType(EDocument.HOMEWORK);
        documentToCheck.setStatus(ECheckStatus.READY_TO_SEND);
        return documentToCheck;
    }
}
