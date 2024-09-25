package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.BotUser;
import org.example.entities.Course;
import org.example.entities.UserCourse;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.UserCourseRepository;
import org.example.services.BotUserService;
import org.example.services.CourseService;
import org.example.services.UserCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository userCourseRepository;
    private final BotUserService botUserService;
    private final CourseService courseService;

    @Override
    public String getFinishedUserListText() {
        StringBuilder result = new StringBuilder();
        List<UserCourse> userCourseList = userCourseRepository.findAllByIsCompletedTrueAndHasDiplomaFalse();
        if (userCourseList.isEmpty()) {
            return "Сейчас список пуст";
        } else {
            userCourseList.forEach(userCourse -> {
                BotUser botUser = botUserService.getById(userCourse.getUserId());
                Course course = courseService.getById(userCourse.getCourseId());
                result.append("Студент: %s, Курс: %s, tgId: %d\n"
                        .formatted(botUser.getFullName(), course.getName(), botUser.getTgId())
                );
            });
            return result.toString();
        }
    }

    @Override
    public void setHasDiploma(String userCourseData) {
        String[] partArray = userCourseData.split(", ");

        long chatId = Long.parseLong(partArray[partArray.length - 1].substring(6));
        long userId = botUserService.getByChatIdAndRole(chatId, ERole.ROLE_USER).getId();

        String courseName = partArray[1].substring(6);
        long courseId = courseService.getByName(courseName).getId();

        UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует пары (студент ID = %s, курс ID = %s)"
                                .formatted(userId, courseName)
                ));

        userCourse.setHasDiploma(true);
        userCourseRepository.saveAndFlush(userCourse);
    }
}
