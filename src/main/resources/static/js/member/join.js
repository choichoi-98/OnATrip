$(document).ready(function() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    console.log(token);
    console.log(header);

    window.checkEmail = function() {
        $.ajax({
            url: "/checkEmail",
            type: "post",
            dataType: "json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: { "email": $("#email").val() },
            success: function(data) {
                if (data) {
                    alert("이미 등록된 이메일입니다.");
                } else {
                    sendNumber(); // 인증번호 발송 함수 호출
                }
            },
            error: function() {
                alert("서버 오류가 발생했습니다.");
            }
        });
    };

    window.sendNumber = function() {
        $("#input-group").removeClass('hide').addClass('show');
        $.ajax({
            url: "/mail",
            type: "post",
            dataType: "json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: { "email": $("#email").val() },
            success: function(data) {
                alert("인증번호 발송");
                $("#Confirm").val(data); // .attr("value", data) 대신 .val(data) 사용
            },
            error: function() {
                alert("서버 오류가 발생했습니다.");
            }
        });
    };

    window.confirmNumber = function() {
        var number1 = $("#number").val();
        var number2 = $("#Confirm").val();

        console.log("number1: ", number1);
        console.log("number2: ", number2);

        if (number1 == number2) {
            alert("인증되었습니다.");
            isEmailVerified = true; // 인증 완료 시 이메일 인증 변수 업데이트
        } else if (number1 == '') {
            alert("번호를 입력해주세요.");
        } else {
            alert("번호가 다릅니다.");
        }
    };

    let isEmailVerified = false;

    $('#confirmBtn').click(function() {
        if (isEmailVerified) {
            return;
        }
        confirmNumber(); // 인증 번호 확인 함수 호출
    });

    $('.form_join').submit(function(event) {
        event.preventDefault(); // 폼 제출 기본 동작 막기

        if (!isEmailVerified) {
            alert('이메일 인증을 완료해주세요.');
            return;
        }

        let password = $('#password').val();
        let passwdCheck = $('#passwdCheck').val();

        if (password !== passwdCheck) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        $(this).off('submit').submit();
        alert('회원가입이 완료되었습니다. \n 로그인 페이지로 이동합니다.');
    });
});
