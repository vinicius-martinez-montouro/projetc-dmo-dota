package com.ifsp.dmo.dotaapplication.model;

import java.io.Serializable;

/**
 * @author vinicius.montouro
 */
public class BaseEntity implements Serializable {

    protected String name;

    public BaseEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
