package naverut.rooment.service;

import android.content.Context;

import naverut.rooment.file.dao.PasswordDao;
import naverut.rooment.entity.Password;

/**
 * パスワードチェックサービス
 */
public class PasswordService {
    // デフォルトコンストラクタ禁止
    private PasswordService(){}

    /**
     * パスワードとチェック用パスワードが一致する場合、ハッシュ化したパスワードを書き込む
     * @param context android用context
     * @param password パスワード
     * @param passwordCheck チェック用パスワード
     * @return true:パスワードをファイルに書き込んだ／パスワードとパスワードチェックが一致しない
     */
    public static boolean writePassword(Context context, String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            return false;
        }

        // パスワードをハッシュ化
        String hashPassword = String.valueOf(password.hashCode());

        // パスワード書き込み
        Password passwordEntity = Password.builder().password(hashPassword).build();
        PasswordDao passwordDao = new PasswordDao(context);
        passwordDao.insert(passwordEntity);

        return true;
    }

    /**
     * パスワードのチェックを行う
     * @param context android用context
     * @param password パスワード
     * @return パスワードの一致
     */
    public static boolean checkPassword(Context context, String password) {
        // パスワードをハッシュ化
        String hashPassword = String.valueOf(password.hashCode());

        // 登録されているパスワードと一致するか確認
        PasswordDao passwordDao = new PasswordDao(context);
        Password passwordEntity = passwordDao.selectById(hashPassword);

        // 一致状態を返却
        return (passwordEntity != null);
    }
}
