package com.hubble.data.domain;

public abstract class AbstractObject {

    public enum Type {

          EVENT(0)
        , USER(1)
        , ATTR(4)
        , TEAM(16)
        , DOC(24)
        ;

        private final int code; // код объекта

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Type valueOf(int code) {
            for ( Type type : Type.values() ) {
                if (type.code == code) return type;
            }
            return null;
        }
    }

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
