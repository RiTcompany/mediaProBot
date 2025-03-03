package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageNavigation {
    PREV("◀"),
    NEXT("▶");

    private final String value;

    EPageNavigation(String value) {
        this.value = value;
    }

}
