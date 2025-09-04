import java.time.Instant;

public class Payment {
    private int id;
    private double amount;
    private Instant dateTime;

    public Payment(int id, double amount, Instant dateTime) {
        this.id = id;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public Instant getDateTime() {
        return dateTime;
    }
}