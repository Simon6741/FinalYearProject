package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class ImageUpload {

    private String id;
    private String name;
    private String url;
    private String key;
    private String desc;
    private double price;
    private String category;
    private int availableStock;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public String getUrl() {

        return url;
    }

    public String getDesc() {
        return desc;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public ImageUpload(String id, String name, String url, String desc, double price, String category, int availableStock) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.price = price;
        this.category = category;
        this.availableStock = availableStock;
    }

    public ImageUpload(){

    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
