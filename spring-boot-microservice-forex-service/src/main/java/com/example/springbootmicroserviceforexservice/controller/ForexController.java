package com.example.springbootmicroserviceforexservice.controller;

import com.example.springbootmicroserviceforexservice.domain.ExchangeValue;
import com.example.springbootmicroserviceforexservice.exception.NoExchangeException;
import com.example.springbootmicroserviceforexservice.repository.ExchangeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ForexController {

    private final Environment environment;
    private final ExchangeValueRepository exchangeValueRepository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValues(
            @PathVariable final String from,
            @PathVariable final String to
    ) throws NoExchangeException {
        final Optional<ExchangeValue> exchangeValue = Optional.ofNullable(exchangeValueRepository.findByFromAndTo(from, to));

        if (exchangeValue.isPresent()){
            final ExchangeValue result = exchangeValue.get();
            result.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return result;
        } else {
            throw new NoExchangeException(String.format("No exchange rate found from %s to %s", from, to));
        }
    }

}
