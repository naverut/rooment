package naverut.rooment.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイル系Util
 */
public class InFileUtil {
    // デフォルトコンストラクタ禁止
    private InFileUtil(){}

    /**
     * ファイルの存在チェック
     * @param context android用context
     * @param fileName 読み込むファイル名
     * @return
     */
    public static boolean exists(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        boolean exists = file.exists();
        return exists;
    }

    /**
     * ファイルを削除する
     * @param context android用context
     * @param fileName 削除ファイル名
     * @return 削除結果
     */
    public static boolean delete(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        boolean del = file.delete();
        return del;
    }

    /**
     * ファイル内全行読み込み
     * @param context android用context
     * @param fileName 読み込むファイル名
     * @return ファイルの各行をリストにしたもの
     * @throws IOException ファイルエラー
     */
    public static List<String> readAllLines(Context context, String fileName) throws IOException {
        // 返却用List
        List<String> lines = new ArrayList<>();

        // ファイル存在チェック
        if (!exists(context, fileName)) {
            return lines;
        }

        // ファイル読み取り
        try (
            FileInputStream fio = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fio);
            BufferedReader reader = new BufferedReader(isr);
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    /**
     * ファイルへ書き込み
     * @param context android用context
     * @param fileName 書き込むファイル名
     * @param str 書き込む文字
     * @param append true:追記／false:上書き
     * @throws IOException ファイルエラー
     */
    public static void write(Context context, String fileName, String str, boolean append) throws IOException {
        // 書き込みモード確定
        int mode = Context.MODE_PRIVATE;
        if(append) {
            mode = Context.MODE_APPEND;
        }

        // ファイル書き込み
        try (
            OutputStream fos = context.openFileOutput(fileName, mode);
            PrintWriter writer = new PrintWriter(fos);
        ) {
            writer.write(str);
            writer.flush();
        }
    }

    /**
     * ファイルをコピーする。コピー先が存在する場合は上書きする
     * @param context android用context
     * @param orgFileName コピー元ファイル名
     * @param dstFileName コピー先ファイル名
     * @return コピー成功
     * @throws IOException ファイルエラー
     */
    public static boolean copy(Context context, String orgFileName, String dstFileName) throws IOException {
        // ファイル存在チェック
        if (!exists(context, orgFileName)) {
            return false;
        }

        // ファイルコピー
        try (
            FileInputStream fio = context.openFileInput(orgFileName);
            OutputStream fos = context.openFileOutput(dstFileName, Context.MODE_PRIVATE);
        ) {
            FileChannel inChannel = fio.getChannel();
            FileChannel outChannel = ((FileOutputStream) fos).getChannel();
            long pos = 0;
            while (pos < inChannel.size()) {
                pos += inChannel.transferTo(pos, inChannel.size(), outChannel);
            }
        }
        return true;
    }
}
