package naverut.rooment.base.bool;

import android.content.Context;

import java.io.IOException;

import naverut.rooment.util.InFileUtil;

/**
 * android内部ファイルでのbool値永続化
 */
public abstract class AbstractInfileBool implements IBool {
    /**
     * android用context
     */
    private final Context context;

    /**
     * コンストラクタ
     * @param context android用context
     */
    public AbstractInfileBool(Context context) {
        this.context = context;
    }

    /**
     * ファイル名を取得する
     * @return ファイル名
     */
    protected abstract String getFileName();

    @Override
    public void setBool(boolean bool) {
        if (bool) {
            try {
                InFileUtil.write(context, getFileName(), "", false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            InFileUtil.delete(context, getFileName());
        }
    }

    @Override
    public boolean isTrue() {
        return InFileUtil.exists(context, getFileName());
    }
}
