{
  :elections
  {
    :not-found "No election with ID %d found."
    :new-voters-registered "%d new voters registered."
    :voter-registration-failed "Voter registration failed"
  }
  :votes
  {
    :register "Register voters"
    :partial-results "Partial results"
    :final-results "Final results"
    :count "Votes: %d"
    :add-voters "Add voter list"
    :instructions "Please select %d candidates towards which you will cast your vote and press the 'Place Vote' button."
    :place "Place Vote"
    :used-token "Token already used"
    :not-accepting-votes "Not currently in voting period"
    :recorded "Vote recorded! Thank you."
    :invalid "Invalid vote. Please ensure you selected the right amount of candidates."
  }
  :session
  {
    :new "Login"
    :destroy "Logout from %s"
    :destroyed "Logged out successfully!"
    :invalid "Invalid authentication. Please try again"
    :created "Logged in successfully!"
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
    :up "Application is up"
    :db-connection
    {
      :successful "DB connection is up. Last migration: %s."
      :failed "DB connection failed. Connection/query to %s failed with: %s %s."
    }
    :connection
    {
      :successful "Connection to %s has response code %d with response time %.2f ms."
      :failed "HTTP request failed: %s"
    }
  }
  :forbidden "Unauthorized access"
  :none "none"
}
