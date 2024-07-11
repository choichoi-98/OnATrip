$(document).ready(function() {
     let token = $("meta[name='_csrf']").attr("content");
     let header = $("meta[name='_csrf_header']").attr("content");
     console.log(token);
     console.log(header);

  $('#findPasswordForm').submit(function(event) {
    event.preventDefault(); // 기본 폼 제출 동작 막기

    var email = $('#email').val();
    if (email) {
        console.log('Email:', email); // 디버깅을 위한 콘솔 출력
    }

    // 이메일 유효성 검사
    if (!isValidEmail(email)) {
      alert('유효한 이메일 주소를 입력해주세요.');
      return;
    }

    // 이메일 발송 AJAX 요청
    $.ajax({
      type: 'POST',
      url: '/sendEmail',
      beforeSend : function(xhr)
          {
               xhr.setRequestHeader(header, token);
          },
      data: { email: email },
      success: function(data) {
         if (!data) {
         console.log("이메일주소" + email);
                   alert('이메일 발송에 실패했습니다.');
                } else {
                  alert('이메일이 발송되었습니다.');
                }
              },
              error: function() {
                alert('서버 오류가 발생했습니다.');
              }
            });
          });


  // 이메일 유효성 검사 함수
  function isValidEmail(email) {
    // 이메일 형식 검사 로직 구현
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
});
