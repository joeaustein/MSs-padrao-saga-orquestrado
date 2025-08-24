package br.com.microservices.orchestrated.productvalidationservice.core.enums;

import com.sun.net.httpserver.Authenticator;

public class ESagaStatus {
    SUCCESS,
    ROLLBACK_PENDING,
    FAIL
}
