package quinton.terence.eugynefamous.Model;

public class cart {

    private String pid, pname, price, quantity, discount, color, size, category, sex;

    public cart() {
    }

    public cart(String pid, String pname, String price, String quantity, String discount, String color, String size, String category, String sex) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.color = color;
        this.size = size;
        this.category = category;
        this.sex = sex;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
