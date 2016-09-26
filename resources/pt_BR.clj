{
  :elections
  {
    :not-found "Eleição com ID %d não foi encontrada."
    :new-voters-registered "%d novo(s) eleitor(es) inscrito(s)."
    :voter-registration-failed "Inscrição de eleitores falhou"
    :create "Criar eleição"
    :name "Nome da eleição"
    :description "Descrição"
    :startdate "Data de início da votação"
    :enddate "Data de fim da votação"
    :candidates-to-elect "Número de candidatos a serem eleitos"
    :candidates-to-vote-on "Número de candidatos a selecionar em um voto"
    :election-created "Eleição criada com sucesso!"
    :create-election-failed "Criação da eleição falhou. Tente novamente"
  }
  :votes
  {
    :register "Inscrever eleitores"
    :partial-results "Resultados parciais"
    :final-results "Resultados finais"
    :count "Votos"
    :add-voters "Adicionar lista de eleitores"
    :instructions "Por favor, selecione %d candidatos a favor dos quais seu voto será contabilizado e pressione o botão 'Votar'."
    :place "Votar"
    :used-token "Seu token já foi utilizado"
    :not-accepting-votes "Essa eleição não está em período de votação"
    :recorded "Voto contabilizado! Obrigado."
    :invalid "Voto inválido. Por favor, verifique que selecionou a quantidade correta de candidatos."
    :confirm "Votos não podem ser alterados uma vez que forem enviados. Você tem certeza que deseja enviar seu voto?"
    :voter-count "Total de eleitores"
    :votes-count "Total de votos a receber"
    :casted-votes-count "Total de votos recebidos"
  }
  :session
  {
    :new "Login"
    :destroy "Deslogar de %s"
    :destroyed "Deslogado com sucesso!"
    :invalid "Autenticação inválida. Tente novamente"
    :created "Logado com sucesso!"
  }
  :mailer
  {
    :token
    {
      :subject "Convite para votar na eleição de membros ao board de diretores da %s"
      :text! "Olá %s,

Como participante do Agile Brazil 2014 e 2015, você está habilitado a votar para a eleição de três membros
ao board de diretores da Agile Alliance Brazil para o período de 2017-2019.

Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.
Você deve votar em três candidatos e tem até %s para submeter seu voto.

%s

Agradecemos a sua participação,
Agile Alliance Brazil"
      :html* "Olá %s,


Como participante do Agile Brazil 2014 e 2015, você está habilitado a votar para a eleição de
três membros ao board de diretores da Agile Alliance Brazil para o período de 2017-2019.


Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.
Você deve votar em três candidatos e tem até %s para submeter seu voto.


[%s](%s)


Agradecemos a sua participação,
Agile Alliance Brazil"
    }
  }
  :status
  {
    :up "A applicação está funcionando"
    :db-connection
    {
      :successful "Conexão com o BD operacional. A última migração foi: %s."
      :failed "Conexão com o BD falhou. Conexão/busca a %s falhou com: %s %s."
    }
    :connection
    {
      :successful "Conexão até %s respondeu com código %d em %.2f ms."
      :failed "Pedido HTTP falhou: %s"
    }
  }
  :forbidden "Acesso negado"
  :none "nenhum(a)"
  :pt-BR "Português do Brasil"
  :en-US "Inglês Americano"
  :cpf "Cadastro de Pessoa Física"
  :agile-alliance-logo "Logo da Agile Alliance"
}
