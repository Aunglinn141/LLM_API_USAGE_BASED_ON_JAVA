# LLM API Usage Based on Java

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![API](https://img.shields.io/badge/API-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)

A comprehensive Java-based application that demonstrates different approaches to integrating Large Language Model (LLM) APIs. This project showcases four distinct implementation methods for consuming LLM services in Java applications.

## 🚀 Features

- **Multiple Implementation Methods**: Four different approaches to LLM API integration
- **RESTful API**: Clean REST endpoints for LLM interactions
- **Interactive Documentation**: Swagger UI for API exploration
- **Spring Boot Framework**: Modern Java application structure
- **Comprehensive Examples**: Real-world usage scenarios
- **Error Handling**: Robust error management and logging

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 11+**: JDK 11 or higher
- **Maven 3.6+**: For dependency management and build
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

## 🛠️ Installation

### 1. Clone the Repository
```bash
git clone https://github.com/Aunglinn141/LLM_API_USAGE_BASED_ON_JAVA.git
cd LLM_API_USAGE_BASED_ON_JAVA
```

### 2. Configure Dependencies
```bash
mvn clean install
```

### 3. Environment Setup
Create an `application.properties` file in `src/main/resources/`:

```properties
# Server Configuration
server.port=8123

# LLM API Configuration
llm.api.key=your_api_key_here
llm.api.base-url=https://api.openai.com/v1
llm.api.timeout=30000

# Documentation
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/api/doc.html
```

## 🎯 Quick Start

### 1. Run the Application
```bash
mvn spring-boot:run
```

Or run the main class:
```bash
java -jar target/LLM_API_USAGE_BASED_ON_JAVA-1.0.0.jar
```

### 2. Access the API Documentation
Open your browser and navigate to:
```
http://localhost:8123/api/doc.html
```

### 3. Test the Endpoints
The application will be available at:
```
http://localhost:8123
```

## 🔧 API Endpoints

### Core Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/health` | Health check endpoint |
| `POST` | `/api/v1/chat/completion` | Chat completion using Method 1 |
| `POST` | `/api/v1/chat/stream` | Streaming chat using Method 2 |
| `POST` | `/api/v1/embeddings` | Text embeddings using Method 3 |
| `POST` | `/api/v1/analyze` | Text analysis using Method 4 |

### Example Request
```bash
curl -X POST http://localhost:8123/api/v1/chat/completion \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello, how are you?",
    "model": "gpt-3.5-turbo",
    "temperature": 0.7
  }'
```

### Example Response
```json
{
  "id": "chat-123456",
  "model": "gpt-3.5-turbo",
  "response": "Hello! I'm doing well, thank you for asking. How can I help you today?",
  "usage": {
    "prompt_tokens": 12,
    "completion_tokens": 18,
    "total_tokens": 30
  },
  "created": 1640995200
}
```

## 🏗️ Implementation Methods

This project demonstrates four different approaches to LLM API integration:

### Method 1: Direct HTTP Client
- Uses Java's built-in HTTP client
- Synchronous request/response pattern
- Simple implementation for basic use cases

### Method 2: Reactive Streams
- WebFlux-based reactive implementation
- Handles streaming responses
- Non-blocking I/O operations

### Method 3: Third-Party Libraries
- Integration with popular Java LLM libraries
- Abstracted API calls
- Enhanced error handling

### Method 4: Custom Wrapper Service
- Custom service layer implementation
- Advanced features like retry logic
- Metrics and monitoring integration

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── llmapi/
│   │               ├── YuAiAgentApplication.java
│   │               ├── controller/
│   │               │   ├── ChatController.java
│   │               │   ├── EmbeddingController.java
│   │               │   └── AnalysisController.java
│   │               ├── service/
│   │               │   ├── LLMService.java
│   │               │   ├── Method1Service.java
│   │               │   ├── Method2Service.java
│   │               │   ├── Method3Service.java
│   │               │   └── Method4Service.java
│   │               ├── model/
│   │               │   ├── ChatRequest.java
│   │               │   ├── ChatResponse.java
│   │               │   └── ApiResponse.java
│   │               └── config/
│   │                   ├── LLMConfig.java
│   │                   └── SwaggerConfig.java
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
    └── java/
        └── com/
            └── example/
                └── llmapi/
                    └── LLMServiceTest.java
```

## 🔐 Configuration

### API Key Setup
Set your LLM provider API key:

```bash
export LLM_API_KEY="your-api-key-here"
```

Or add it to your `application.properties`:
```properties
llm.api.key=${LLM_API_KEY:your-default-key}
```

### Supported LLM Providers
- OpenAI GPT models
- Anthropic Claude
- Google PaLM
- Hugging Face models
- Custom API endpoints

## 📊 Usage Examples

### Chat Completion
```java
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    
    @Autowired
    private LLMService llmService;
    
    @PostMapping("/completion")
    public ResponseEntity<ChatResponse> chatCompletion(@RequestBody ChatRequest request) {
        ChatResponse response = llmService.generateCompletion(request);
        return ResponseEntity.ok(response);
    }
}
```

### Streaming Response
```java
@PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> streamChat(@RequestBody ChatRequest request) {
    return llmService.streamCompletion(request);
}
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Manual Testing
Use the Swagger UI at `http://localhost:8123/api/doc.html` to test endpoints interactively.

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   Error: Port 8123 is already in use
   Solution: Change the port in application.properties or kill the process using the port
   ```

2. **API Key Invalid**
   ```bash
   Error: 401 Unauthorized
   Solution: Verify your API key is correctly set in the configuration
   ```

3. **Connection Timeout**
   ```bash
   Error: Connection timed out
   Solution: Check network connectivity and increase timeout values
   ```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Java naming conventions
- Use proper documentation
- Include unit tests for new features
- Maintain consistent formatting

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [OpenAI](https://openai.com/) - LLM API provider
- [Swagger](https://swagger.io/) - API documentation
- [Maven](https://maven.apache.org/) - Build automation

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/Aunglinn141/LLM_API_USAGE_BASED_ON_JAVA/issues) page
2. Create a new issue with detailed description
3. Contact the maintainer: [Aunglinn141](https://github.com/Aunglinn141)

## 🚀 Future Enhancements

- [ ] Add more LLM provider integrations
- [ ] Implement caching mechanisms
- [ ] Add authentication and authorization
- [ ] Create Docker containerization
- [ ] Add monitoring and metrics
- [ ] Implement rate limiting
- [ ] Add batch processing capabilities

---

⭐ **If you found this project helpful, please give it a star!** ⭐
