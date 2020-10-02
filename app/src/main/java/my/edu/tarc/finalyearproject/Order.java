package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class Order {
    private String name, phone, address, city, state, date, time,key,email,delivered_time,delivered_date,Uid;
    private double totalAmount;

    public Order() {
    }

    public Order(String name, String phone, String address, String city, String state, String date, String time, double totalAmount, String email, String delivered_time, String delivered_date, String Uid) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
        this.email= email;
        this.delivered_date= delivered_date;
        this.delivered_time= delivered_time;
        this.Uid=Uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getUid() {
        return Uid;
    }

    public String getDelivered_time() {
        return delivered_time;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
