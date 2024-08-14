        $(document).ready(function() {
             let token = $("meta[name='_csrf']").attr("content");
             let header = $("meta[name='_csrf_header']").attr("content");
             console.log(token);
             console.log(header);
            function validateEmail() {
                var $emailInput = $('#email');
                var $emailError = $('#emailError');
                var emailPattern = /^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/;
                if (!emailPattern.test($emailInput.val().trim())) {
                    $emailError.addClass('show');
                } else {
                    $emailError.removeClass('show');
                }
            }

            function validatePassword() {
                var $passwordInput = $('#password');
                var $passwordError = $('#passwordError');
                if ($passwordInput.val().trim() === '') {
                    $passwordError.addClass('show');
                } else {
                    $passwordError.removeClass('show');
                }
            }

            $('#email').on('input', validateEmail);
            $('#password').on('input', validatePassword);

            $('#loginForm').on('submit', function(event) {
                validateEmail();  // 추가 검증
                validatePassword();  // 추가 검증

                var $emailError = $('#emailError');
                var $passwordError = $('#passwordError');
                if ($emailError.hasClass('show') || $passwordError.hasClass('show')) {
                    event.preventDefault(); // 폼 제출을 막음
                }
            });

             $('#email, #password').on('input', function() {
                 $('#error').hide();  // 에러 메시지 숨기기
             });
        });
