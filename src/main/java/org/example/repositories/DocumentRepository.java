package org.example.repositories;

import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentToCheck, Long> {
    Optional<DocumentToCheck> findFirstByStatusAndDocumentType(ECheckStatus status, EDocument eDocument);

    Optional<DocumentToCheck> findFirstByStatusAndModeratorIdAndDocumentType(
            ECheckStatus status, Long moderatorId, EDocument eDocument
    );

    default boolean isNotExistByChatIdAndDocumentTypeAndNotFinishedStatus(
            Long chatId, EDocument documentType
    ) {
        return !findAllByChatIdAndDocumentTypeAndStatus(
                chatId, documentType, ECheckStatus.READY_TO_SEND
        ).isEmpty();
    }

    Optional<DocumentToCheck> findFirstByChatIdAndDocumentTypeAndStatus(
            long chatId, EDocument eDocument, ECheckStatus status
    );

    default List<DocumentToCheck> getHomeworkToSendList(long chatId) {
        return findAllByChatIdAndDocumentTypeAndStatus(chatId, EDocument.HOMEWORK, ECheckStatus.READY_TO_SEND);
    }

    List<DocumentToCheck> findAllByChatIdAndDocumentTypeAndStatus(long chatId, EDocument eDocument, ECheckStatus status);

    Optional<DocumentToCheck> findByChatIdAndStatus(long chatId, ECheckStatus status);

    default List<DocumentToCheck> getNewDiplomaList() {
        return findAllByDocumentTypeAndStatus(EDocument.DIPLOMA, ECheckStatus.NEW);
    }

    List<DocumentToCheck> findAllByDocumentTypeAndStatus(EDocument type, ECheckStatus status);
}
