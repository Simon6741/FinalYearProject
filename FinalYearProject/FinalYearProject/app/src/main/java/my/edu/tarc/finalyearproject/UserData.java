package my.edu.tarc.finalyearproject;

public class UserData {
    String Name,Address,Password,Email;

    public UserData (){

    }

    public UserData(String name, String address, String password, String email) {
        Name = name;
        Address = address;
        Password = password;
        Email = email;
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
}
