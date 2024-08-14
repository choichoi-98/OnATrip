$(document).ready(function() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $('.deleteReplyBtn').on('click', function() {
        let replyId = $(this).data('id'); // data-id 값 읽어오기
        console.log('삭제할 댓글 ID:', replyId); // ID 확인
        if (!replyId) {
            console.error('댓글 ID를 찾을 수 없습니다.');
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/deleteAnswer',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: { id: replyId },
            success: function(response) {
                // 성공 시 삭제 버튼 숨기기 및 댓글 내용 지우기
                $('.deleteReplyBtn[data-id="' + replyId + '"]').hide();
                $('#replyForm').val(''); // 댓글 내용 지우기
                alert('댓글이 삭제되었습니다.');
            },
            error: function(xhr, status, error) {
                console.error('댓글 삭제 실패:', status, error);
            }
        });
    });
});
