package naverut.rooment.file.dao;

import android.content.Context;

import naverut.rooment.base.dao.AbstractInfileDao;
import naverut.rooment.entity.Password;

/**
 * パスワード用DAO
 * @auther naverut
 */
public final class PasswordDao extends AbstractInfileDao<Password> {
    // ファイル名
    public static String fileName = "password.txt";

    /**
     * コンストラクタ
     * @param context android用context
     */
    public PasswordDao(Context context) {
        super(context);
    }

    @Override
    protected final String getFileName() {
        return fileName;
    }


    @Override
    protected String toLine(Password password) {
        return password.getPassword();
    }

    @Override
    protected Object getEntityKey(Password password) {
        return password.getPassword();
    }

    @Override
    protected final Password create(String[] separate) {
        Password password = Password.builder().password(separate[0]).build();

        return password;
    }
}
