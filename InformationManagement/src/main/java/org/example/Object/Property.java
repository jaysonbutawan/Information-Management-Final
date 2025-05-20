package org.example.Object;

public class Property {
    private int id;
    private String property_name;
    private String address;
    private float size;
    private double rent_price;
    private int type_id;
    private String description;

    public Property() {
    }

    public Property(String property_name, String address, float size, double rent_price, int type_id, String description) {
        this.property_name = property_name;
        this.address = address;
        this.size = size;
        this.rent_price = rent_price;
        this.type_id = type_id;
        this.description = description;


    }

    public String getProperty_name() {
        return property_name;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getSize() {

        return size;
    }

    public String getDescription() {
        return description;
    }

    public void setSize(float size) {

        this.size = size;
    }

    public double getRent_price() {
        return rent_price;
    }

    public void setRent_price(double rent_price) {
        this.rent_price = rent_price;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

