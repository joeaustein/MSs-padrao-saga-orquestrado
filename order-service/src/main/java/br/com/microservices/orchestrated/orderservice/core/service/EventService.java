package br.com.microservices.orchestrated.orderservice.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.config.exception.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository repository;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        // Loga o final de toda a saga:
        log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());  
    }

    public List<Event> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters filters) {
        validateEmptyFilters(filters);
        if (!StringUtils.isEmpty(filters.getOrderId())) {
            return findByOrderId(filters.getOrderId());
        } else {
            return findByTransactionId(filters.getTransactionId());
        }
    }

    private Event findByOrderId(String orderId) {
        return repository
            .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
            .orElseThrow(() -> new ValidationException("Event not found by orderID!"));
    }

    private Event findByTransactionId(String transactionId) {
        return repository
            .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
            .orElseThrow(() -> new ValidationException("Event not found by transactionID!"));
    }

    private void validateEmptyFilters(EventFilters filters) {
        if (StringUtils.isEmpty(filters.getOrderId()) && StringUtils.isEmpty(filters.getTransactionId())) {
            throw new ValidationException("OrderID or TransactionID must be informed!");
        }
    }

    public Event save(Event event) {
        return repository.save(event);
    }
}

