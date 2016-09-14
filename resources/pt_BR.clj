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
      :subject "You're invited to vote in %s!"
      :text!
      (str
        "Hello %s,\n\n"
        "We're pleased to invite you to vote on %s.\n\n"
        "To cast your vote, simply access the following URL. Note that you can only vote once and cannot edit your vote after placing it.\n\n"
        "%s\n\n"
        "Sincerely,\n"
        "Agile Alliance Brazil board"
      )
      :html*
      (str
        "Hello %s,\n\n"
        "We're pleased to invite you to vote on %s.\n\n"
        "To cast your vote, simply access the following URL. Note that you can only vote once and cannot edit your vote after placing it.\n\n"
        "[%s](%s)\n\n"
        "Sincerely,\n"
        "Agile Alliance Brazil board"
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
