package naverut.rooment.file.dao;

import android.content.Context;

import naverut.rooment.base.dao.AbstractInfileDao;
import naverut.rooment.constant.Constant;
import naverut.rooment.entity.RecordHistory;

/**
 * 打刻履歴用DAO
 */
public final class RecordHistoryDao extends AbstractInfileDao<RecordHistory> {
    // ファイル名
    public static final String fileName = "recordHistory.txt";

    /**
     * コンストラクタ
     * @param context android用context
     */
    public RecordHistoryDao(Context context) {
        super(context);
    }

    @Override
    protected final String getFileName() {
        return fileName;
    }


    @Override
    protected final RecordHistory create(String[] separate) {
        RecordHistory recordHistory = RecordHistory.builder()
                .historyFileName(separate[0])
                .historyMemo(separate[1])
                .build();

        return recordHistory;
    }

    @Override
    protected String toLine(RecordHistory recordHistory) {
        return recordHistory.getHistoryFileName() + Constant.SPLIT
                + recordHistory.getHistoryMemo();
    }

    @Override
    protected Object getEntityKey(RecordHistory recordHistory) {
        return recordHistory.getHistoryFileName();
    }
}
