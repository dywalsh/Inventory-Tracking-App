package ie.tcd.guineec.inventorymanager;

public class Project {
    private int id;
    private String endDate;
    private String name;
    private User createdBy;

    public Project(int id, String endDate, String name, User createdBy) {
        this.id = id;
        this.endDate = endDate;
        this.name = name;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public User getCreatedBy() {
        return createdBy;
    }
}
