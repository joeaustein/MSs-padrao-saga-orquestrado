#!/bin/bash

# Lista de aplicações
applications=(
    "order-service"
    "orchestrator-service"
    "product-validation-service"
    "payment-service"
    "inventory-service"
)

# Total de aplicações
total_apps=${#applications[@]}

# Função para construir aplicação
build_application() {
    app="$1"
    index="$2"
    
    echo "------------------------------------------------------------"
    echo "[BUILD] ($index/$total_apps) Iniciando build da aplicação: $app"
    echo "------------------------------------------------------------"

    # Rodando gradle com logs mínimos
    (cd "$app" && ./gradlew clean build -x test --no-daemon --quiet)

    if [ $? -eq 0 ]; then
        echo "[BUILD] Aplicação $app finalizada com sucesso!"
    else
        echo "[BUILD][ERRO] Falha ao buildar $app"
        exit 1
    fi
}

# Função para subir os containers
docker_compose_up() {
    echo "------------------------------------------------------------"
    echo "[DOCKER] Subindo containers com Docker Compose"
    echo "------------------------------------------------------------"
    docker-compose up --build -d

    if [ $? -ne 0 ]; then
        echo "[DOCKER][ERRO] Falha ao subir containers!"
        exit 1
    fi

    echo "[DOCKER] Containers em execução, validando serviços..."

    # Validação simples: checar se cada container está rodando
    for app in "${applications[@]}"; do
        if docker ps --format '{{.Names}}' | grep -q "$app"; then
            echo "[VALIDAÇÃO] Serviço $app está rodando!"
        else
            echo "[VALIDAÇÃO][ERRO] Serviço $app não está rodando!"
            exit 1
        fi
    done

    echo "------------------------------------------------------------"
    echo "[DOCKER] Todos os serviços estão em execução com sucesso!"
    echo "------------------------------------------------------------"
}

# Função para remover containers restantes
remove_remaining_containers() {
    echo "------------------------------------------------------------"
    echo "[DOCKER] Removendo containers antigos"
    echo "------------------------------------------------------------"
    docker-compose down -v
    echo "[DOCKER] Containers antigos removidos."
    echo "------------------------------------------------------------"
}

# Limpeza de resíduos do Docker
docker_cleanup() {
    echo "------------------------------------------------------------"
    echo "[DOCKER] Limpando recursos não utilizados"
    echo "------------------------------------------------------------"
    docker image prune -f        # remove imagens dangling (<none>)
    docker volume prune -f       # remove volumes órfãos
    docker network prune -f      # remove redes não utilizadas
    docker system prune -f       # remove tudo que não está sendo usado (sem mexer nos ativos)
    echo "[DOCKER] Limpeza concluída!"
    echo "------------------------------------------------------------"
}

# Função para buildar todas as aplicações
build_all_applications() {
    echo "------------------------------------------------------------"
    echo "[BUILD] Iniciando build de todas as aplicações"
    echo "------------------------------------------------------------"
    i=0
    for app in "${applications[@]}"; do
        i=$((i+1))
        build_application "$app" "$i"
    done
    echo "[BUILD] Todas as aplicações foram buildadas com sucesso!"
    echo "------------------------------------------------------------"
}

# Execução do pipeline
echo "============================================================"
echo "[PIPELINE] Pipeline iniciado!"
echo "============================================================"
build_all_applications
remove_remaining_containers
docker_cleanup
docker_compose_up
echo "============================================================"
echo "[PIPELINE] Pipeline concluído com sucesso!"
echo "============================================================"
