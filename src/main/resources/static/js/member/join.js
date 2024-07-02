 $(document).ready(function(){
     let token = $("meta[name='_csrf']").attr("content");
     let header = $("meta[name='_csrf_header']").attr("content");
     console.log(token);
     console.log(header);

     window.sendNumber = function() {
         $("#input-group").css("display", "flex");
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
             },
             error: function() {
                         alert("서버 오류가 발생했습니다.");
             }
         });
     }

     window.confirmNumber = function() {
         var number1 = $("#number").val();
         var number2 = $("#Confirm").val();

           // 콘솔에 값 찍기
           console.log("number1: ", number1);
           console.log("number2: ", number2);

         if (number1 == number2) {
             alert("인증되었습니다.");
         } else {
             alert("번호가 다릅니다.");
         }
     }

 });
