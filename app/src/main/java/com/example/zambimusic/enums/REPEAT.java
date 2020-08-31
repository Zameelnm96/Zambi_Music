package com.example.zambimusic.enums;

public enum REPEAT {
    REPEAT_ALL,
    NO_REPEAT,
    REPEAT_ONE;
    public static REPEAT toREPEAT (String enumString) {
        try {
            return valueOf(enumString);
        } catch (Exception ex) {
            // For error cases
            return NO_REPEAT;
        }
    }
}
