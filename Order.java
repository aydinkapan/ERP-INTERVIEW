 public class Order {
    int orderId;
    int itemId;
    int quantity;
    double unitPrice;

    public Order(int orderId, int itemId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}
