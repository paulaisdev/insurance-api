# 📌 Insurance API

API para gerenciamento de apólices de seguro, incluindo processamento de arquivos CSV e integração com Kafka para processamento assíncrono de pagamentos.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Kafka**
- **Docker**
- **OpenCSV**

## 📂 Estrutura do Projeto

```
insurance-api/
│-- src/
│   ├── main/
│   │   ├── java/com/insurance/api/
│   │   │   ├── controller/  # Controllers da API
│   │   │   ├── service/      # Lógica de negócios
│   │   │   ├── model/        # Entidades do banco
│   │   │   ├── repository/   # Repositórios JPA
│   │   │   ├── messaging/    # Integração com Kafka
│   │   │   ├── utils/        # Utilitários e validações
│   │   ├── resources/
│   │   │   ├── application.yml  # Configuração do Spring Boot
│-- docker-compose.yml  # Configuração do ambiente Docker
│-- Insurance API.postman_collection.json  # Collection do Postman para testes
│-- README.md
```

## 🔧 Como Rodar o Projeto

### 1️⃣ Pré-requisitos:
- **Docker** e **Docker Compose**
- **JDK 17**
- **Maven**

### 2️⃣ Subir os serviços
```
docker-compose up -d
```

### 3️⃣ Rodar a aplicação localmente
```
mvn spring-boot:run
```

A API estará disponível em:  
👉 `http://localhost:8080`

## 📌 Endpoints Principais

### 🏢 **Apólices**
- `GET /apolices` → Lista todas as apólices
- `GET /apolices/{id}` → Busca uma apólice por ID
- `POST /apolices` → Cria uma nova apólice
- `PUT /apolices/{id}` → Atualiza uma apólice
- `DELETE /apolices/{id}` → Remove uma apólice

### 📂 **Upload de CSV**
- `POST /csv/upload` → Envia um arquivo CSV para processamento

### 💳 **Pagamentos**
- `POST /parcelas/{id}/processar-pagamento?formaPagamento=CARTAO`  
  → Envia um pagamento para processamento via Kafka

## 🧪 Testes com Postman
Recomenda-se o uso da collection do Postman para testar a API:

1. **Importar a collection** localizada no projeto:
   ```
   ./Insurance API.postman_collection.json
   ```
2. **Configurar o ambiente** (se necessário).
3. **Executar os testes**.
