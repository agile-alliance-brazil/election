INSERT INTO elections (
  id, name, description, startdate, enddate, candidatestoelect, candidatestovoteon)
VALUES (
  1,
  'Eleição da Agile Alliance Brazil para Board 2017-2019',
  'Eleição para election is for the 2017-2019 board',
  '2016-09-20 00:00:00-03',
  '2016-09-22 23:59:59-03',
  3,
  3);

INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  1,
  1,
  'Diana Prince',
  'She is wonderful!',
  'wonder.woman@leagueofjustice.com',
  0);
INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  2,
  1,
  'Harley Quinn',
  'She is not quite your usual woman but she can make you completely mad',
  'harley.quinn@joker.com',
  0);
INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  3,
  1,
  'Barbara Gordon',
  'Father is a known police hero. She is quite the show herself although you wont be seeing her much around after hours. She has other plans',
  'barbara.gordon@gothampd.com',
  0);
INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  4,
  1,
  'Barry Allen',
  'He can change everything',
  'barry.allen@flash.com',
  0);
INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  5,
  1,
  'Oliver Queen',
  'Pretty good at hitting his targets',
  'oliver.queen@queenconsolidated.com',
  0);
INSERT INTO candidates (
  id, electionid, fullname, minibio, email, votecount)
VALUES (
  6,
  1,
  'Bruce Wayne',
  'A little scary but very rich',
  'bruce@wayneenterprises.com',
  0);
