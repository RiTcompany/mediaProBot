package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.RegisterDto;
import org.example.entities.BotUser;
import org.example.entities.Role;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.BotUserRepository;
import org.example.repositories.RoleRepository;
import org.example.services.BotUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {
    private final BotUserRepository botUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public BotUser getById(long botUserId) throws EntityNotFoundException {
        return botUserRepository.findById(botUserId)
                .orElseThrow(() -> getException(ERole.ROLE_USER, botUserId));
    }

    @Override
    public BotUser getByEmail(String email) {
        return botUserRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Не существует пользователя с email = ".concat(email))
            );
    }

    @Override
    public boolean existsByTgId(long tgId) {
        return botUserRepository.existsByTgId(tgId);
    }

    @Override
    public BotUser getByChatIdAndRole(
            long chatId, ERole eRole
    ) throws EntityNotFoundException {
        BotUser botUser = botUserRepository.findByTgId(chatId)
                .orElseThrow(() -> getException(eRole, chatId));

        if (!hasRole(botUser, eRole)) {
            throw getException(eRole, chatId);
        }

        return botUser;
    }

    @Override
    public boolean hasRole(BotUser botUser, ERole eRole) {
        return botUser.getRoleList().stream().anyMatch(role -> role.getRoleName().equals(eRole));
    }

    @Override
    public void register(RegisterDto registerDto) {
        BotUser botUser = botUserRepository.findByEmail(registerDto.getEmail()).orElseThrow(() ->
                new EntityNotFoundException("Не существует пользователя с email = ".concat(registerDto.getEmail()))
        );

        botUser.setTgId(registerDto.getTgId());
        botUser.setRoleList(getUserRoleList());
        botUserRepository.saveAndFlush(botUser);
    }

    private EntityNotFoundException getException(ERole eRole, long chatId) {
        return new EntityNotFoundException("Не существует %s с ID = %d".formatted(eRole, chatId));
    }

    private List<Role> getUserRoleList() {
        List<Role> list = new ArrayList<>();
        list.add(roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() ->
                new EntityNotFoundException("Роль USER была удалена из БД")
        ));
        return list;
    }
}
