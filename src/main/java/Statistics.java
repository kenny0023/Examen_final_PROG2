import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public List<Fee> getLateFees(List<Fee> fees, Instant t) {
        List<Fee> lateFees = new ArrayList<>();
        for (Fee fee : fees) {
            if (fee.getStatus(t) == FeeStatus.LATE) {
                lateFees.add(fee);
            }
        }
        return lateFees;
    }

    public double getTotalMissingFees(List<Fee> fees, Instant t) {
        return getLateFees(fees, t).stream()
                .mapToDouble(fee -> fee.getAmountDue() - fee.getPayments().stream().mapToDouble(Payment::getAmount).sum())
                .sum();
    }

    public double getTotalPaidByStudent(Student student, List<Fee> fees, Instant t) {
        return fees.stream()
                .filter(fee -> fee.getStudent().getId() == student.getId())
                .flatMap(fee -> fee.getPayments().stream())
                .filter(payment -> payment.getDateTime().isBefore(t) || payment.getDateTime().equals(t))
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}