{
  :elections
  {
    :not-found "Eleição com ID %d não foi encontrada."
    :new-voters-registered "%d novos eleitores inscritos."
    :voter-registration-failed "Inscrição de eleitores falhou"
  }
  :votes
  {
    :register "Inscrever eleitores"
    :partial-results "Resultados parciais"
    :final-results "Resultados finais"
    :count "Votos: %d"
    :add-voters "Adicionar lista de eleitores"
    :instructions "Por favor, selecione %d candidatos a favor dos quais seu voto será contabilizado e pressione o botão 'Votar'."
    :place "Votar"
    :used-token "Seu token já foi utilizado"
    :not-accepting-votes "Essa eleição não está em período de votação"
    :recorded "Voto contabilizado! Obrigado."
    :invalid "Voto inválido. Por favor, verifique que selecionou a quantidade correta de candidatos."
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
      :text!
      (str
        "Olá %s,\n\n"
        "Como participante do Agile Brazil 2014 e 2015, você está habilitado a votar para a eleição de "
        "três membros ao board de diretores da Agile Alliance Brazil para o período de 2017-2019.\n\n"
        "Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.\n\n"
        "Você deve votar em três candidatos.\n\n"
        "%s\n\n"
        "Agradecemos a sua participação,\n"
        "Agile Alliance Brazil"
      )
      :html*
      (str
        "Olá %s,\n\n"
        "Como participante do Agile Brazil 2014 e 2015, você está habilitado a votar para a eleição de "
        "três membros ao board de diretores da Agile Alliance Brazil para o período de 2017-2019.\n\n"
        "Para votar, acesse o link abaixo. Você poderá votar apenas uma vez e não poderá editar seu voto após o envio.\n\n"
        "Você deve votar em três candidatos.\n\n"
        "[%s](%s)\n\n"
        "Agradecemos a sua participação,\n"
        "Agile Alliance Brazil"
      )
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
}
