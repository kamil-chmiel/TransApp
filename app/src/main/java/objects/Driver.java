package objects;


import java.util.List;

public class Driver extends Worker {

    private List<Order> myOrders;

    public List<Order> getOrders() {
        return myOrders;
    }

    public void setOrders(List<Order> orders) {
        this.myOrders = orders;
    }
}
