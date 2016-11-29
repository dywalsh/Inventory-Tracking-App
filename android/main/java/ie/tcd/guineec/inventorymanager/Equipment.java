package ie.tcd.guineec.inventorymanager;

public class Equipment {
    private int id;
    private String barcode;
    private Project projectUsing;
    private Individual indResponsible;
    private String description;
    private User createdBy;

    public Equipment(int id, String barcode, Project projectUsing, Individual indResponsible, String description, User createdBy) {
        this.id = id;
        this.barcode = barcode;
        this.projectUsing = projectUsing;
        this.indResponsible = indResponsible;
        this.description = description;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public Project getProjectUsing() {
        return projectUsing;
    }

    public Individual getIndResponsible() {
        return indResponsible;
    }

    public String getDescription() {
        return description;
    }

    public User getCreatedBy() {
        return createdBy;
    }
}