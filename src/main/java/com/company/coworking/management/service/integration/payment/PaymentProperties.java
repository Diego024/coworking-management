package com.company.coworking.management.service.integration.payment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integration.payment-gateway")
public class PaymentProperties {
    private String url;
}
