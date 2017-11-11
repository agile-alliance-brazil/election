(function($) {
  var maxVotes = 0;
  var votes = [];

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
    $('form.vote input[type="submit"]').prop('disabled', $('li.candidate.selected').size() < maxVotes);
  };

  var toggleVote = function() {
    var target = $(this);
    var unselecting = target.hasClass('selected');

    if (!unselecting || maxVotes == 1) {
      var previousVote = $('#vote-1').val();
      unvoteOn(previousVote);
    }

    var selectedCandidates = $('li.candidate.selected').size();
    if (!unselecting && selectedCandidates < maxVotes) {
      voteOn(target.data('candidate-id'));
    } else if (unselecting) {
      unvoteOn(target.data('candidate-id'));
    }
    checkSubmition();
  };

  var confirmVote = function() {
    if (confirm($(this).data('confirm-message'))) {
      this.submit();
    } else {
      return false;
    }
  };

  $(document).ready(function() {
    $('.vote li.candidate').click(toggleVote);
    var existingVotes = $('.vote').filter(function(current, index) {
      return $('#vote-'+current).val() !== "";
    });
    $('.vote li.candidate').removeClass('selected');
    existingVotes.each(function(index, current) {
      var vote = $(current).val();
      $('.vote li.candidate#candidate-' + vote).addClass('selected');
    });
    checkSubmition();
    maxVotes = parseInt($('form.vote').data('vote-count'), 10);
    for (var index = 0; index < maxVotes; index++) {
      votes.push(index + 1);
    }
    $('form.vote input[type="submit"]').click(confirmVote);
  });
})(jQuery);
