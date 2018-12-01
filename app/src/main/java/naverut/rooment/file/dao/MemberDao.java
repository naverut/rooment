package naverut.rooment.file.dao;

import android.content.Context;

import naverut.rooment.base.dao.AbstractInfileDao;
import naverut.rooment.bean.MemberNo;
import naverut.rooment.constant.Constant;
import naverut.rooment.entity.Member;

/**
 * メンバ用DAO
 */
public final class MemberDao extends AbstractInfileDao<Member> {
    // ファイル名
    public static final String fileName = "member.txt";

    /**
     * コンストラクタ
     * @param context android用context
     */
    public MemberDao(Context context) {
        super(context);
    }

    @Override
    protected final String getFileName() {
        return fileName;
    }

    @Override
    protected String toLine(Member member) {
        return member.getMemberNo().toString() + Constant.SPLIT
                + member.getName();
    }

    @Override
    protected Object getEntityKey(Member member) {
        return member.getMemberNo();
    }

    @Override
    protected final Member create(String[] separate) {
        MemberNo memberNo = MemberNo.builder().memberNo(separate[0]).build();
        Member member = Member.builder()
                .memberNo(memberNo)
                .name(separate[1])
                .build();

        return member;
    }
}
