(function($) {
  var votes = [1, 2, 3];

  var voteOn = function(candidateId) {
    var currentlyUnvoted = votes.filter(function(current, index) {
      return $('#vote-'+current).val() === "";
    });
    if (currentlyUnvoted.length > 0) {
      var toVoteOn = currentlyUnvoted[0];
      $('#vote-'+toVoteOn).val(candidateId);
      $('li.candidate#candidate-' + candidateId).addClass('selected');
    }
  };

  var unvoteOn = function(candidateId) {
    var currentVotes = votes.filter(function(current, index) {
      return $('#vote-'+current).val() === ""+candidateId;
    });
    if (currentVotes.length > 0) {
      $(currentVotes).each(function(index, current) {
        $('#vote-'+current).val("");
      });
    }
    $('li.candidate#candidate-' + candidateId).removeClass('selected');
  };

  var checkSubmition = function() {
    $('form.vote input[type="submit"]').prop('disabled', $('li.candidate.selected').size() !== 3);
  };

  var toggleVote = function() {
    var target = $(this);
    var unselecting = target.hasClass('selected');
    var selectedCandidates = $('li.candidate.selected').size();
    if (!unselecting && selectedCandidates < 3) {
      voteOn(target.data('candidate-id'));
    } else if (unselecting) {
      unvoteOn(target.data('candidate-id'));
    }
    checkSubmition();
  };

  $(document).ready(function() {
    $('li.candidate').click(toggleVote);
    var votes = $('.vote').filter(function(current, index) {
      return $('#vote-'+current).val() !== "";
    });
    $('li.candidate').removeClass('selected');
    votes.each(function(index, current) {
      var vote = $(current).val();
      $('li.candidate#candidate-' + vote).addClass('selected');
    });
    checkSubmition();
  });
})(jQuery);
