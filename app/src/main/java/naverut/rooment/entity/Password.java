/*
 * Copyright (c) 2018 Komatsu Ltd. All rights reserved.
 */
package naverut.rooment.entity;


/**
 * パスワードを保持するエンティティ
 * @author naverut
 */
public class Password {
    /** パスワード */
    private final String password;



    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String password;

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Password build() {
            return new Password(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Password(Builder builder) {
        this.password = builder.password;
    }
}
