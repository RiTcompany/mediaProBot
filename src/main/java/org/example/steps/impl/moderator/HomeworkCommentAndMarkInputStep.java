package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.ResultDto;
import org.example.entities.BotUser;
import org.example.entities.ChatHash;
import org.example.entities.DocumentToCheck;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

@Component
@RequiredArgsConstructor
public class HomeworkCommentAndMarkInputStep extends InputStep {
    private final DocumentService documentService;
    private final BotUserService botUserService;
    private static final double MAX_MARK = 5;
    private static final double MIN_MARK = 0;
    private static final String PREPARE_MESSAGE_TEXT = """
            Проверьте домашнее задание ученика.
            Напишите комментарий, который будет выслан ученику, а так же оценку. Между должно быть 2 переноса строки.
            
            Пример:
            "Комментарий
            
            Оценка"
            """;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        BotUser botUser = botUserService.getByChatIdAndRole(chatHash.getChatId(), ERole.ROLE_MODERATOR);
        DocumentToCheck documentToCheck = documentService.getCheckingDocument(
                botUser.getId(), EDocument.HOMEWORK
        );

        Message message = MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(new File(documentToCheck.getPath()))
                        .setText(PREPARE_MESSAGE_TEXT)
                        .sendDocument(chatHash.getChatId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (data.isBlank()) {
            return new ResultDto(false, "Вы ввели пустую строку. Попробуйте снова");
        }

        if (data.length() > ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        try {
            String[] messagePartArray = data.split("\n\n");
            double mark = Double.parseDouble(messagePartArray[1]);
            if (MIN_MARK > mark || mark > MAX_MARK) {
                return new ResultDto(false, "Введите оценку от %f до %f".formatted(MIN_MARK, MAX_MARK));
            }

        } catch (NumberFormatException e) {
            return new ResultDto(false, "Введите дробное число");
        } catch (ArrayIndexOutOfBoundsException e) {
            return new ResultDto(false, "Между комментарием и оценкой должно быть 2 переноса строки");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long moderatorId = botUserService.getByChatIdAndRole(chatHash.getChatId(), ERole.ROLE_MODERATOR).getId();
        DocumentToCheck documentToCheck = documentService.saveResponse(
                moderatorId, data, EDocument.HOMEWORK
        );
        sendMessageToVolunteer(documentToCheck.getChatId(), collectMessageToVolunteer(data), sender);
        return 0;
    }

    private void sendMessageToVolunteer(long chatId, String comment, AbsSender sender) {
        MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(comment)
                        .sendMessage(chatId),
                sender
        );
    }

    private String collectMessageToVolunteer(String moderatorMessage) {
        String[] partArray = moderatorMessage.split("\n\n");
        double mark = Double.parseDouble(partArray[1]);
        String result = (mark < 4) ? "Переделайте работу" : "Работа успешно сдана";
        return "<i>Комментарий от проверяющего:</i> %s\n<i>Оценка:</i> %s\n<i>Результат:</i> %s"
                .formatted(partArray[0], partArray[1], result);
    }
}
