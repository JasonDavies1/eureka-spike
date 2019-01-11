package com.example.springbootmicroserviceforexservice.repository;

import com.example.springbootmicroserviceforexservice.domain.ExchangeValue;
import org.apache.el.lang.ELArithmetic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {

    ExchangeValue findByFromAndTo(String from, String to);

}
