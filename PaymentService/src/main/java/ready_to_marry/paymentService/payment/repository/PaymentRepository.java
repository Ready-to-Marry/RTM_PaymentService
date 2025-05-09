package ready_to_marry.paymentService.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ready_to_marry.paymentService.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
