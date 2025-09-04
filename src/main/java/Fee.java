import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Fee {
    private int id;
    private String label;
    private double amountDue;
    private Instant deadline;
    private Student student;
    private List<Payment> payments = new ArrayList<>();

    public Fee(int id, String label, double amountDue, Instant deadline, Student student) {
        this.id = id;
        this.label = label;
        this.amountDue = amountDue;
        this.deadline = deadline;
        this.student = student;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public List<Payment> getPayments() { return payments; }
    public double getAmountDue() { return amountDue; }
    public Instant getDeadline() { return deadline; }
    public Student getStudent() { return student; }

    public double getTotalPaidUpTo(Instant t) {
        return payments.stream()
            .filter(p -> !p.getDateTime().isAfter(t))
            .mapToDouble(Payment::getAmount)
            .sum();
    }

    public FeeStatus getStatusAt(Instant t) {
        double totalPaid = getTotalPaidUpTo(t);
        if (totalPaid == 0) return FeeStatus.NULL;
        if (totalPaid > amountDue) return FeeStatus.OVERPAID;
        if (totalPaid == amountDue) return FeeStatus.PAID;
        if (t.isAfter(deadline)) return FeeStatus.LATE;
        return FeeStatus.IN_PROGRESS;
    }

    public FeeStatus getStatus(Instant instant) {
        double totalPaid = payments.stream().mapToDouble(Payment::getAmount).sum();

        if (totalPaid == 0) {
            return FeeStatus.NULL;
        }
        if (totalPaid > amountDue) {
            return FeeStatus.OVERPAID;
        }
        if (totalPaid == amountDue) {
            return FeeStatus.PAID;
        }
        if (instant.isAfter(deadline)) {
            return FeeStatus.LATE;
        }
        return FeeStatus.IN_PROGRESS;
    }
}