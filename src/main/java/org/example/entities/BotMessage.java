package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EBotMessage;

@Entity
@Table(name = "bot_message")
@Getter
@Setter
public class BotMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "writer_id")
    private Long writerId;

    @Enumerated(EnumType.STRING)
    private EBotMessage status;
}
