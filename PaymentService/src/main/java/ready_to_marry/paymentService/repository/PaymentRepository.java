package ready_to_marry.paymentService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ready_to_marry.paymentService.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
