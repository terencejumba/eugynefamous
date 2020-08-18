package quinton.terence.eugynefamous.Model;

public class AdminOrders {

    private String name, phone, state, home, address, totalAmount, method, mpesaPhone, date, time;

    public AdminOrders() {
    }

    public AdminOrders(String name, String phone, String state, String home, String address, String totalAmount, String method, String mpesaPhone, String date, String time) {
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.home = home;
        this.address = address;
        this.mpesaPhone = mpesaPhone;
        this.totalAmount = totalAmount;
        this.method = method;
        this.date = date;
        this.time = time;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMpesaPhone() {
        return mpesaPhone;
    }

    public void setMpesaPhone(String mpesaPhone) {
        this.mpesaPhone = mpesaPhone;
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
}
