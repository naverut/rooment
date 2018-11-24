package naverut.rooment.enums;

/**
 * 入退室状態
 */
public enum Status {
    /** 入室 */
    INTO("1"),
    /** 退室 */
    OUT("2"),
    ;

    private final String value;

    /**
     * 保存値を取得する
     * @return 保存値
     */
    public String getValue() {
        return value;
    }

    Status(String value) {
        this.value = value;
    }

    /**
     * String値から取得。一致がない場合はOUTとする
     */
    public static Status of(String value) {
        for (Status e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return OUT;
    }
}
