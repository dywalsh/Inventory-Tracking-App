package ie.tcd.guineec.inventorymanager;

public class Individual {
    private int id;
    private String name;
    private User createdBy;

    public Individual(int id, String name, User createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreatedBy() {
        return createdBy;
    }
}
