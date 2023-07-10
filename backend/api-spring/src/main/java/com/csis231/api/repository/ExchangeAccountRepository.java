package com.csis231.api.repository;

import com.csis231.api.model.ExchangeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeAccountRepository extends CrudRepository<ExchangeAccount, Long> {
    ExchangeAccount findByUsername(String username);
}
