#!/bin/bash

# Lista de aplicações
applications=(
    "order-service"
    "orchestrator-service"
    "product-validation-service"
    "payment-service"
    "inventory-service"
)

# Função para construir aplicação
build_application() {
    app="$1"
    echo "Building application $app"
    (cd "$app" && gradle build -x test)
    echo "Application $app finished building!"
}

# Função para subir os containers
docker_compose_up() {
    echo "Running containers!"
    docker-compose up --build -d
    echo "Pipeline finished!"
}

# Função para remover containers restantes
remove_remaining_containers() {
    echo "Removing all containers."
    docker-compose down
    containers=$(docker ps -aq)

    if [ -n "$containers" ]; then
        echo "There are still containers created: $containers"
        for container in $containers; do
            echo "Stopping container $container"
            docker container stop "$container"
        done
        docker container prune -f
    fi
}

# Função para buildar todas as aplicações em paralelo
build_all_applications() {
    echo "Starting to build applications!"
    for app in "${applications[@]}"; do
        build_application "$app" &
    done
    wait
}

# Execução do pipeline
echo "Pipeline started!"
build_all_applications
remove_remaining_containers
docker_compose_up
