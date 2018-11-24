package naverut.rooment.entity;

import java.sql.Timestamp;

import naverut.rooment.bean.MemberNo;
import naverut.rooment.enums.Status;

/**
 * カード読み込み時刻のエンティティ
 * @author naverut
 */
public class Record {
    /** メンバ番号 */
    private final MemberNo memberNo;
    /** カード読み込み時刻 */
    private final Timestamp date;
    /** カード読み込み後入退室状態 */
    private final Status status;
    /** カード番号 */
    private final String cardId;


    public MemberNo getMemberNo() {
        return memberNo;
    }

    public Timestamp getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public String getCardId() {
        return cardId;
    }

    public static class Builder {
        private MemberNo memberNo;
        private Timestamp date;
        private Status status;
        private String cardId;


        public Builder memberNo(MemberNo memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public Builder date(Timestamp date) {
            this.date = date;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder cardId(String cardId) {
            this.cardId = cardId;
            return this;
        }

        public Record build() {
            return new Record(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Record(Builder builder) {
        this.memberNo = builder.memberNo;
        this.date = builder.date;
        this.status = builder.status;
        this.cardId = builder.cardId;
    }
}
