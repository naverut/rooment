package naverut.rooment.entity;

/**
 * 履歴ファイル名と名称の紐づけを保持するEntity
 */
public class RecordHistory {
    /** 履歴ファイル名 */
    private final String historyFileName;
    /** 履歴メモ */
    private final String historyMemo;


    public String getHistoryFileName() {
        return historyFileName;
    }

    public String getHistoryMemo() {
        return historyMemo;
    }

    public static class Builder {
        private String historyFileName;
        private String historyMemo;

        public Builder historyFileName(String historyFileName) {
            this.historyFileName = historyFileName;
            return this;
        }

        public Builder historyMemo(String historyMemo) {
            this.historyMemo = historyMemo;
            return this;
        }


        public RecordHistory build() {
            return new RecordHistory(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private RecordHistory(Builder builder) {
        this.historyFileName = builder.historyFileName;
        this.historyMemo = builder.historyMemo;
    }
}
