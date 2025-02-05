# Insurance API 🚀

A **Insurance API** é um sistema RESTful desenvolvido para o gerenciamento de apólices de seguro e processamento de pagamentos. Utiliza **Spring Boot**, **PostgreSQL** e **Apache Kafka** para garantir escalabilidade e processamento assíncrono de arquivos CSV.

## 📌 Funcionalidades Implementadas

### **Gerenciamento de Apólices**
- Criar uma nova apólice (`POST /apolices`)
- Buscar uma apólice por ID (`GET /apolices/{id}`)
- Listar todas as apólices (`GET /apolices`)

### **Processamento de Pagamentos**
- Enviar pagamento de parcela via Kafka (`POST /parcelas/{id}/processar-pagamento`)
- Notificação via Kafka ao concluir pagamento

### **Importação de Apólices via CSV**
- Upload de arquivo CSV (`POST /csv/upload`)
- Processamento assíncrono via Kafka
- Persistência das apólices no banco de dados

### **Arquitetura e Padrões**
- Aplicação dos princípios **SOLID** para melhor organização do código
- **Respostas RESTful apropriadas**, garantindo boas práticas no retorno de requisições (`200 OK`, `201 Created`, `400 Bad Request`, etc.)
- **Logging estruturado** para rastreamento de transações e depuração eficiente
- **Swagger UI** para documentação automática da API

---

## ⚙️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Web, JPA, Validation**
- **PostgreSQL + HikariCP**
- **Apache Kafka** para comunicação assíncrona
- **Swagger (Springdoc OpenAPI)**

---

## 🏗 Instalação e Configuração

### **1. Clone o repositório**
```
git clone https://github.com/seu-usuario/insurance-api.git
cd insurance-api
```

### **2. Suba os serviços com Docker**
```
docker-compose up -d --build
```
Isso inicia os serviços do **PostgreSQL** e **Apache Kafka** automaticamente.

### **3. Execute a aplicação local (não há necessidade caso use o Docker**
```sh
mvn spring-boot:run
```

A API estará disponível em:
```
http://localhost:8080
```

A documentação do **Swagger** pode ser acessada em:
```
http://localhost:8080/swagger-ui.html
```