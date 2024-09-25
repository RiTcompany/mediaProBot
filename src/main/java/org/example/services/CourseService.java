package org.example.services;

import org.example.entities.Course;
import org.springframework.stereotype.Service;

@Service
public interface CourseService {
    Course getById(long courseId);

    Course getByName(String name);
}
