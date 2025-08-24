package br.com.microservices.orchestrated.paymentservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import br.com.microservices.orchestrated.paymentservice.core.enums.ESagaStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String source;
    private ESagaStatus status;
    private String message;
    private LocalDateTime createdAt;
}
