package org.example.services;

import org.springframework.stereotype.Service;

@Service
public interface UserCourseService {
    String getFinishedUserListText();

    void setHasDiploma(String userCourseData);
}
