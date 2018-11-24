package naverut.rooment.file.dao;

import android.content.Context;

import java.sql.Timestamp;

import naverut.rooment.base.dao.AbstractInfileDao;
import naverut.rooment.bean.MemberNo;
import naverut.rooment.constant.Constant;
import naverut.rooment.entity.Record;
import naverut.rooment.enums.Status;

/**
 * カード読み込み時刻用DAO
 * @naverut
 */
public final class RecordDao extends AbstractInfileDao<Record> {
    // ファイル名
    public static String fileName = "record.txt";

    /**
     * コンストラクタ
     * @param context android用context
     */
    public RecordDao(Context context) {
        super(context);
    }

    @Override
    protected final String getFileName() {
        return fileName;
    }


    @Override
    protected final Record create(String[] separate) {
        MemberNo memberNo = MemberNo.builder().memberNo(separate[0]).build();
        Timestamp times = Timestamp.valueOf(separate[1]);
        Status st = Status.of(separate[2]);
        String cardId = separate[3];

        Record record = Record.builder()
                .memberNo(memberNo)
                .date(times)
                .status(st)
                .cardId(cardId)
                .build();

        return record;
    }

    @Override
    protected String toLine(Record record) {
        return record.getMemberNo().toString() + Constant.SPLIT
                + record.getDate().toString() + Constant.SPLIT
                + record.getStatus().getValue() + Constant.SPLIT
                + record.getCardId();
    }

    @Override
    protected Object getEntityKey(Record record) {
        return record.getMemberNo();
    }
}
