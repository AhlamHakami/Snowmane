package com.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
//mapping the table by java
public class Command {
    @Id
    private Long id;
    private int direct;
    private String name;
    private String time;
    private Long child;

    public Command(Long id, int direct, String name, String time, Long child) {
        this.id = id;
        this.direct = direct;
        this.name = name;
        this.time = time;
        this.child = child;
    }

    public Command() {
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getChild() {
        return child;
    }

    public void setChild(Long child) {
        this.child = child;
    }


}
