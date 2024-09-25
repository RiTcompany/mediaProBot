package org.example.services;

import org.example.entities.UserLesson;
import org.springframework.stereotype.Service;


@Service
public interface UserLessonService {
    UserLesson getByUserIdAndLessonId(long userId, long lessonId);

    void saveAndFlush(UserLesson userLesson);

    void saveCompleted(long userId, long lessonId);
}
