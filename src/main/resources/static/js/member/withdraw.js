$(document).ready(function() {
     let token = $("meta[name='_csrf']").attr("content");
     let header = $("meta[name='_csrf_header']").attr("content");
     console.log(token);
     console.log(header);

    $('#confirmWithdraw').on('click', function() {
        var password = $('#password').val();
        var $passwordError = $('#passwordError');

        // 비밀번호 입력 유효성 검사
        if (password.trim() === '') {
            $passwordError.text('비밀번호를 입력해 주세요.');
            return;
        } else {
            $passwordError.text('');
        }

        $.ajax({
            url: '/withdraw',
            type: 'POST',
            beforeSend : function(xhr)
                 {
                    xhr.setRequestHeader(header, token);
                 },
            data: {
                password: password
            },
            success: function(response) {
                if (response.success) {
                    alert('회원 탈퇴가 완료되었습니다.');
                    window.location.href = '/logout'; // 로그아웃 후 리다이렉션
                } else {
                    alert('비밀번호가 일치하지 않습니다.');
                    $passwordError.text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function() {
                console.error("Error status: " + status);
                console.error("Error message: " + error);
                console.error("Response text: " + xhr.responseText);

                $passwordError.text('서버 오류가 발생했습니다. 다시 시도해 주세요.');
            }
        });
    });
});
