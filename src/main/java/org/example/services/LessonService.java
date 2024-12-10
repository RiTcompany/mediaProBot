package org.example.services;

import org.example.entities.Lesson;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface LessonService {
    Lesson getById(Long lessonId) throws EntityNotFoundException;
}
