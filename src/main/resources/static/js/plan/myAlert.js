$(document).ready(function() {
    console.log('Alerts section is loaded.');

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
                                <p class="alert-recipient">${alert.memberEmail}님이 초대하셨습니다.</p>
                            </div>
                            <div>
                                <button class="btn accept-btn" style="background-color: #33333; border-color: #a9a9a9; color: #333; font-size: 0.9rem; padding: 0.4rem 0.8rem; border-radius: 0.3rem;" data-alert-id="${alert.id}">수락</button>
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
        $.ajax({
            url: '/alerts/acceptAlert/' + alertId,
            type: 'POST',
            success: function(response) {
                console.log('Alert accepted:', response);
                // 버튼 클릭 후 UI 업데이트 로직 추가 가능
                $(this).closest('.alert-item').remove();
            },
            error: function(xhr, status, error) {
                console.error('Failed to accept alert:', error);
            }
        });
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
});
