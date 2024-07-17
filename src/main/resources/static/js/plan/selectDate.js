// 선언된 변수를 전역에서 함수 내부로 이동하고 함수의 인자로 전달하여 사용하도록 수정
$(document).ready(function(){
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    console.log(token);
    console.log(header);

    // moment.js 한국어 설정 로드
    moment.locale('ko');

    $('#datepicker-container').daterangepicker({
        autoUpdateInput: false,
        alwaysShowCalendars: true,
        minDate: moment(),
        locale: {
            cancelLabel: '지우기',
            applyLabel: '적용',
            customRangeLabel: '사용자 지정',
            daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
            monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            firstDay: 0,
            format: 'YYYY/MM/DD',
            separator: ' - ',
            applyButtonClasses: 'btn-primary',
            cancelButtonClasses: 'btn-secondary'
        }
    });

    // apply event
    $('#datepicker-container').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
    });

    // cancel event
    $('#datepicker-container').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });

    $('#datepicker-container').on('click', function() {
        $(this).trigger('click');
    });

    //---완료 버튼 클릭 시
    $('.btn-get-started').on('click', function(){
        var dateRange = $('#datepicker-container').val();
        var locationId = $(this).data('location-id');

        console.log(dateRange);

        if(!dateRange){
            $('#datepicker-container').focus();
        } else {
            var dates = dateRange.split('-');
            var startDate = dates[0];
            var endDate = dates[1];
            console.log(startDate);
            console.log(endDate);
            console.log('location-id : ', locationId);
            $.ajax({
                url: '/createPlan',
                type: 'POST',
                contentType: 'application/json', // 보내는 데이터의 타입을 JSON으로 명시
                data: JSON.stringify({
                    startDate: startDate,
                    endDate: endDate,
                    memberId: 1, //-----임시, 추후 수정 필요
                    locationId: locationId // 위치 ID 추가

                }),
                beforeSend: function(xhr) {
                    // 데이터를 전송하기 전에 헤더에 csrf값을 설정
                    xhr.setRequestHeader(header, token);
                },
                success: function(planId) {
                    console.log("Plan db삽입 성공");
                    console.log('planId for createDetailPlan', planId);
                    createDetailPlan(dateRange, planId, header, token);
                },
                error: function(xhr, status, error) {
                    console.error('Plan db삽입 실패', status, error);
                }
            });
        }
    });
});

//-------createDetailPlan
function createDetailPlan(dateRange, planId, header, token) {
    console.log('createDetailPlan - dateRange : ', dateRange);
    console.log('createDetailPlan, ajax요청-createDetailPlan', planId);
    $.ajax({
        url: 'createDetailPlan',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
               dateRange: dateRange,
               planId: planId
        }),
        beforeSend: function(xhr) {
            // 데이터를 전송하기 전에 헤더에 csrf값을 설정
            xhr.setRequestHeader(header, token);
        },
        success: function(response) {
            console.log('detailPlan 성공적으로 추가되었습니다.:', response.planId);
            window.location.href = '../viewDetailPlan?planId=' + encodeURIComponent(response.planId);
        },
        error: function(xhr, status, error) {
            console.error('detailPlan-createDetailPlan 중 오류 발생:', status, error);
        }
    });
}

//-------------------------- viewDetailPlan
function viewDetailPlan(planId, header, token) {
        console.log('veiewDetailPlan-planId:',planId);
    $.ajax({
        url: '../detailPlan', // GET 요청을 보내는 URL
        method: 'GET',
        data: {
            planId: planId
        },
        beforeSend: function(xhr) {
            // 데이터를 전송하기 전에 헤더에 csrf값을 설정
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            console.log('GET 요청 성공:', data);

        },
        error: function(xhr, status, error) {
            console.error('GET 요청 중 오류 발생:', status, error);
        }
    });
}
