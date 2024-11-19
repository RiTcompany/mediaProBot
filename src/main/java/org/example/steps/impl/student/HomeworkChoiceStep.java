package org.example.steps.impl.student;

import lombok.RequiredArgsConstructor;
import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.BotUser;
import org.example.entities.ChatHash;
import org.example.entities.DocumentToCheck;
import org.example.entities.Lesson;
import org.example.enums.ERole;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.services.LessonService;
import org.example.steps.ChoiceStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeworkChoiceStep extends ChoiceStep {
    private final DocumentService documentService;
    private final LessonService lessonService;
    private final BotUserService botUserService;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>домашнее задание</b>, которое вы хотите сдать:";

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, "Выберите один из выше предложенных вариантов");
        }

        if (!documentService.existsById(Long.parseLong(messageDto.getData()))) {
            return new ResultDto(false, "Такого домашнего задания не существует. Обратитесь в поддержку.");
        }

        return new ResultDto(true);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageWithPageableKeyBoard(
                chatHash, PREPARE_MESSAGE_TEXT, keyboardDto(chatHash), sender
        );
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        documentService.setCheckStatus(Long.parseLong(data));
        return 0;
    }

    private KeyboardDto keyboardDto(ChatHash chatHash) {
        BotUser botUser = botUserService.getByChatIdAndRole(chatHash.getChatId(), ERole.ROLE_USER);
        return keyboardMapper.keyboardDto(chatHash, getButtonList(
                documentService.getHomeworkToSendListByUserId(botUser.getId())
        ));
    }

    private List<ButtonDto> getButtonList(List<DocumentToCheck> documentToCheckList) {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (DocumentToCheck document : documentToCheckList) {
            Lesson lesson = lessonService.getById(document.getLessonId());
            String name = "%s (%s)".formatted(lesson.getName(), lesson.getCourse().getName());
            buttonDtoList.add(new ButtonDto(document.getId().toString(), name));
        }

        return buttonDtoList;
    }
}
