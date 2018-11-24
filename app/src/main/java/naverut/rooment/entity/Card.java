package naverut.rooment.entity;

import naverut.rooment.bean.MemberNo;

/**
 * カードとメンバの紐づけを保持するEntity
 * @author naverut
 */
public class Card {
    /** カードID */
    private final String cardId;
    /** メンバ番号 */
    private final MemberNo memberNo;
    /** 備考 */
    private final String memo;


    public String getCardId() {
        return cardId;
    }

    public MemberNo getMemberNo() {
        return memberNo;
    }

    public String getMemo() {
        return memo;
    }

    public static class Builder {
        private String cardId;
        private MemberNo memberNo;
        private String memo;

        public Builder cardId(String cardId) {
            this.cardId = cardId;
            return this;
        }

        public Builder memberNo(MemberNo memberNo) {
            this.memberNo = memberNo;
            return this;
        }
        public Builder memo(String memo) {
            this.memo = memo;
            return this;
        }


        public Card build() {
            return new Card(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Card(Builder builder) {
        this.cardId = builder.cardId;
        this.memberNo = builder.memberNo;
        this.memo = builder.memo;
    }
}
