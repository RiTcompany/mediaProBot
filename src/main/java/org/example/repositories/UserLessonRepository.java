package org.example.repositories;

import org.example.entities.UserLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLessonRepository extends JpaRepository<UserLesson, Long> {
    Optional<UserLesson> findByUserIdAndLessonId(long userId, long lessonId);
}
