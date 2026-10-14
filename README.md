# Sistema de Tarefas

Projeto simples em Spring Boot desenvolvido para o Trabalho 5 de Cloud Computing e DevOps Avançado, com o objetivo de demonstrar um pipeline de Integração Contínua (CI).

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
./mvnw spring-boot:run
```

A aplicação fica disponível em:

```text
http://localhost:8080
```

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

O pipeline está definido no arquivo:

```text
.github/workflows/ci.yml
```

Ele é executado automaticamente a cada `push` ou `pull request` realizado na branch `main`.

As etapas são:

1. Checkout do código
2. Configuração do Java 17
3. Build da aplicação
4. Execução dos testes
5. Geração do arquivo JAR
6. Upload do JAR como artefato

Caso o build ou algum teste falhe, o pipeline é interrompido automaticamente e a execução é marcada como falha no GitHub Actions.

O artefato gerado é identificado pelo SHA do commit.

## Como investigar falhas

Para investigar uma falha, basta acessar a aba **Actions** do repositório, selecionar a execução que apresentou erro e abrir o log da etapa que falhou.

Os logs permitem identificar em qual etapa o problema ocorreu e qual foi o erro encontrado.

## Segurança

O projeto não utiliza credenciais externas.

Caso fossem necessárias credenciais ou tokens, eles deveriam ser armazenados utilizando **GitHub Secrets**, sem serem incluídos diretamente no código-fonte ou no arquivo do pipeline.

# Evidências

## Execução bem-sucedida

Foi realizada uma execução completa do pipeline com o código e os testes em seu estado correto.

Todas as etapas foram concluídas com sucesso:

- Checkout
- Configuração do Java
- Build
- Testes
- Geração do JAR
- Upload do artefato

A execução ficou marcada em verde no GitHub Actions, demonstrando que o projeto pode ser compilado, testado e empacotado automaticamente.

### Evidência

![Pipeline executado com sucesso]
<img width="1920" height="851" alt="image" src="https://github.com/user-attachments/assets/889212ce-ff50-4b07-a02a-783586e07985" />


---

## Detecção de falha

Também foi realizada uma execução propositalmente incorreta para verificar se o pipeline seria capaz de detectar uma falha nos testes e interromper o processo.

O teste utilizado foi:

```java
@Test
void deveRetornar404AoEditarAusente() throws Exception {
    mvc.perform(
            put("/api/tarefas/99999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "titulo": "CI",
                              "status": "PENDENTE"
                            }
                            """)
            )
            .andExpect(status().isNotFound());
}
```

O comportamento correto da aplicação é retornar o status HTTP **404 Not Found** ao tentar editar uma tarefa inexistente.

Para provocar uma falha controlada, a expectativa do teste foi temporariamente alterada de:

```java
.andExpect(status().isNotFound());
```

para:

```java
.andExpect(status().isOk());
```

Assim, o teste passou a esperar o status HTTP **200 OK**, enquanto a aplicação continuou retornando corretamente **404 Not Found**.

O GitHub Actions identificou a divergência:

```text
Status expected:<200> but was:<404>

Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
```

Com a falha detectada na etapa **Testes**, o pipeline foi interrompido. Por isso, as etapas seguintes de geração do JAR e salvamento do artefato não foram executadas.

Isso demonstra que o pipeline impede que uma versão com testes falhando avance no processo.

### Evidência

![Pipeline interrompido após falha nos testes]
<img width="1919" height="854" alt="image" src="https://github.com/user-attachments/assets/cca52bfd-6ab3-48e3-97b8-0aefabb716bc" />


Após registrar a evidência, o teste foi restaurado para sua condição correta:

```java
.andExpect(status().isNotFound());
```

Dessa forma, o repositório final permanece com todos os testes funcionando corretamente.
