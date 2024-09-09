$(document).ready(function() {
    // CSRF 토큰과 헤더를 가져옵니다
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    console.log(token);
    console.log(header);

    // 회원 탈퇴 버튼 클릭 이벤트 처리
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
            beforeSend: function(xhr) {
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
            error: function(xhr, status, error) {
                console.error("Error status: " + status);
                console.error("Error message: " + error);
                console.error("Response text: " + xhr.responseText);

                $passwordError.text('비밀번호가 일치하지 않습니다.');
            }
        });
    });

    // 비밀번호 변경 폼 제출 이벤트 처리
    $('#changePasswordForm').on('submit', function(event) {
        event.preventDefault();

        var currentPassword = $('#currentPassword').val();
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();

        if (newPassword !== confirmPassword) {
            alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
            return;
        }

        // 서버로 현재 비밀번호 확인 요청
        $.ajax({
            url: '/checkCurrentPassword',
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: {
                currentPassword: currentPassword
            },
            success: function(response) {
                if (response.isValid) {
                    $.ajax({
                        url: '/newPassword',
                        type: 'POST',
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        data: {
                            newPassword: newPassword
                        },
                        success: function() {
                            alert('비밀번호가 성공적으로 변경되었습니다.');
                            $('#changePasswordModal').modal('hide');
                        },
                        error: function(xhr, status, error) {
                            alert('비밀번호 변경 중 오류가 발생했습니다.');
                        }
                    });
                } else {
                    alert('현재 비밀번호가 올바르지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                    console.error("Error status: " + status);
                    console.error("Error message: " + error);
                    console.error("Response text: " + xhr.responseText);
                    alert('비밀번호가 일치하지 않습니다.');
            }
        }); // 비밀번호 확인 ajax 끝
    });
});
