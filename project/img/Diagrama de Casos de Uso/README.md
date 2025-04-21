@startuml
left to right direction
skinparam packageStyle rectangle

actor Aluno
actor Professor
actor "Empresa Parceira" as Empresa
actor "Instituição de Ensino" as Instituicao
actor Sistema
actor Usuário

rectangle "Sistema de Mérito Acadêmico" {
  Usuário --> (Logar no Sistema)

  Aluno --> (Realizar Cadastro)
  Aluno --> (Consultar Extrato)
  Aluno --> (Trocar Moedas)
  (Distribuir Moedas) --> Aluno
  Aluno --> Usuário
  
  Professor --> (Distribuir Moedas)
  Professor --> (Consultar Saldo)
  Professor --> Usuário
  
  Empresa --> (Cadastrar Vantagem)
  Empresa --> (Validar Cupom)
  Empresa --> Usuário
  
  Instituicao --> (Estabelecer Parceria)
  
  Sistema --> (Acumular Moedas)
  Sistema --> (Enviar Notificação)
  Sistema --> (Gerenciar Autenticação)

  (Realizar Cadastro) .> (Selecionar Instituição) : «include»
  (Distribuir Moedas) .> (Verificar Vínculo) : «include»
  (Distribuir Moedas) .> (Enviar Notificação) : «extend» 
  (Trocar Moedas) .> (Validar Cupom) : «include»
  (Estabelecer Parceria) .> (Cadastrar Professores) : «include»
  (Cadastrar Vantagem) .> (Definir Custo) : «include»
  
  (Gerenciar Autenticação) <.. (Realizar Cadastro) : «include»
  (Gerenciar Autenticação) <.. (Distribuir Moedas) : «include»
  (Gerenciar Autenticação) <.. (Consultar Extrato) : «include»
  (Gerenciar Autenticação) <.. (Trocar Moedas) : «include»
  (Gerenciar Autenticação) <.. (Cadastrar Vantagem) : «include»
}

@enduml