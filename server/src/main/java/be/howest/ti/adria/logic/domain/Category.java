package be.howest.ti.adria.logic.domain;

import java.io.Serializable;

public class Category implements Serializable {
    private final String name;
    private int id;

    public Category(String name) {
        this.name = name;
    }
    public Category(int id, String name) {
        this(name);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return id;
    }
}

