package org.example.services;

import org.example.dto.RegisterDto;
import org.example.entities.BotUser;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface BotUserService {
    BotUser getById(long botUserId);

    BotUser getByEmail(String email);

    boolean existsByTgId(long tgId);

    BotUser getByChatIdAndRole(long chatId, ERole eRole) throws EntityNotFoundException;

    boolean hasRole(BotUser botUser, ERole eRole);

    void register(RegisterDto registerDto);
}
