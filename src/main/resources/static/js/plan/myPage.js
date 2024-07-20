let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function() {
    // 자세히 보기 버튼 클릭 시 이벤트 핸들러
    $(document).on('click', '.view-detail', function() {
        var planId = $(this).data('planid');
        if (planId) {
            window.location.href = '/viewDetailPlan?planId=' + planId;
        } else {
            console.log('planId 값이 존재하지 않습니다.');
        }
    });

    // 나가기 버튼 클릭 시 이벤트 핸들러
    $(document).on('click', '.delete-plan', function() {
        var planId = $(this).data('planid');
        var activeTabId = $('#travelPlanTabs .nav-link.active').attr('id');
        console.log('활성화된 탭 Id : ', activeTabId);

        if (planId) {
            $('#confirmExitModal').data('planid', planId).data('activeTabId', activeTabId).modal('show');
        } else {
            console.log('planId 값이 존재하지 않습니다.');
        }
    });

    // 모달에서 "나가기" 버튼 클릭 시 이벤트 핸들러
    $('#confirmExitButton').on('click', function() {
        var planId = $('#confirmExitModal').data('planid');
        var activeTabId = $('#confirmExitModal').data('activeTabId');
        deletePlan(planId, activeTabId);
        $('#confirmExitModal').modal('hide');
    });

    // 친구 초대 버튼 클릭 시 이벤트 핸들러
    $(document).on('click', '.invite', function() {
        var planId = $(this).data('planid');
        console.log('친구초대 버튼 클릭----planid: ', planId);
        $('#inviteFriendModal').data('planid', planId).modal('show');
        $('#inviteFriendButton').prop('disabled', true);

    });

    // 검색 버튼 클릭 시 이벤트 핸들러
    $('#searchFriendButton').on('click', function() {
        var email = $('#friendEmail').val();
        if (email) {
            searchFriendByEmail(email);
        } else {
            $('.search-results').empty();
        }
    });

    // 이메일 입력 필드에서 엔터키 입력 시 검색 이벤트 발생
    $('#friendEmail').keypress(function(event) {
        if (event.keyCode === 13) {
            $('#searchFriendButton').click();
        }
    });

    // 초대하기 버튼 클릭 시 이벤트 핸들러
    $('#inviteFriendButton').on('click', function() {
        var email = $('#friendEmail').val();
        var planId =  $('#inviteFriendModal').data('planid');
        console.log("초대할 친구의 email-----", email);
        console.log("초대할 planid-----", planId);
         inviteFriend(email, planId);
    });

});//$(document).ready(function() {

function deletePlan(planId, activeTabId) {
    console.log('삭제할 planId: ', planId);

    $.ajax({
        url: '/deletePlan',
        method: 'POST',
        data: { planId: planId },
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(response) {
            console.log('plan 삭제 성공');
            // ajax로 데이터 다시 불러오는 부분 추가
            updatePlanList(activeTabId);

        },
        error: function(xhr, status, error) {
            console.log('plan 삭제 중 오류 발생: ', error);
        }
    });
}

// 동적으로 planList 재구성 함수
function updatePlanList(activeTabId) {
    var url;
    var data = {};

    if (activeTabId === 'private-tab') {
        url = '/getPlans';
        data.activeTabId = activeTabId; // URL이 /getPlans일 때만 data를 보냄
    } else {
                window.location.reload();
    }
    $.ajax({
        url: url , //'/getPlans'-> '나의 여정'에 관한건 ajax로 처리 됨. '공유된 여정'은 안댐.
        data: { activeTabId: activeTabId },
        cache: false, // 캐시 비활성화
        success: function(data) {
            if (activeTabId === 'private-tab') {
                $('#private').html(data);
            } else {
                $('#shared').html(data);
            }
            console.log('tab - activeTabId : ', activeTabId);

            // 삭제 후 동적으로 불러온 컨텐츠에 대해 이벤트 핸들러 재설정
            $(document).on('click', '.view-detail', function() {
                var planId = $(this).data('planid');
                if (planId) {
                    window.location.href = '/viewDetailPlan?planId=' + planId;
                } else {
                    console.log('planId 값이 존재하지 않습니다.');
                }
            });

            $(document).on('click', '.delete-plan', function() {
                var planId = $(this).data('planid');
                var activeTabId = $('#travelPlanTabs .nav-link.active').attr('id');
                console.log('활성화된 탭 Id : ', activeTabId);

                if (planId) {
                    $('#confirmExitModal').data('planid', planId).data('activeTabId', activeTabId).modal('show');
                } else {
                    console.log('planId 값이 존재하지 않습니다.');
                }
            });
        },
        error: function(xhr, status, error) {
            console.log('Plan 리스트 업데이트 중 오류 발생: ', error);
        }
    });
}

// 친구 이메일로 검색 함수
function searchFriendByEmail(email) {
    $.ajax({
        url: '/searchFriendByEmail',
        method: 'GET',
        data: { email: email },
        success: function(response) {
            var resultHtml = '';
            if (response === true) {
                resultHtml = '<ul><li>' + email + '</li></ul>';
                $('#inviteFriendButton').prop('disabled', false); // 활성화

            } else {
                resultHtml = '<p>검색 결과가 없습니다.</p>';
                $('#inviteFriendButton').prop('disabled', true); // 비활성화

            }
            $('.search-results').html(resultHtml);
        },
        error: function(xhr, status, error) {
            console.log('친구 검색 중 오류 발생: ', error);
        }
    });
}

// 친구 초대 함수
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
            window.location.href = '/myPage';
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
