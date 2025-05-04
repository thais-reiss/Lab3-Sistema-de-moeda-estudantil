@startuml
left to right direction
skinparam packageStyle rectangle

actor Aluno
actor Professor
actor "Empresa Parceira" as Empresa
actor "Instituição de Ensino" as Instituicao
actor "Tempo" as Timer << Relógio >>

rectangle "Sistema de Mérito Acadêmico" {
  (Realizar Cadastro)
  (Consultar Extrato)
  (Trocar Moedas)
  (Distribuir Moedas)
  (Cadastrar Vantagem)
  (Validar Cupom)
  (Estabelecer Parceria)
  
  (Acumular Moedas) as (Acumular)
  (Enviar Notificação) as (Notificar)
  
  Aluno --> (Realizar Cadastro)
  Aluno --> (Consultar Extrato)
  Aluno --> (Trocar Moedas)
  
  Professor --> (Distribuir Moedas)
  Professor --> (Consultar Extrato)
  
  Empresa --> (Cadastrar Vantagem)
  Empresa --> (Validar Cupom)
  
  Instituicao --> (Estabelecer Parceria)
  
  Timer --> (Acumular) : Mensalmente
  
  (Realizar Cadastro) .> (Selecionar Instituição) : «include»
  (Distribuir Moedas) .> (Verificar Vínculo) : «include»
  (Trocar Moedas) .> (Validar Cupom) : «include»
  
  (Acumular) --> (Notificar) : Gera evento
  (Distribuir Moedas) --> (Notificar)
  (Trocar Moedas) --> (Notificar)
}

@enduml