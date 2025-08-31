package br.com.microservices.orchestrated.orderservice.core.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import br.com.microservices.orchestrated.orderservice.core.document.Event;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;

    private Event saveEvent(Event event) {
        return repository.save(event);
    }
}

