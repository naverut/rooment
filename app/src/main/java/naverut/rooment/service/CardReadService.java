package naverut.rooment.service;

import android.content.Context;

import java.sql.Timestamp;

import naverut.rooment.bean.MemberNo;
import naverut.rooment.file.bool.MemberStatusBool;
import naverut.rooment.file.dao.RecordDao;
import naverut.rooment.entity.Card;
import naverut.rooment.entity.Record;
import naverut.rooment.enums.Status;

/**
 * カード読み取り時の動作
 */
public class CardReadService {
    // デフォルトコンストラクタ禁止
    private CardReadService(){}

    /**
     * 指定カードを打刻する
     * @param context android用context
     * @param card 読み込んだカード
     * @return カードの持ち主の読み込み後入退室状態
     */
    public static Status writeCard(Context context, Card card) {
        // カードの持ち主の入退室状態を取得
        MemberNo memberNo = card.getMemberNo();
        MemberStatusBool memberStatusBool = new MemberStatusBool(context, memberNo);

        // 入退室状態を反転
        Status status = Status.INTO;
        if (memberStatusBool.isTrue()) {
            status = Status.OUT;
        }

        // 入退室時刻を打刻
        Record record = Record.builder()
                .memberNo(memberNo)
                .date(new Timestamp(System.currentTimeMillis()))
                .status(status)
                .cardId(card.getCardId())
                .build()
                ;
        RecordDao recordDao = new RecordDao(context, RecordDao.fileName);
        recordDao.insert(record);

        // 切り替えたステータスを上書き
        memberStatusBool.setBool(Status.INTO == status);

        return status;
    }
}
