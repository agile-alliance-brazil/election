{
  :elections
  {
    :not-found "No election with ID %d found."
    :new-voters-registered "%d new voter(s) registered."
    :some-voters-registered "%d new voter(s) registered (existing: %d, invalid: %d)"
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
  :candidates
  {
    :add "Add a new candidate"
    :added "Candidate added"
    :add-failed "Error adding candidate"
    :edit "Edit details"
    :update "Atualizar"
    :updated "Candidate updated"
    :fullname "Full name"
    :email "E-mail"
    :twitter "Twitter"
    :motivation "Motivation"
    :region "Region"
    :minibio "Mini-bio"
  }
  :votes
  {
    :register "Register voters"
    :partial-results "Partial results"
    :final-results "Final results"
    :count "Votes"
    :add-voters "Add voter list"
    :instructions "Please select %d candidate(s) towards which you will cast your vote and press the 'Place Vote' button."
    :place "Place Vote"
    :used-token "Token already used"
    :not-accepting-votes "Not currently in voting period"
    :recorded "Vote recorded! Thank you."
    :invalid "Invalid vote. Please ensure you selected the right amount of candidates."
    :confirm "Votes cannot be changed once cast. Are you sure you want to cast your vote?"
    :voter-count "Total voter count"
    :votes-count "Total votes to receive"
    :casted-votes-count "Total votes received"
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

As an attendee of Agile Brazil 2016, 2017 or 2018, you are invited to vote in the election of
one board members for Agile Alliance Brazil for the 2020-2022 term.

To vote, access the link below. You will be able to vote only once and you cannot edit your vote after casting it.
You have to vote in one candidate and must cast your vote until %s.

%s

Thank you for your participation,
Agile Alliance Brazil"
      :html* "Hello %s,


As an attendee of Agile Brazil 2016, 2017 or 2018, you are invited to vote in the election of
one board members for Agile Alliance Brazil for the 2020-2022 term.
To vote, access the link below. You will be able to vote only once and you cannot edit your vote after casting it.
You have to vote in one candidates and must cast your vote until %s.

[%s](%s)


Thank you for your participation,
Agile Alliance Brazil"
    }
    :reminder
    {
      :subject "The election %s is ending soon. Please remember to place your vote."
      :text! "Hello %s,

Agile Alliance Brazil is in another board renewal cycle. Through an election with voters from the
Brazilian members of Agile Alliance, we will select, among %d candidates, the %d new board member(s) for
the 2020-2022 term.

Your vote, as a member of Agile Alliance Brazil, is very important for this process. If you already cast
your vote, no further action is needed. Thank you very much. If you have not cast your vote, learn a bit more
about each candidate below (sorted alphabetically). To vote, search your inbox for an email with subject
'%s'. In it, you will find a unique link to place your vote securely until %s. If you encounter any problems,
reply to this email describing your problem so we can help you.

Candidates:

%s

Sincerely,

Agile Alliance Brazil"
      :candidate-partial-text! "#%s

Describe a little bit about your reasons to be part of the Agile Alliance Brazil board between 2020 and 2022.

%s

Region: %s
%s
---
"
      :candidate-social-text! "
Social Network: %s
"
      :html* "Hello %s,

Agile Alliance Brazil is in another board renewal cycle. Through an election with voters from the
Brazilian members of Agile Alliance, we will select, among %d candidates, the %d new board member(s) for
the 2020-2022 term.

Your vote, as a member of Agile Alliance Brazil, is very important for this process. If you already cast
your vote, no further action is needed. Thank you very much. If you have not cast your vote, learn a bit more
about each candidate below (sorted alphabetically). To vote, search your inbox for an email with subject
**%s**. In it, you will find a unique link to place your vote securely until **%s**. If you encounter any problems,
reply to this email describing your problem so we can help you.

Candidates:

%s

Sincerely,

Agile Alliance Brazil"
      :candidate-partial-html* "#%s

**Describe a little bit about your reasons to be part of the Agile Alliance Brazil board between 2020 and 2022.**

%s

**Region:** %s
%s
---
"
      :candidate-social-html* "
**Social Network:** [%s](%s)
"
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
      :successful "Connection to '%s' has response code %s with response time %d ms."
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
