import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class FeeTest {
    @Test
    public void testFeeStatus() {
        Student s = new Student(1, "Doe", "John", Instant.parse("2023-09-01T00:00:00Z"));
        Fee fee = new Fee(1, "Scolarité", 1000.0, Instant.parse("2025-09-10T00:00:00Z"), s);

        // Aucun paiement
        assertEquals(FeeStatus.NULL, fee.getStatusAt(Instant.parse("2025-09-04T00:00:00Z")));

        // Paiement partiel avant deadline
        fee.addPayment(new Payment(1, 400.0, Instant.parse("2025-09-03T00:00:00Z")));
        assertEquals(FeeStatus.IN_PROGRESS, fee.getStatusAt(Instant.parse("2025-09-04T00:00:00Z")));

        // Paiement total avant deadline
        fee.addPayment(new Payment(2, 600.0, Instant.parse("2025-09-04T00:00:00Z")));
        assertEquals(FeeStatus.PAID, fee.getStatusAt(Instant.parse("2025-09-04T00:00:00Z")));

        // Paiement en surplus
        fee.addPayment(new Payment(3, 100.0, Instant.parse("2025-09-05T00:00:00Z")));
        assertEquals(FeeStatus.OVERPAID, fee.getStatusAt(Instant.parse("2025-09-05T00:00:00Z")));

        // Paiement partiel après deadline
        Fee lateFee = new Fee(2, "Bibliothèque", 500.0, Instant.parse("2025-09-01T00:00:00Z"), s);
        lateFee.addPayment(new Payment(4, 200.0, Instant.parse("2025-09-02T00:00:00Z")));
        assertEquals(FeeStatus.LATE, lateFee.getStatusAt(Instant.parse("2025-09-04T00:00:00Z")));
    }
}