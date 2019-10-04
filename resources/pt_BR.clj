{
  :elections
  {
    :not-found "Eleição com ID %d não foi encontrada."
    :new-voters-registered "%d novo(s) eleitor(es) inscrito(s)."
    :some-voters-registered "%d new voter(s) registered (existing: %d, invalid: %d)"
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
  :candidates
  {
    :add "Adicionar novo candidato"
    :added "Candidato adicionado"
    :add-failed "Erro ao adicionar candidato"
    :edit "Editar detalhes"
    :update "Atualizar"
    :updated "Candidato atualizado"
    :fullname "Nome completo"
    :email "E-mail"
    :twitter "Twitter"
    :motivation "Motivação"
    :region "Região"
    :minibio "Minibio"
  }
  :votes
  {
    :register "Inscrever eleitores"
    :partial-results "Resultados parciais"
    :final-results "Resultados finais"
    :count "Votos"
    :add-voters "Adicionar lista de eleitores"
    :instructions "Por favor, selecione %d candidato(a) a favor do(a) qual seu voto será contabilizado e pressione o botão 'Votar'."
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

Como participante do Agile Brazil 2016, 2017 ou 2018, você está habilitado a votar para a eleição de um membro
ao board de diretoria da Agile Alliance Brazil para o período de 2020-2022.

Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.
Você deve votar em um(a) candidato(a) e tem até %s para submeter seu voto.

%s

Agradecemos a sua participação,
Agile Alliance Brazil"
      :html* "Olá %s,


Como participante do Agile Brazil 2016, 2017 ou 2018, você está habilitado a votar para a eleição de
um membros ao board de diretoria da Agile Alliance Brazil para o período de 2020-2022.


Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.
Você deve votar em um(a) candidato(a) e tem até %s para submeter seu voto.


[%s](%s)


Agradecemos a sua participação,
Agile Alliance Brazil"
    }
    :reminder
    {
      :subject "A eleição %s está quase terminando. Lembre-se de votar."
      :text! "Olá %s,

A Agile Alliance Brazil está em mais um ciclo de renovação do board de diretoria da associação.
Por meio de uma eleição entre o corpo de membros brasileiros da Agile Alliance, selecionaremos entre
%d candidatos(as), o(a) novo(a) integrante do conselho de administração para o período de 2020-2022.

Seu voto, como membro(a) da Agile Alliance Brazil, é muito importante para esse processo. Caso já
tenha votado, não precisa fazer mais nada. Muito obrigado. Se ainda não tiver votado, conheça
um pouco mais sobre cada candidato(a) participante dessa eleição (listados em ordem alfabética) abaixo. Para
votar, procure em sua caixa de e-mail por um mensagem intitulada '%s'. Nela você encontrará um
link único para fazer sua votação com a máxima segurança até %s. Se tiver dificuldades, responda este email
descrevendo seu problema para lhe ajudarmos.

Candidatos(as):

%s

Atenciosamente,

Agile Alliance Brazil"
      :candidate-partial-text! "#%s

Descreva um pouco sobre a sua motivação para participar do board da Agile Alliance Brazil no período 2020-2022.

%s

Região: %s

%s

---
"
      :candidate-social-text! "
Rede Social: %s
"
      :html* "Olá %s,

A Agile Alliance Brazil está em mais um ciclo de renovação do board de diretoria da associação.
Por meio de uma eleição entre o corpo de membros brasileiros da Agile Alliance, selecionaremos entre
%d candidatos(as), o(a) novo(a) integrantes do conselho de administração para o período de 2020-2022.

Seu voto, como membro(a) da Agile Alliance Brazil, é muito importante para esse processo. Caso já
tenha votado, não precisa fazer mais nada. Muito obrigado. Se ainda não tiver votado, conheça
um pouco mais sobre cada candidato(a) participante dessa eleição (listados em ordem alfabética) abaixo. Para
votar, procure em sua caixa de e-mail por um mensagem intitulada **%s**. Nela você
encontrará um link único para fazer sua votação com a máxima segurança até **%s**. Se tiver dificuldades, responda este email
descrevendo seu problema para lhe ajudarmos.

##Candidatos(as):

%s

Atenciosamente,

Agile Alliance Brazil"
      :candidate-partial-html* "#%s

**Descreva um pouco sobre a sua motivação para participar do board da Agile Alliance Brazil no período 2020-2022.**

%s

**Região:** %s

%s

---
"
      :candidate-social-html* "
**Rede Social:** [%s](%s)
"
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
      :successful "Conexão até '%s' respondeu com código %s em %d ms."
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
