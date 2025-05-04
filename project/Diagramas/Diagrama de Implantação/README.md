@startuml
skinparam nodesep 50
skinparam ranksep 50
skinparam defaultFontName Arial

artifact "Computador do Usuário" as user {
  component "Navegador" as browser
}

node "Servidor de Aplicação" as app_server {
  component "Front-end" as front
  component "Back-end" as back
}

node "Servidor de Banco de Dados" as db_server {
  database "Banco de Dados" as database
}

front .. back
browser .. front
browser .. back
app_server .. db_server
user -- app_server : «HTTPS»
app_server -- db_server : «JDBC»

@enduml