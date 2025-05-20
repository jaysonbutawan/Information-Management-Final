package org.example.Object;

public class PropertyDetails {
    private int id;
    private String name;
    private String address;
    private float size;
    private double rent;
    private String typeName;
    private String description;
    private String status;

    public PropertyDetails(int id, String name, String address, float size, double rent, String typeName, String description, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.size = size;
        this.rent = rent;
        this.typeName = typeName;
        this.description = description;
        this.status = status;
    }

    // Getters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public float getSize() { return size; }
    public double getRent() { return rent; }
    public String getTypeName() { return typeName; }
    public String getDescription() { return description; }
}

