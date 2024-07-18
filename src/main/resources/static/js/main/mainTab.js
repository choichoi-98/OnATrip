$(document).ready(function() {
    // 페이지 로드 시 모달 숨김 처리 (CSS에서 처리됨)
    $('#modal-root').hide();

    // 오늘 날짜 가져오기
    var today = new Date();
    today.setHours(0, 0, 0, 0); // 시간 정보를 0으로 설정하여 날짜만 비교

    // 각 위치 항목에 대해 endDate 확인
    $('[data-enddate]').each(function() {
        var endDate = new Date($(this).data('enddate'));
        endDate.setHours(0, 0, 0, 0); // 시간 정보를 0으로 설정하여 날짜만 비교

        if (today.getTime() !== endDate.getTime()) {
            $(this).find('.new-badge').show();
        }
    });

    // 검색 입력 처리
    $('input[name="search"]').on('input', function() {
        var searchText = $(this).val().toLowerCase(); // 입력된 검색어를 소문자로 변환

        // 모든 위치 항목 숨기기
        $('#tab-all > div').hide();
        $('#tab-domestic > div').hide();
        $('#tab-international > div').hide();

        // 검색어와 일치하는 위치 항목 표시
        var allResults = 0;
        var domesticResults = 0;
        var internationalResults = 0;

        $('#tab-all > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                allResults++;
            }
        });

        $('#tab-domestic > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                domesticResults++;
            }
        });

        $('#tab-international > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                internationalResults++;
            }
        });

        // 검색 결과가 없을 경우 메시지 표시
        if (allResults === 0) {
            $('#no-results-all').show();
        } else {
            $('#no-results-all').hide();
        }

        if (domesticResults === 0) {
            $('#no-results-domestic').show();
        } else {
            $('#no-results-domestic').hide();
        }

        if (internationalResults === 0) {
            $('#no-results-international').show();
        } else {
            $('#no-results-international').hide();
        }
    });

    // 탭 클릭 처리
    $('#tab-all-btn').click(function() {
        activateTab('#tab-all', this);
    });

    $('#tab-domestic-btn').click(function() {
        activateTab('#tab-domestic', this);
    });

    $('#tab-international-btn').click(function() {
        activateTab('#tab-international', this);
    });

    function activateTab(tabId, button) {
        // 클릭된 탭 버튼 활성화
        $(button).addClass('text-lightScheme-primary').removeClass('text-gray-500');
        $(button).addClass('border-lightScheme-primary').removeClass('border-transparent');

        // 다른 탭 버튼 비활성화
        $('#tab-all-btn, #tab-domestic-btn, #tab-international-btn').not(button).addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-all-btn, #tab-domestic-btn, #tab-international-btn').not(button).addClass('border-transparent').removeClass('border-lightScheme-primary');

        // 해당 탭 콘텐츠 표시
        $('#tab-all, #tab-domestic, #tab-international').not(tabId).hide();
        $(tabId).show();
    }

    // 목적지 클릭 시 모달 열기
    $('#tab-all, #tab-domestic, #tab-international').on('click', '.h-auto.overflow-hidden', function() {
        var locationId = $(this).attr('data-location-id'); // 위치 ID 가져오기 (data- 속성 사용)
        showModal(locationId);
    });

    // 모달 닫기 처리
    $('#modal-root').on('click', '.close-modal-btn, #close-button-inside-modal, .bg-black', function() {
        closeModal();
    });

   // 모달 열기 함수 수정
   function showModal(locationId) {
       console.log('Received locationId:', locationId); // locationId 확인용 로그

       var csrfToken = $('meta[name="_csrf"]').attr('content');
       $.ajax({
           url: '/location/' + locationId,
           type: 'GET',
           dataType: 'json',
            beforeSend: function(xhr) {
                           xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
            },
           success: function(data) {
               // 서버에서 받은 데이터를 모달에 표시
               console.log('Received Data:', data);  // 데이터 콘솔에 출력
               $('#location-city').text(data.city);
               $('#location-country').text(data.countryName);
               $('#location-image').attr('src', data.imagePath);
               console.log('Image Path:', data.imagePath);  // 이미지 경로 콘솔에 출력
               $('#location-description').text(data.description);

               // 일정 만들기 버튼에 locationId 설정
               $('#create-itinerary').attr('data-location-id', locationId);

               // 모달을 페이드인하여 보여줌
               $('#modal-root').fadeIn();
           },
           error: function(xhr, status, error) {
               console.error('위치 데이터 가져오기 오류:', error);
               console.log('XHR 응답:', xhr);

               alert('위치 데이터를 가져오지 못했습니다. 나중에 다시 시도해 주세요.');
           }
       });
   }



    // 모달 닫기 함수
    function closeModal() {
        $('#modal-root').fadeOut(); // 모달 닫기
    }
});
