//$(document).ready(function(){
//     let token = $("meta[name='_csrf']").attr("content");
//     let header = $("meta[name='_csrf_header']").attr("content");
//     console.log(token);
//     console.log(header);
//
//    var loginBtn = $("#loginBtn");
//
//    loginBtn.submit(function(event) {
//     var email = $("input[name='email']").val();
//     var password = $("input[name='password']").val();
//
//      // 비밀번호가 일치하지 않는 경우
//      $.ajax({
//          type: "POST",
//          url: "/checkPassword",
//           data: {
//               email: email,
//               password: password
//           },
//           success: function(response) {
//               if (!response) {
//                   errorMessage = "비밀번호가 일치하지 않습니다.";
//                   alert(errorMessage);
//                   event.preventDefault(); // 폼 제출을 막음
//               } else {
//                   // 비밀번호가 일치할 경우 폼 제출 진행
//               }
//           },
//           error: function(xhr, status, error) {
//           console.error("서버 오류:", error);
//           }
//      });
//    }
//});
