package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RecurringPaymentsRepository extends CrudRepository<RecurringPayment, Long> {

    @Query(value = "select * from recurring_payments rp where rp.active = 1 "
                    + "and rp.next_execution_time = CURDATE()",
        nativeQuery = true)
    List<RecurringPayment> findAllForExecution();

    List<RecurringPayment> findAllByWalletId(Long walletId);

    Optional<RecurringPayment> findByIdAndWalletId(Long id, Long walletId);

}
