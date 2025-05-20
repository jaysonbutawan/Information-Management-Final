package org.example.Object;

public class Property_Type {
    private int type_id;
    private String type_name;
    private String description;

    public Property_Type(int type_id, String type_name, String description) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.description = description;
    }

    public int getType_id() {
        return type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type_name;
    }
}

