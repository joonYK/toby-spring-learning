package user.domain;

public enum Level {
    BASIC(1, 2),

    SILVER(2, 3),

    GOLD(3, null);

    private final int value;

    //다음 단계로 등급이 올라갈때의 레벨.
    private final Integer nextLevelValue;

    public int intValue() {
        return value;
    }

    public Level nextLevel() {
        if(nextLevelValue == null)
            return null;

        return valueOf(nextLevelValue);
    }

    Level(int value, Integer nextLevelValue) {
        this.value = value;
        this.nextLevelValue = nextLevelValue;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown value: " + value);
        }
    }
}
