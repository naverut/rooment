package naverut.rooment.file.bool;

import android.content.Context;

import naverut.rooment.base.bool.AbstractInfileBool;

/**
 * メンバ編集モード保持用
 * @author naverut
 */
public class EditMemberBool extends AbstractInfileBool {
    /**
     * コンストラクタ
     *
     * @param context android用context
     */
    public EditMemberBool(Context context) {
        super(context);
    }

    @Override
    protected String getFileName() {
        return "editMember.bool";
    }
}
