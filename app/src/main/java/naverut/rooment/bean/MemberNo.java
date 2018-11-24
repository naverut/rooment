/*
 * Copyright (c) 2018 Komatsu Ltd. All rights reserved.
 */
package naverut.rooment.bean;

/**
 * メンバ番号を保持するエンティティ
 * 外部メンバも追加するなど番号形態の変化を吸収するためのクラス
 */
public class MemberNo {
    /** メンバ番号 */
    private final String memberNo;

    public String getMemberNo() {
        return memberNo;
    }

    @Override
    public String toString() {
        return memberNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MemberNo)) {
            return false;
        }

        MemberNo target = (MemberNo) obj;
        return memberNo.equals(target.memberNo);
    }

    @Override
    public int hashCode() {
        return memberNo.hashCode();
    }

    public static class Builder {
        private String memberNo;

        public Builder memberNo(String memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public MemberNo build() {
            return new MemberNo(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private MemberNo(Builder builder) {
        this.memberNo = builder.memberNo;
    }
}
