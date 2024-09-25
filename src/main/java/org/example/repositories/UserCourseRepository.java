package org.example.repositories;

import org.example.entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findAllByIsCompletedTrueAndHasDiplomaFalse();

    Optional<UserCourse> findByUserIdAndCourseId(long userId, long courseId);
}
