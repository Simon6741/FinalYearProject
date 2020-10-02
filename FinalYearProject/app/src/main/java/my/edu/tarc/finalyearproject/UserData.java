package my.edu.tarc.finalyearproject;

import com.google.firebase.database.Exclude;

public class UserData {
    private String Name,Address,Password,Email,Phone,key;

    public UserData (){

    }

    public UserData(String name, String password, String email, String address, String phone) {
        Name = name;
        Address = address;
        Password = password;
        Email = email;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
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


