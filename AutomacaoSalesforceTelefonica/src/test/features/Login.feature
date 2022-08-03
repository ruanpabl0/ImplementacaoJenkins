#language: pt
@Regressao
Funcionalidade: Login

  @CT01
  Delineação do Cenário:: ativacao
    Dado que inicializei os drivers para o <id>
    E Que ao acessar o site do salesforce
    Quando clicar em assista a demonstracao
    E visualizar a tela seguinte

    Exemplos:
      | id      | 
      | "Login" | 