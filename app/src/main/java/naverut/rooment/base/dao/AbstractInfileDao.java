package naverut.rooment.base.dao;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import naverut.rooment.constant.Constant;
import naverut.rooment.util.InFileUtil;

/**
 * android内部用ファイルDAO
 * @author naverut
 */
public abstract class AbstractInfileDao<T> implements IDao<T> {
    // android用context
    private final Context context;

    /**
     * コンストラクタ
     * @param context android用context
     */
    public AbstractInfileDao(Context context) {
        this.context = context;
    }


    /**
     * ファイルパス取得
     * @return 取得するファイルパス
     */
    protected abstract String getFileName();

    /**
     * エンティティ作成
     * @param separate ファイル内の行データを分割したもの
     * @return 行から作成したエンティティ
     **/
    protected abstract T create(String[] separate);

    /**
     *  ファイルの行を取得する
     * @param t エンティティ
     * @return エンティティを行にしたString
     */
    protected abstract String toLine(T t);

    /**
     * エンティティのキー値を取得する
     * @param t エンティティ
     * @return エンティティのキー値
     */
    protected abstract Object getEntityKey(T t);

    @Override
    public final T selectById(Object key) {
        if (key == null) {
            return null;
        }

        try {
            List<String> lines = InFileUtil.readAllLines(context, getFileName());
            for (String line : lines) {
                String[] separate = line.split(Constant.SPLIT);
                T t = create(separate);
                if (key.equals(getEntityKey(t))) {
                    return t;
                }
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final List<T> select(Object key) {
        List<T> list = new ArrayList<>();

        if (key == null) {
            return list;
        }

        try {
            List<String> lines = InFileUtil.readAllLines(context, getFileName());
            for (String line : lines) {
                String[] separate = line.split(Constant.SPLIT);
                T t = create(separate);
                if (key.equals(getEntityKey(t))) {
                    list.add(t);
                }
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final List<T> selectAll() {
        String fileName = getFileName();

        List<T> list = new ArrayList<>();
        try {
            List<String> lines = InFileUtil.readAllLines(context, fileName);
            for (String line : lines) {
                String[] separate = line.split(Constant.SPLIT);
                list.add(this.create(separate));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public final int insert(T t) {
        try {
            InFileUtil.write(context, getFileName(), toLine(t) + '\n', true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public final int update(T t) {
        int updateCnt = 0;
        try {
            // ファイル行を読み、指定エンティティに一致したものを置き換えてlistに追加
            Object key = getEntityKey(t);
            StringBuilder fileData = new StringBuilder();

            List<String> lines = InFileUtil.readAllLines(context, getFileName());
            for (String line : lines) {
                String[] separate = line.split(Constant.SPLIT);
                T createT = create(separate);
                // キーに一致すれば渡されたデータ、そうでなければそのまま
                if (key.equals(getEntityKey(createT))) {
                    fileData.append(toLine(t));
                    updateCnt++;
                } else {
                    fileData.append(line);
                }
                fileData.append('\n');
            }

            // 各行データを上書き
            InFileUtil.write(context, getFileName(), fileData.toString(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return updateCnt;
    }
}
