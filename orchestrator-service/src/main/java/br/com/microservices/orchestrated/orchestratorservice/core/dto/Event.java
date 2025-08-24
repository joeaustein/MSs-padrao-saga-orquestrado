package br.com.microservices.orchestrated.orchestratorservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String id;
    private String transactionId;
    private String orderId;
    private Order payload;
    private EEventSource source;
    private ESagaStatus status;
    private List<History> eventHistory;
    private LocalDateTime createdAt;
}
