package naverut.rooment.base.bool;

/**
 * bool値を永続化する
 * @author naverut
 */
public interface IBool {
    /**
     * bool値を設定する
     * @param bool bool値
     */
    void setBool(boolean bool);

    /**
     * trueか判定する
     * @return true/false
     */
    boolean isTrue();
}
