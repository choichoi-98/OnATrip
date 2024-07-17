 $(document).ready(function() {
     $('#newPasswordCheck').on('input', function() {
         var password = $('#newPassword').val();
         var confirmPassword = $(this).val();
         var match = $('#passwordMatch');

         if (confirmPassword === '') {
             match.text('');
         } else if (password === confirmPassword) {
             match.text('비밀번호가 일치합니다.');
             match.removeClass('not-match').addClass('match');
         } else {
             match.text('비밀번호가 일치하지 않습니다.');
             match.removeClass('match').addClass('not-match');
         }
     });
});