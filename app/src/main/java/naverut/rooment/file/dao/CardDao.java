package naverut.rooment.file.dao;

import android.content.Context;

import naverut.rooment.base.dao.AbstractInfileDao;
import naverut.rooment.bean.MemberNo;
import naverut.rooment.constant.Constant;
import naverut.rooment.entity.Card;

/**
 * カード用DAO
 */
public final class CardDao extends AbstractInfileDao<Card> {
    // ファイル名
    public static final String fileName = "card.txt";

    /**
     * コンストラクタ
     * @param context android用context
     */
    public CardDao(Context context) {
        super(context);
    }

    @Override
    protected final String getFileName() {
        return fileName;
    }

    @Override
    protected final Card create(String[] separate) {
        MemberNo memberNo = MemberNo.builder().memberNo(separate[1]).build();
        Card card = Card.builder()
                .cardId(separate[0])
                .memberNo(memberNo)
                .memo(separate[2])
                .build();

        return card;
    }

    @Override
    protected String toLine(Card card) {
        return card.getCardId() + Constant.SPLIT
                + card.getMemberNo().toString() + Constant.SPLIT
                + card.getMemo();
    }

    @Override
    protected Object getEntityKey(Card card) {
        return card.getCardId();
    }
}
