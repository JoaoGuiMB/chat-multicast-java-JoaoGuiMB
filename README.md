# Chat Multicast

**Aluno**: João Guilherme Martins Borborema

**Email**: jborborema@sga.pucminas.br

**Curso**: Engenharia de Software

**Disciplina**: Laboratório de desenvolvimento de aplicações móveis e distribuídas

**Professor**: Hugo Bastos de Paula

## Como usar


1) Clone o repositório

2) Acesse a pasta chat-multicast-java-JoaoGuiMB

2) Acesse a pasta bin (cd bin)

3) Execute a Classe Server

4) Execute a Classe Client, passando os
   parâmetros startServer e o nome do usuário do sistema

**Exemplo**:
 ```bash
 java redes.Server
 java redes.Client startServer Ana
 ```

## Classes

- **Client**: Gerencia os comando de entrada do usuário e faz o envio das informações para o servidor
- **Server**: Cria um server UDP, possui a lógica de gerenciar as salas e chat de acordo com os comandos enviados pelo Cliente
- **Room**: Modelo das salas de chat, possui os atributos: id, nome, endereço, lista de usuários

## Protocolo


O protocolo para a comunicação entre o Client e o Servidor segue o formato "opção;dados", em que a opção é um número natural, referente ao comando dado pelo cliente e os dados é a informação enviada, por exemplo:

```bash 
 1;Nome da Sala
```

O número 1 é a opção de criar uma sala, e o segundo parâmtro é o nome dessa Sala

## Comandos


- **Criar Sala**: representado pelo valor **1** no menu inicial do sistema, ápos isso o usuário informará o nome da nova sala;

- **Listar Salas**: número **2** no menu inicial do sistema, o servidor retonará todas as salas já criadas;

- **Entrar em sala**: número **3** no menu inicial, ápos isso o usuário irá informar o nome da sala em que deseja entrar;

- **Listar membros**: ápos entrar na sala, o servidor informará os membros que estão nessa sala;

- **Enviar Mensagem**: dentro da sala, o usuário digitará qualquer texto, exceto apenas o número 0, e pressionar "Enter" para enviar a mensagem;

- **Sair da Sala**: dentro da sala, o usuário poderá digitar o número 0 para sair e retornará para o menu inicial.
