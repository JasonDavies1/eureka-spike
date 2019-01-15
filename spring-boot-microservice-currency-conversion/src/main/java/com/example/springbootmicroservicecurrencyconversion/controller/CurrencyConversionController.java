package com.example.springbootmicroservicecurrencyconversion.controller;

import com.example.springbootmicroservicecurrencyconversion.domain.CurrencyConversion;
import com.example.springbootmicroservicecurrencyconversion.exception.ServiceException;
import com.example.springbootmicroservicecurrencyconversion.proxy.CurrencyExchangeServiceProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CurrencyConversionController {

    private final CurrencyExchangeServiceProxy proxy;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrency(
            @PathVariable final String from,
            @PathVariable final String to,
            @PathVariable final BigDecimal quantity
    ) {
        final Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        final ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
                uriVariables);

        final CurrencyConversion response = responseEntity.getBody();

        return new CurrencyConversion(
                response.getId(),
                from,
                to,
                response.getConversionMultiple(),
                quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort());
    }

    @HystrixCommand(fallbackMethod = "errorResponse")
    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyFeign(
            @PathVariable final String from,
            @PathVariable final String to,
            @PathVariable final BigDecimal quantity
    ) {
        final CurrencyConversion response = proxy.retrieveExchangeValue(from, to);

        logger.info("{}", response);

        return new CurrencyConversion(
                response.getId(),
                from,
                to,
                response.getConversionMultiple(),
                quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort()
        );
    }

    private CurrencyConversion errorResponse(
            final String from,
            final String to,
            final BigDecimal quantity
    ) throws ServiceException {
        throw new ServiceException(String.format("Sorry, no conversion found from %s to %s", from, to));
    }

}
