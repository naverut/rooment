package naverut.rooment.file.bool;

import android.content.Context;

import naverut.rooment.base.bool.AbstractInfileBool;
import naverut.rooment.bean.MemberNo;

/**
 * メンバ入退室状態保持用
 * @author naverut
 */
public class MemberStatusBool extends AbstractInfileBool {
    private final MemberNo memberNo;
    /**
     * コンストラクタ
     *
     * @param context android用context
     */
    public MemberStatusBool(Context context, MemberNo memberNo) {
        super(context);
        this.memberNo = memberNo;
    }

    // ファイル名
    private static String fileName = "member_status_{no}.bool";
    @Override
    protected String getFileName() {
        String targetFileName = fileName.replace("{no}", memberNo.toString());
        return targetFileName;
    }
}
