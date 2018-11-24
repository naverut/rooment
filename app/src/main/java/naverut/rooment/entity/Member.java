/*
 * Copyright (c) 2018 Komatsu Ltd. All rights reserved.
 */
package naverut.rooment.entity;


import naverut.rooment.bean.MemberNo;

/**
 * メンバを保持するエンティティ
 * @author naverut
 */
public class Member {
    /** メンバ番号 */
    private final MemberNo memberNo;
    /** メンバ名称 */
    private final String name;


    public MemberNo getMemberNo() {
        return memberNo;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private MemberNo memberNo;
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder memberNo(MemberNo no) {
            this.memberNo = no;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Member(Builder builder) {
        this.memberNo = builder.memberNo;
        this.name = builder.name;
    }
}
