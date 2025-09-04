import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeeManagementTest {
    private Student student;
    private Fee fee;
    private Instant now;
    private Instant pastDeadline;
    private Instant futureDeadline;

    @BeforeEach
    void setUp() {
        student = new Student(1, "Jean", "Dupont", Instant.now());
        now = Instant.now();
        pastDeadline = now.minusSeconds(3600); // 1 heure avant
        futureDeadline = now.plusSeconds(3600); // 1 heure après
    }

    @Test
    void testFeeStatusNull() {
        fee = new Fee(1, "Frais de scolarité", 1000.0, futureDeadline, student);
        assertEquals(FeeStatus.NULL, fee.getStatus(now));
    }

    @Test
    void testFeeStatusInProgress() {
        fee = new Fee(1, "Frais de scolarité", 1000.0, futureDeadline, student);
        fee.addPayment(new CashPayment(1, 500.0, now));
        assertEquals(FeeStatus.IN_PROGRESS, fee.getStatus(now));
    }

    @Test
    void testFeeStatusPaid() {
        fee = new Fee(1, "Frais de scolarité", 1000.0, futureDeadline, student);
        fee.addPayment(new CashPayment(1, 1000.0, now));
        assertEquals(FeeStatus.PAID, fee.getStatus(now));
    }

    @Test
    void testFeeStatusLate() {
        fee = new Fee(1, "Frais de scolarité", 1000.0, pastDeadline, student);
        fee.addPayment(new CashPayment(1, 500.0, now));
        assertEquals(FeeStatus.LATE, fee.getStatus(now));
    }

    @Test
    void testFeeStatusOverpaid() {
        fee = new Fee(1, "Frais de scolarité", 1000.0, futureDeadline, student);
        fee.addPayment(new CashPayment(1, 1500.0, now));
        assertEquals(FeeStatus.OVERPAID, fee.getStatus(now));
    }

    @Test
    void testGetLateFees() {
        Statistics stats = new Statistics();
        Fee fee1 = new Fee(1, "Frais 1", 1000.0, pastDeadline, student);
        fee1.addPayment(new CashPayment(1, 500.0, now));
        Fee fee2 = new Fee(2, "Frais 2", 1000.0, futureDeadline, student);
        fee2.addPayment(new CashPayment(2, 500.0, now));
        List<Fee> fees = Arrays.asList(fee1, fee2);

        List<Fee> lateFees = stats.getLateFees(fees, now);
        assertEquals(1, lateFees.size());
        assertEquals(fee1, lateFees.get(0));
    }

    @Test
    void testGetTotalMissingFees() {
        Statistics stats = new Statistics();
        Fee fee1 = new Fee(1, "Frais 1", 1000.0, pastDeadline, student);
        fee1.addPayment(new CashPayment(1, 500.0, now));
        Fee fee2 = new Fee(2, "Frais 2", 1000.0, pastDeadline, student);
        fee2.addPayment(new CashPayment(2, 200.0, now));
        List<Fee> fees = Arrays.asList(fee1, fee2);

        double totalMissing = stats.getTotalMissingFees(fees, now);
        assertEquals(1300.0, totalMissing, 0.001);
    }

    @Test
    void testGetTotalPaidByStudent() {
        Statistics stats = new Statistics();
        Fee fee1 = new Fee(1, "Frais 1", 1000.0, futureDeadline, student);
        fee1.addPayment(new CashPayment(1, 500.0, now.minusSeconds(7200)));
        Fee fee2 = new Fee(2, "Frais 2", 1000.0, futureDeadline, student);
        fee2.addPayment(new CashPayment(2, 300.0, now));
        List<Fee> fees = Arrays.asList(fee1, fee2);

        double totalPaid = stats.getTotalPaidByStudent(student, fees, now);
        assertEquals(800.0, totalPaid, 0.001);
    }
}