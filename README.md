# reserva-hospedagem

web service de reservas e hospedagens com Spring REST e Spring Data JPA

## pré-requisitos:

* o serviço precisa se conectar a um banco de dados existente e configurado via `application.properties`
  * existe um dump exemplo para o MySQL fornecido na pasta "database"

## arquitetura do banco de dados

<p align="center"><img src="/imagens/0-database.png" alt="modelo de banco de dados"/></p>

## interações disponíveis
#### 1. entrar com as datas para hospedagem e receber os quartos disponíveis no período desejado:

<p align="center"><img src="/imagens/1-buscar-vagas.png" alt="tela" /></p>

#### 2. marcar e reservar os quartos disponíveis:

<p align="center"><img src="/imagens/2-reservar.png" alt="tela" /></p>

#### 3. iniciar o cadastro para o caso de um novo cliente

<p align="center"><img src="/imagens/3-fazer-cadastro.png" alt="tela" /></p>

#### 4. a recepcionista fará o checkin com a possibilidade de mudar a data de checkout (para uma data inferior da reserva)

<p align="center"><img src="/imagens/4-fazer-checkin.png" alt="tela" /></p>

#### 5. a recepcionista poderá buscar as hospedagens vigente antes de inciar o checkout

<p align="center"><img src="/imagens/5-buscar-hospedagens.png" alt="tela" /></p>

#### 6. durante a hospedagem, o cliente pode solicitar diversos serviços de quarto

<p align="center"><img src="/imagens/6-solicitar-servico.png" alt="tela" /></p>

#### 7. o checkout é iniciado

<p align="center"><img src="/imagens/7-fazer-checkout.png" alt="tela" /></p>
