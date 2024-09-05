# ChunksUpload

ChunksUpload é um projeto Spring Boot para upload de arquivos em pedaços (chunks). Este projeto permite que arquivos grandes sejam enviados em partes menores e depois reconstituídos no servidor.

## Requisitos

- Java 17 ou superior
- Gradle 7.0 ou superior

## Configuração

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu-usuario/chunksupload.git
    cd chunksupload
    ```

2. Configure o diretório de upload no arquivo `src/main/resources/application.properties`:
    ```ini
    spring.application.name=chunksUpload
    server.port=8080
    upload.dir=./src/main/resources/uploads
    ```

## Construção e Execução

Para construir e executar o projeto, utilize os seguintes comandos:

```sh
./gradlew build
./gradlew bootRun
```

Este projeto é uma aplicação Spring Boot para o upload de arquivos em chunks. A aplicação permite o envio de arquivos em partes (chunks) e a conclusão do upload para compor o arquivo final.

## Estrutura do Projeto

- `src/main/java/br/com/chunksupload/ChunksUploadApplication.java`: Classe principal do Spring Boot.
- `src/main/java/br/com/chunksupload/controller/ChunksUploadController.java`: Controlador responsável pelos endpoints de upload.
- `src/main/java/br/com/chunksupload/dto/ChunkUploadDTO.java`: Objeto de Transferência de Dados (DTO) para os dados do upload de chunks.
- `src/main/java/br/com/chunksupload/exception/FileUploadException.java`: Classe de exceção personalizada para erros de upload de arquivos.
- `src/main/java/br/com/chunksupload/service/ChunksUploadService.java`: Interface do serviço de upload de chunks.
- `src/main/java/br/com/chunksupload/impl/ChunksUploadServiceImpl.java`: Implementação do serviço de upload de chunks.
- `src/main/resources/application.properties`: Arquivo de configuração da aplicação.
- `src/test/java/br/com/chunksupload/ChunksUploadApplicationTests.java`: Testes de contexto do Spring Boot.

## Endpoints

### Upload de Chunk

- **URL:** `/upload/chunk`
- **Método:** POST
- **Parâmetros:**
  - `uuid`: Identificador único do upload.
  - `originalName`: Nome original do arquivo.
  - `index`: Índice do chunk.
  - `file`: Arquivo chunk a ser enviado.
- **Resposta:** Mensagem de sucesso ou erro.

### Completar Upload

- **URL:** `/upload/complete`
- **Método:** POST
- **Parâmetros:**
  - `uuid`: Identificador único do upload.
  - `originalName`: Nome original do arquivo.
- **Resposta:** Mensagem de sucesso ou erro.

## Execução

A aplicação estará disponível em [http://localhost:8080](http://localhost:8080).

## Configuração

As configurações da aplicação estão localizadas no arquivo `src/main/resources/application.properties`. Você pode modificar este arquivo para ajustar as configurações da aplicação conforme necessário.

## Contribuição

Se você deseja contribuir para este projeto, sinta-se à vontade para abrir uma issue ou enviar um pull request.

