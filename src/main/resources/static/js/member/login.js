//$(document).ready(function(){
//     let token = $("meta[name='_csrf']").attr("content");
//     let header = $("meta[name='_csrf_header']").attr("content");
//     console.log(token);
//     console.log(header);
//
//    $('#form_login').submit(function(event) {
//        event.preventDefault(); // 폼 기본 제출 동작 방지
//
//        var email = $('#email').val();
//        var password = $('#password').val();
//        var match = $('#checkMember');
//
//        // 간단한 예시로 이메일과 비밀번호가 존재하고 비밀번호가 'password'일 때만 로그인 성공으로 가정합니다.
//        if (email && password === 'password') {
//            // 로그인 성공
//            match.text('');
//        } else {
//            // 로그인 실패
//            match.text('이메일이나 비밀번호가 잘못되었습니다. 다시 확인해주세요.'); // 에러 메시지 표시
//            match.removeClass('checkMember').addClass('checkFail');
//        }
//    });
//});
