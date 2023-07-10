package com.csis231.api.repository;

import com.csis231.api.model.Trades;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trades, Long> {
    Trades save(Trades trade);

}
