package com.csis231.api.repository;

import com.csis231.api.model.trading_pair;
import org.springframework.data.repository.CrudRepository;

public interface PairRepository extends CrudRepository<trading_pair, Long> {

}
