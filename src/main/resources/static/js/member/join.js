 $(document).ready(function(){
     let token = $("meta[name='_csrf']").attr("content");
     let header = $("meta[name='_csrf_header']").attr("content");
     console.log(token);
     console.log(header);


  window.checkEmail = function() {
         $.ajax({
             url: "/checkEmail",
             type: "post",
             dataType: "json",
             beforeSend : function(xhr)
                 { //데이터를 전송하기 전에 헤더에 csrf값을 설정
                   xhr.setRequestHeader(header, token);
                 },
             data: { "email": $("#email").val() },
             success: function(data) {
                 if(data) {
                     alert("이미 등록된 이메일입니다.");
                     //$('.text-next').prop('disabled', true);
                 }else {
                     //$('.text-next').prop('disabled', false);
                     sendNumber(); // 인증번호 발송 함수 호출
                     }
             },
             error: function() {
                 alert("서버 오류가 발생했습니다.");
             }
         });
     };


      window.sendNumber = function() {
          $("#input-group").css("display", "flex");
          //이메일 인증번호 전송
          $.ajax({
              url: "/mail",
              type: "post",
              dataType: "json",
              beforeSend : function(xhr)
                                   { //데이터를 전송하기 전에 헤더에 csrf값을 설정
                                     xhr.setRequestHeader(header, token);
                                   },
              data: { "email": $("#email").val() },
                          success: function(data) {
                              alert("인증번호 발송");
                              $("#Confirm").attr("value", data);
                              //window.emailVerified = false; // 이메일 인증 초기화
                          },
                          error: function() {
                              alert("서버 오류가 발생했습니다.");
                          }
                      });
                  };


//인증번호 일치하는지 확인
     window.confirmNumber = function() {
         var number1 = $("#number").val();
         var number2 = $("#Confirm").val();

           // 콘솔에 값 찍기
           console.log("number1: ", number1);
           console.log("number2: ", number2);

         if (number1 == number2) {
             alert("인증되었습니다.");
         } else if(number1 == '') {
             alert("번호를 입력해주세요.");
         }else {
              alert("번호가 다릅니다.");
         }
     }

     // 이메일 인증 여부를 저장하는 변수
     let isEmailVerified = false;

     // 이메일 인증 버튼 클릭 이벤트 처리
     $('#confirmBtn').click(function() {
       // 이메일 인증 로직 구현
       // 인증이 완료되면 isEmailVerified를 true로 설정
       isEmailVerified = true;
     });

     // 회원가입 버튼 클릭 이벤트 처리
     $('.form_join').submit(function(event) {
       event.preventDefault(); // 폼 제출 기본 동작 막기

       // 이메일 인증이 완료되지 않은 경우
       if (!isEmailVerified) {
         alert('이메일 인증을 완료해주세요.');
         return;
       }
        // 비밀번호와 비밀번호 확인 필드 값 가져오기
        let password = $('#password').val();
        let passwdCheck = $('#passwdCheck').val();

        // 비밀번호가 일치하지 않는 경우
        if (password !== passwdCheck) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }
       // 이메일 인증이 완료된 경우 폼 제출 진행
       $(this).off('submit').submit();
     });

 });
