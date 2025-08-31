package br.com.microservices.orchestrated.orderservice.core.service;

import java.util.UUID;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.w3c.dom.events.Event;

import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
import br.com.microservices.orchestrated.orderservice.core.producer.SagaProducer;

@Service
@AllArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final EventService eventService;
    private final SagaProducer producer;
    private final JsonUtil jsonUtil;
    private final OrderRepository repository;

    public Order createOrder(OrderRequest orderRequest) {
        var order = Order
            .builder()
            .products(orderRequest.getProducts())
            .createdAt(LocalDateTime.now())
            .transactionId(
                String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
            )
            .build();
        // Persistir order:
        repository.save(order);
        producer.sendEvent(jsonUtil.toJson(createPayload(order)));
        return order;
    }

    private Event createPayload(Order order) {
        var event = Event
            .builder()
            .orderId(order.getId())
            .transactionId(order.getTransactionId())
            .payload(order)
            .createdAt(LocalDateTime.now())
            .build();
        // Persistir event:
        eventService.save(event);
        return event;
    }
}

