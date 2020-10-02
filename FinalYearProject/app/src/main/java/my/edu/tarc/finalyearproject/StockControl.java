package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class StockControl {

    private String ID,name,date,description,pid,time,key;
    private int availableStock;
    public StockControl() {
    }

    public StockControl(String ID, String date, String description, String pid, String time, int availableStock) {
        this.ID = ID;
        this.name = name;
        this.date = date;
        this.description = description;
        this.pid = pid;
        this.time = time;
        this.availableStock = availableStock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
