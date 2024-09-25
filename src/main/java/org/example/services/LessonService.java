package org.example.services;

import org.example.entities.Lesson;
import org.springframework.stereotype.Service;

@Service
public interface LessonService {
    Lesson getById(Long lessonId);
}
