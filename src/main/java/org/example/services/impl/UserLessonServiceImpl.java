package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.UserLesson;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.UserLessonRepository;
import org.example.services.UserLessonService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLessonServiceImpl implements UserLessonService {
    private final UserLessonRepository userLessonRepository;

    @Override
    public UserLesson getByUserIdAndLessonId(long userId, long lessonId) {
        return userLessonRepository.findByUserIdAndLessonId(userId, lessonId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Не существует userLesson (user ID = %s, lesson ID = %s)".formatted(userId, lessonId)
                )
        );
    }

    @Override
    public void saveAndFlush(UserLesson userLesson) {
        userLessonRepository.saveAndFlush(userLesson);
    }

    @Override
    public void saveCompleted(long userId, long lessonId) {
        UserLesson userLesson = getByUserIdAndLessonId(userId, lessonId);
        userLesson.setCompleted(true);
        saveAndFlush(userLesson);
    }
}
