package naverut.rooment.service;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import naverut.rooment.bean.MemberNo;
import naverut.rooment.entity.Member;
import naverut.rooment.entity.Record;
import naverut.rooment.enums.Status;
import naverut.rooment.file.dao.MemberDao;
import naverut.rooment.file.dao.RecordDao;

/**
 * カード読み取り時の動作
 */
public class RecordService {
    // デフォルトコンストラクタ禁止
    private RecordService(){}

    /**
     * 打刻履歴文字を取得する
     * @param context android用context
     * @return 打刻履歴文字
     */
    public static String getRecordString(Context context) {
        // メンバ番号とメンバ名称を保持しておく
        MemberDao memberDao = new MemberDao(context);
        List<Member> memberList = memberDao.selectAll();
        Map<MemberNo, String> memberMap = new HashMap<>();
        for (Member member : memberList) {
            memberMap.put(member.getMemberNo(), member.getName());
        }

        // 打刻履歴を取得
        RecordDao recordDao = new RecordDao(context);
        List<Record> recordList = recordDao.selectAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);

        // 履歴を文字列化
        StringBuilder recordStr = new StringBuilder();
        for (Record record : recordList) {
            // 名称を取得
            String memberName = memberMap.get(record.getMemberNo());
            if (memberName == null) {
                memberName = "未登録者：No." + record.getMemberNo();
            }

            // 時刻を取得
            String date = sdf.format(record.getDate());

            // 入退室状態
            String statusStr = "入室";
            if (record.getStatus() == Status.OUT) {
                statusStr = "退室";
            }

            // 文字取得
            recordStr.append(memberName);
            recordStr.append(',');
            recordStr.append(date);
            recordStr.append(',');
            recordStr.append(statusStr);
            recordStr.append('\n');
        }

        return recordStr.toString();
    }
}
