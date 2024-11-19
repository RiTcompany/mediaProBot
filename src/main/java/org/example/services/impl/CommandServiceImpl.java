package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.commands.FinishedStudentListCommand;
import org.example.commands.InfoCommand;
import org.example.commands.SendBotDocumentCommand;
import org.example.commands.StartCommand;
import org.example.commands.homework.check.impl.CheckHomeworkCommand;
import org.example.commands.homework.send.HomeworkSendCommand;
import org.example.services.CommandService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    private final StartCommand startCommand;
    private final InfoCommand infoCommand;
    private final HomeworkSendCommand homeworkSendCommand;
    private final CheckHomeworkCommand checkHomeworkCommand;
    private final FinishedStudentListCommand finishedStudentListCommand;
    private final SendBotDocumentCommand sendBotDocumentCommand;

    public CommandRegistry registerCommands(String botName) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> botName);
        commandRegistry.register(startCommand);
        commandRegistry.register(infoCommand);
        commandRegistry.register(homeworkSendCommand);
        commandRegistry.register(checkHomeworkCommand);
        commandRegistry.register(finishedStudentListCommand);
        commandRegistry.register(sendBotDocumentCommand);
        return commandRegistry;
    }
}
