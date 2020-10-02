package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class Cart {
    private String pid, pname, discount, key;
    private double pprice;
    private int quantity;

    public Cart() {
    }

    public Cart(String pid, String pname, double pprice, int quantity, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public double getPprice() {
        return pprice;
    }

    public void setPprice(double pprice) {
        this.pprice = pprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public double getTotalPrice(double i, double j){
        double oneTPTotalPrice = i*j;
        double totalPrice = 0;
        totalPrice += oneTPTotalPrice;
        return totalPrice;
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
