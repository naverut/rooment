package naverut.rooment.base.dao;

import java.util.List;

/**
 * DataAccessObjectインタフェース
 * @param <T> 入出力するエンティティ
 */
public interface IDao<T> {
    /**
     * キーに一致する最初のエンティティを取得する
     * @param key キー値
     * @return キーに一致した最初のエンティティ。一致がない場合はNULL
     */
    T selectById(Object key);

    /**
     * キーに一致するすべてのエンティティを取得する
     * @param key キー値
     * @return キーに一致したすべてのエンティティ。一致がない場合は空のリスト
     */
    List<T> select(Object key);

    /**
     * すべてのエンティティを取得する
     * @return すべてのエンティティ。エンティティがない場合は空のリスト
     */
    List<T> selectAll();

    /**
     * エンティティを追記する
     * @param t 追記するエンティティ
     * @return 追記した数(通常は1)
     */
    int insert(T t);

    /**
     * 指定エンティティのキー値に一致するものを更新する
     * @param t 更新したいエンティティ
     * @return 更新数
     */
    int update(T t);
}
