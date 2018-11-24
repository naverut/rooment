package naverut.rooment.file.bool;

import android.content.Context;

import naverut.rooment.base.bool.AbstractInfileBool;

/**
 * 管理者モード保持用
 * @author naverut
 */
public class AdminModeBool extends AbstractInfileBool {
    /**
     * コンストラクタ
     *
     * @param context android用context
     */
    public AdminModeBool(Context context) {
        super(context);
    }

    @Override
    protected String getFileName() {
        return "adminMode.bool";
    }
}
