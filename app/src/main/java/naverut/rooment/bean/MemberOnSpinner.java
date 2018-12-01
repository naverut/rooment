package naverut.rooment.bean;

import naverut.rooment.entity.Member;

/**
 * メンバ情報をSpinnerに載せるクラス
 */
public class MemberOnSpinner {
    private final Member member;
    public MemberOnSpinner(Member member) {
        this.member = member;
    }
    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return member.getName();
    }
}
