@startuml
left to right direction
skinparam packageStyle rectangle

actor Usuario
actor Aluno
actor Professor
actor "Empresa Parceira" as Empresa
actor "Usuario da Instituição" as UserInst

Usuario  <|-- UserInst
UserInst  <|-- Aluno
UserInst  <|-- Professor
Usuario  <|-- Empresa

rectangle "Sistema de Mérito Acadêmico" {
  (Realizar Login)
  (Realizar Cadastro)
  (Consultar Extrato)
  (Trocar Moedas)
  (Distribuir Moedas)
  (Cadastrar Vantagem)
  (Validar Cupom)
  
  (Enviar Notificação) as (Notificar)
  
  Usuario --> (Realizar Login)
  Usuario --> (Realizar Cadastro)

  UserInst --> (Consultar Extrato)

  Aluno --> (Trocar Moedas)
  
  Professor --> (Distribuir Moedas)
  
  Empresa --> (Cadastrar Vantagem)
  Empresa --> (Validar Cupom)
  
  (Distribuir Moedas) .> (Notificar) : «include»
  (Trocar Moedas) .> (Notificar) : «include»
}

@enduml
