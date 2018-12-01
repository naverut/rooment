package naverut.rooment.service;

import android.content.Context;

import java.io.IOException;

import naverut.rooment.entity.RecordHistory;
import naverut.rooment.file.dao.RecordDao;
import naverut.rooment.file.dao.RecordHistoryDao;
import naverut.rooment.util.InFileUtil;

/**
 * 打刻履歴ファイルサービス
 */
public class RecordHistoryService {
    // デフォルトコンストラクタ禁止
    private RecordHistoryService(){}

    /**
     * 打刻履歴を作成する
     * @param historyMemo 履歴メモ
     */
    public static void backupRecord(Context context, String historyMemo) {
        // バックアップ先ファイル名を決定(時刻を取得して一意にする)
        long currentTime = System.currentTimeMillis();
        String historyFileName = "record_" + currentTime + ".txt";


        // 打刻ファイルをコピー
        String orgFileName = RecordDao.fileName;
        try {
            InFileUtil.copy(context, orgFileName, historyFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 打刻バックアップ履歴を保存
        RecordHistory recordHistory = RecordHistory.builder()
                .historyFileName(historyFileName)
                .historyMemo(historyMemo)
                .build();
        RecordHistoryDao recordHistoryDao = new RecordHistoryDao(context);
        recordHistoryDao.insert(recordHistory);

        // 元ファイルを削除
        InFileUtil.delete(context, orgFileName);
    }
}
