package com.hubble.data.domain;


public class User extends AbstractVersionObject {

    public enum Role {

          JUDGE(1)
        , EDITOR(2)
        , ADMIN(4)
        ;

        private final int code;

        Role( int code )
        {
            this.code = code;
        }

        public long getCode()
        {
            return code;
        }

        public static Role valueOf(int code) {
            for ( Role role : Role.values() ) {
                if (role.code == code) return role;
            }
            return null;
        }
    }

    private String firstname;
    private String lastname;
    private String middlename;
    private String nickname;
    private String email;
    private String phone;
    private Integer country;
    private Integer region;
    private Integer roles;

    /**
     *
     *
     * @param n — Идентификатор участника в системе.
     * @param email — Его адрес электронной.
     * @param roles — Его доступы к разделам сайта.
     */
    public User(long n, String email, Integer roles) {
        setId(n);
        this.email = email;
        this.roles = roles;
    }

    /**
     *
     *
     * @param n — Идентификатор участника в системе.
     * @param email — Его адрес электронной.
     */
    public User(long n, String email) {
        setId(n);
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getRoles() {
        return roles;
    }

    public void setRoles(Integer roles) {
        this.roles = roles;
    }

    public boolean hasRole(Role role) {
        return (this.roles & role.getCode()) == role.getCode();
    }

}
