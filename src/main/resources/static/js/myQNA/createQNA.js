$(document).ready(function() {
    $('#confirmBtn').on('click', function(event) {
        // 선택된 문의 유형 확인
        var qnaStatus = $('select[name="qnaStatus"]').val();
        var $errorContainer = $('#form-errors');

        // 초기화
        $errorContainer.text("");

        // 선택이 안된 경우
        if (qnaStatus === "") {
            $errorContainer.text("문의 유형을 선택해 주세요.");
            event.preventDefault(); // 폼 제출 방지
        }
    });
});
