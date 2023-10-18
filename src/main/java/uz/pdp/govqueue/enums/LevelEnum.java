package uz.pdp.govqueue.enums;

import lombok.Getter;

@Getter
public enum LevelEnum {
    MEDIUM(2),
    HARD(3),
    EASY(1);

    private int priority;

    LevelEnum(int priority) {
        this.priority = priority;
    }

}
