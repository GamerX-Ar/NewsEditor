package com.hubble.data.domain;

import org.json.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class News extends AbstractVersionObject implements Comparable<News> {

    public enum Type {

          PRIVATE(8)
        , PUBLIC(9)
        ;

        private final int code;

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

    public enum Status {

          CREATED(1)
        , FOR_APPROVE(2)
        , PUBLISHED(3)
        , REJECTED(4)
        ;

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Status valueOf(int code) {
            for ( Status status : Status.values() ) {
                if (status.code == code) return status;
            }
            return null;
        }
    }

    private Long authorId;          //    R_AUTHOR_ID    NUMBER default 1,
    private Integer secNo;          //    SEC_NO         NUMBER not null,
    private Integer type;           //    D_TYPE         NUMBER not null,
    private Integer status;         //    D_STATUS       NUMBER default 0,
    private Integer seasonCode;     //    SEASON_CODE    NUMBER,
    private String title;           //    TITLE          VARCHAR2(4000),
    private String smallBody;       //    SMALL_BODY     VARCHAR2(4000),
    private String body;            //    BODY           CLOB,
    private String icon;            //    ICON           VARCHAR2(4000) default 'ksi_logo.png' not null


    /**
     * Create a domain object from a json object.
     *
     * @param news this class instance as a JSON object.
     * @throws InvocationTargetException see {@link java.lang.reflect.Method#invoke}
     * @throws IllegalAccessException see {@link java.lang.reflect.Method#invoke}
     * @throws IllegalArgumentException see {@link java.lang.reflect.Method#invoke}
     */
    public News(JSONObject news) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        for (Method m : this.getClass().getMethods()) {
            if ( m.getName().startsWith("set") ) {
                String fieldName = m.getName().substring(3);
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                if ( news.has(fieldName) && !news.getString(fieldName).trim().isEmpty() ) {
                    if ( Integer.class.isAssignableFrom(m.getParameterTypes()[0]) ) {
                        m.invoke(this, news.getInt(fieldName));
                    } else if (String.class.isAssignableFrom(m.getParameterTypes()[0])) {
                        m.invoke(this, news.getString(fieldName));
                    }
                }
            }
        }
    }

    /**
     * Create an entity.
     * @param id Identifier.
     */
    public News(Long id) {
        setId(id);
    }

    /**
     * Create a domain object.
     * The object identifier is default and equals to 0.
     * Objects with a default id is not an entity and is transient.
     */
    public News() {
        setId(0L);
    }

    public void setFd(String fd) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        setFd(Timestamp.valueOf(LocalDateTime.parse(fd, formatter)));
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getSecNo() {
        return secNo;
    }

    public void setSecNo(Integer secNo) {
        this.secNo = secNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(Integer seasonCode) {
        this.seasonCode = seasonCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallBody() {
        return smallBody;
    }

    public void setSmallBody(String smallBody) {
        this.smallBody = smallBody;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(News o) {
        return Long.compare(o.getId(), this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        // if the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        // if obj is not an instance of KsiNews or "null instanceof [type]" then return false
        if (!(obj instanceof News)) {
            return false;
        }

        // typecast obj to KsiNews so that we can compare data members
        News news = (News) obj;

        // this is a condition to compare of two entities, object with default id is not an entity
        if (this.getId() == 0 || news.getId() == 0) {
            return false;
        }

        // compare the data members and return
        return (news.getId().equals(this.getId()));
    }

}
