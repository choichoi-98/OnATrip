$(document).ready(function() {
    console.log('Alerts section is loaded.');

    //MATEiD값 필요 {ALERT.SOUCRE_MEMBEREM
    //PLANiD값 필오 {ALERT.PLAN_ID}
    // AJAX 요청을 통해 알림 데이터 가져오기
    $.ajax({
        url: '/alerts/myAlerts',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log('Alert items fetched:', data);

            if (data.length > 0) {
                // 알림 데이터가 있는 경우
                data.forEach(function(alert) {
                    let alertItem = `
                        <div class="alert-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="alert-message">${alert.message}</h6>
                                <p class="alert-recipient">${alert.sourceMemberEmail}님이 초대하셨습니다.</p>
                            </div>
                            <div>
                                <button class="btn accept-btn" style="background-color: #33333; border-color: #a9a9a9; color: #333; font-size: 0.9rem; padding: 0.4rem 0.8rem; border-radius: 0.3rem;" data-alert-id="${alert.id}" data-sourceMemberEmail="${alert.memberEmail}" data-plan-id="${alert.planId}">수락</button>
                                <button class="btn reject-btn" style="background-color: #33333; border-color: #a9a9a9; color: #333; font-size: 0.9rem; padding: 0.4rem 0.8rem; border-radius: 0.3rem;" data-alert-id="${alert.id}">거절</button>
                            </div>
                        </div>
                    `;
                    $('.alert-summary').append(alertItem);
                });
                console.log('Alert items are present on the page.');
            } else {
                // 알림 데이터가 없는 경우
                $('.alert-summary').append('<p>알림이 없습니다.</p>');
                console.log('No alert items found.');
            }
        },
        error: function(xhr, status, error) {
            console.error('Failed to fetch alert items:', error);
        }
    });

    // 수락 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.accept-btn', function() {
            let alertId = $(this).data('alert-id');
            let email = $(this).data('sourcememberemail'); // camelCase 주의
            let planId = $(this).data('plan-id'); // plan-id로 변경

            console.log('Alert ID:', alertId);
            console.log('Email:', email);
            console.log('Plan ID:', planId);

            inviteFriend(email, planId);
    });

    // 거절 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.reject-btn', function() {
        let alertId = $(this).data('alert-id');
        $.ajax({
            url: '/alerts/rejectAlert/' + alertId,
            type: 'POST',
            success: function(response) {
                console.log('Alert rejected:', response);
                // 버튼 클릭 후 UI 업데이트 로직 추가 가능
                $(this).closest('.alert-item').remove();
            },
            error: function(xhr, status, error) {
                console.error('Failed to reject alert:', error);
            }
        });
    });


});//$(document).ready(function() {


// 친구 추가 함수
function inviteFriend(email, planId) {
    $.ajax({
        url: '/inviteFriend',
        method: 'POST',
        data: { email: email, planId: planId },
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            console.log('친구 초대 성공');
            $('#inviteFriendModal').modal('hide');
            window.location.href = '/myPlanList';
        },

        error: function(xhr, status, error) {
            if (xhr.status === 409) {
                // 이미 초대된 친구일 때
                alert("이미 초대된 친구입니다.");
            } else if (xhr.status === 500) {
                // 서버 내부 오류
                alert("친구 초대 중 오류 발생");
            } else {
                // 기타 오류
                console.log('친구 초대 중 오류 발생: ', error);
            }
        }
    });
}
