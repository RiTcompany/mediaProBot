package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Lesson;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.LessonRepository;
import org.example.services.LessonService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Override
    public Lesson getById(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() ->
                new EntityNotFoundException("Не существует урока с ID = ".concat(String.valueOf(lessonId)))
        );
    }
}
