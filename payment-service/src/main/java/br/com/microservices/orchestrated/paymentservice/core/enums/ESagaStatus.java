package br.com.microservices.orchestrated.paymentservice.core.enums;

import com.sun.net.httpserver.Authenticator;

public enum ESagaStatus {
    SUCCESS,
    ROLLBACK_PENDING,
    FAIL
}
