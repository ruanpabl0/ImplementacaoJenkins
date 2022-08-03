----------------------------------------------------------
               Requisitos da automação
----------------------------------------------------------

É necessário realizar a instalação dos componentes abaixo:

Versão 11 ou superior do Java Development Kit (JDK) - Link para Download: https://www.oracle.com/java/technologies/javase-downloads.html
Pacote 2019-12 do Eclipse - Link para Download: https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2019-12/R/eclipse-jee-2019-12-R-win32-x86_64.zip
Versão 87 do navegador Google Chrome - Link para Download: https://www.google.com/intl/pt-BR/chrome/
Git - Link para Download: https://git-scm.com/download/win

Plugin do Cucumber
Passo a Passo:
 1 - Fazer o download da versão 11 do JDK a partir do link indicado acima. 
 2 - Executar o arquivo do JDK baixado e seguir com a instalação até o fim. 
 3 - Em pesquisar, buscar por Editar as variáveis de ambiente do Sistema e abrir a opção exibida. 
 4 - No Popup de Propriedades do Sistema, selecionar a opção Variáveis de Ambiente... 
 5 - No Popup de Variáveis de Ambiente, no quadro Variáveis do Sistema, selecionar a opção Novo... 
 6 - No Popup Nova Variáveis de Sistema, preencher o campo Nome da variável com JAVA_HOME e preencher o campo valor da variável com o diretório de instalação do JDK* e clicar em OK O diretório padrão é o C:\Program Files\Java 
 7 - No Popup de Variáveis de Ambiente, no quadro Variáveis do Sistema, selecionar a variável Path e clicar na opção Editar... 
 8 - No Popup Editar a variável de ambiente, clicar na opção novo e preencher o campo com %JAVA_HOME%\bin. Mover o novo campo para cima com o botão Mover para Cima até que ele seja o primeiro da lista. 
 9 - Clicar em OK até que todos os Popups sejam fechados. 
 10 - Fazer o download do pacote 2019-12 do Eclipse a partir do link indicado acima 
 11 - Descompactar o arquivo do Eclipse baixado para o disco C: e executar o eclipse.exe 
 12 - Realizar a instalação do Eclipse e escolher o diretório de Workspace desejado. 
 13 - Abrir o Eclipse e no menu Help selecionar a opção Eclipse Marketplace 
 14 - Buscar pelo plugin do Cucumber e realizar a instalação 
 15 - Fazer o download do GIT a partir do link indicado acima 
 16 - Executar o arquivo do GIT baixado e seguir com a instalação até o fim 
 17 - Acessar o diretório no qual deseja manter o projeto, clicar com o botão direito e selecionar a opção Git Bash Here 
 18 - Colar o comando git clone com o repositório do projeto
 19 - Aguardar download do projeto finalizar e fechar o prompt 
 20 - Acessar o Eclipse e no menu File selecionar a opção Open Project from File System 
 21 - Preencher o campo import source com o diretório do projeto e clicar em Finish 
 22 - Aguardar importação e build do projeto. *Caso ocorram problemas devido aos caracteres epeciais e acentos, clicar com o botão direito no projeto, acessar opção Properties, na seção Text file encoding, clicar na opção Other e selecionar o encoding UTF-8 e clicar em Apply and Close 
 23 - No diretório src/test/java/runner está localizada a classe MainRunner para execução dos testes. 