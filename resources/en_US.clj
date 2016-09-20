{
  :elections
  {
    :not-found "No election with ID %d found."
    :new-voters-registered "%d new voter(s) registered."
    :voter-registration-failed "Voter registration failed"
    :create "Create election"
    :name "Election name"
    :description "Description"
    :startdate "Start date for election to start"
    :enddate "Deadline for votes to be casted"
    :candidates-to-elect "Number of candidates to elect"
    :candidates-to-vote-on "Number of candidates to select on a vote"
    :election-created "Election created successfully!"
    :create-election-failed "Election creation failed. Try again"
  }
  :votes
  {
    :register "Register voters"
    :partial-results "Partial results"
    :final-results "Final results"
    :count "Votes"
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
      :text! "Hello %s,

As an attendee of Agiel Brazil 2014 and/or 2015, you are invited to vote in the election of
three board members for Agile Alliance Brazil for the 2017-2019 term.

To vote, access the link below. You will be able to vote only once and you cannot edit your vote after casting it.
You have to vote in three candidates and must cast your vote until %s.

%s

Thank you for your participation,
Agile Alliance Brazil"
      :html* "Hello %s,


As an attendee of Agiel Brazil 2014 and/or 2015, you are invited to vote in the election of
three board members for Agile Alliance Brazil for the 2017-2019 term.
To vote, access the link below. You will be able to vote only once and you cannot edit your vote after casting it.
You have to vote in three candidates and must cast your vote until %s.

[%s](%s)


Thank you for your participation,
Agile Alliance Brazil"
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
  :pt-BR "Brazilian Portuguese"
  :en-US "English (U.S.)"
  :ssn "Social Security Number"
  :agile-alliance-logo "Agile Alliance logo"
}
