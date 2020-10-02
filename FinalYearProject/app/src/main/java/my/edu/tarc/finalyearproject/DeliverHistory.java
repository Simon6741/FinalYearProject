package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class DeliverHistory {

    private String address, date, time, email, name, pid, key;

    public DeliverHistory() {
    }

    public DeliverHistory(String address, String date, String time, String email, String name, String pid) {
        this.address = address;
        this.date = date;
        this.time = time;
        this.email = email;
        this.name = name;
        this.pid = pid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
