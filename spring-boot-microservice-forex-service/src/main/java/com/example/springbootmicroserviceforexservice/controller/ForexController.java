package com.example.springbootmicroserviceforexservice.controller;

import com.example.springbootmicroserviceforexservice.domain.ExchangeValue;
import com.example.springbootmicroserviceforexservice.repository.ExchangeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ForexController {

    private final Environment environment;
    private final ExchangeValueRepository exchangeValueRepository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValues(
            @PathVariable final String from,
            @PathVariable final String to
    ) {
        final ExchangeValue exchangeValue = exchangeValueRepository.findByFromAndTo(from, to);

        exchangeValue.setPort(
                Integer.parseInt(environment.getProperty("local.server.port"))
        );

        return exchangeValue;
    }

}
