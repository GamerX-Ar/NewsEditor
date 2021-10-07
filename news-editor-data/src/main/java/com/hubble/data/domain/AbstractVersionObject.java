package com.hubble.data.domain;

import java.sql.Timestamp;

public abstract class AbstractVersionObject extends AbstractObject {

    protected Timestamp fd;
    protected Timestamp td;

    public Timestamp getFd() {
        return fd;
    }

    public void setFd(Timestamp fd) {
        this.fd = fd;
    }

    public Timestamp getTd() {
        return td;
    }

    public void setTd(Timestamp td) {
        this.td = td;
    }

}
