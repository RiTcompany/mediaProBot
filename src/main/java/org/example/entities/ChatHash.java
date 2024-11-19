package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;

@Entity
@Table(name = "chat_hash")
@Getter
@Setter
public class ChatHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "e_conversation")
    private EConversation eConversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "e_conversation_step")
    private EConversationStep eConversationStep;

    @Column(name = "prev_bot_message_id")
    private Integer prevBotMessageId;

    @Column(name = "prev_bot_message_page_number")
    private Integer prevBotMessagePageNumber;

    @Column(name = "chat_id")
    private Long chatId;

    public void setDefaultPrevBotMessagePageNumber() {
        prevBotMessagePageNumber = 0;
    }

}
