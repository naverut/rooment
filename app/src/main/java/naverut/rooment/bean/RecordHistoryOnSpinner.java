package naverut.rooment.bean;

import naverut.rooment.entity.RecordHistory;

/**
 * 打刻履歴をSpinnerに載せるクラス
 */
public class RecordHistoryOnSpinner {
    private final RecordHistory recordHistory;

    public RecordHistoryOnSpinner(RecordHistory recordHistory) {
        this.recordHistory = recordHistory;
    }

    public RecordHistory getRecordHistory() {
        return recordHistory;
    }

    @Override
    public String toString() {
        return this.recordHistory.getHistoryMemo();
    }
}
