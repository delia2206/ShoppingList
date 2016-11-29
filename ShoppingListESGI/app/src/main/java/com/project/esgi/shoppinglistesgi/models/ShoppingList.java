package com.project.esgi.shoppinglistesgi.models;

import java.util.Date;

/**
 * Created by delia on 29/11/2016.
 */
public class ShoppingList {
    private Integer id;
    private String name;
    private Date created_date;
    private Boolean completed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
