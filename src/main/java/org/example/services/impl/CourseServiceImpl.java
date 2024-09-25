package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Course;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.CourseRepository;
import org.example.services.CourseService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public Course getById(long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Не существует Course ID = ".concat(String.valueOf(courseId)))
        );
    }

    @Override
    public Course getByName(String name) {
        return courseRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException("Не существует курса = ".concat(name))
        );
    }
}
