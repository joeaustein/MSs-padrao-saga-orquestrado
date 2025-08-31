package br.com.microservices.orchestrated.orderservice.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String>{

}
