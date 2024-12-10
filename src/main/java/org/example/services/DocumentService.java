package org.example.services;

import org.example.dto.HomeworkDto;
import org.example.entities.DocumentToCheck;
import org.example.enums.EDocument;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.NoTgIdException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Service
public interface DocumentService {
    void create(long chaId, String path, EDocument eDocument);

    DocumentToCheck getToCheck(EDocument eDocument);

    DocumentToCheck getCheckingDocument(long moderatorId, EDocument eDocument) throws EntityNotFoundException;

    DocumentToCheck saveResponse(long moderatorId, String message, EDocument eDocument) throws EntityNotFoundException;

    void setModerator(DocumentToCheck documentToCheck, long botUserId);

    boolean maySendHomework(long chatId, EDocument eDocument);

    void addHomework(HomeworkDto homeworkDto, AbsSender sender) throws NoTgIdException, EntityNotFoundException;

    boolean existsById(Long documentId);

    List<DocumentToCheck> getHomeworkToSendListByUserId(Long userId);

    void setCheckStatus(long documentId);

    DocumentToCheck getById(long documentId);

    void setPath(long chatId, String path);

    List<DocumentToCheck> getNewDiplomaList();

    void saveList(List<DocumentToCheck> documentToCheckList);
}
