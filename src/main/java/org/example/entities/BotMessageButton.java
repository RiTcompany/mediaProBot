package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bot_message_button")
@Getter
@Setter
public class BotMessageButton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "button_name")
    private String buttonName;

    @Column(name = "button_link")
    private String buttonLink;

    @Column(name = "bot_message_id")
    private Long botMessageId;
}
