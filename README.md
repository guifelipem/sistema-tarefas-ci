# Sistema de Tarefas

Projeto simples em Spring Boot usado no Trabalho 5 de Cloud Computing e DevOps Avançado para demonstrar um pipeline de Integração Contínua (CI).

## Tecnologias

- Java 17
- Spring Boot
- Maven
- H2
- GitHub Actions

## Executar o projeto

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

A aplicação fica disponível em `http://localhost:8080`.

## Executar os testes

Windows:

```bash
mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

## Pipeline CI

O arquivo do pipeline está em `.github/workflows/ci.yml`.

O pipeline é executado automaticamente em push ou pull request para a branch `main` e possui as seguintes etapas:

1. Checkout do código
2. Configuração do Java 17
3. Build
4. Execução dos testes
5. Geração do arquivo JAR
6. Upload do JAR como artefato

Se o build ou algum teste falhar, o pipeline é interrompido e a execução fica marcada como falha no GitHub Actions.

O artefato é identificado pelo SHA do commit no nome gerado pelo GitHub Actions.

## Como reproduzir

Clone o repositório e execute:

```bash
./mvnw clean test
./mvnw package -DskipTests
```

No Windows, utilize `mvnw.cmd` no lugar de `./mvnw`.

## Como investigar falhas

Abra a aba **Actions** do repositório, selecione a execução com erro e consulte o log da etapa que falhou.

## Segurança

O projeto não utiliza credenciais externas. Caso sejam necessárias no futuro, devem ser armazenadas em **GitHub Secrets**, e não diretamente no código ou no arquivo do pipeline.

## Evidências para a entrega

Para a entrega, registrar:

- uma execução bem-sucedida do pipeline;
- uma execução em que uma falha de build ou teste seja detectada.

Para demonstrar a falha, pode-se alterar temporariamente um teste para que ele falhe, fazer um push e registrar a execução vermelha. Depois, basta desfazer a alteração e fazer um novo push.
