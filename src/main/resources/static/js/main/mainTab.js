$(document).ready(function() {
    // 페이지 로드 시 결과 메시지 숨기기
    $('#no-results-all').hide();
    $('#no-results-domestic').hide();
    $('#no-results-international').hide();

    // 페이지 로드 시 모달 숨김 처리 (CSS에서 처리됨)
    $('#modal-root').hide();

    // 오늘 날짜 가져오기
    var today = new Date();
    today.setHours(0, 0, 0, 0); // 시간 정보를 0으로 설정하여 날짜만 비교

    // 각 위치 항목에 대해 endDate 확인
    $('[data-enddate]').each(function() {
        var endDate = new Date($(this).data('enddate'));
        endDate.setHours(0, 0, 0, 0); // 시간 정보를 0으로 설정하여 날짜만 비교

        if (today.getTime() === endDate.getTime()) {
            $(this).find('.new-badge').hide(); // 오늘 날짜와 일치하는 경우 'NEW' 배지 숨김
        } else {
            $(this).find('.new-badge').show(); // 오늘 날짜와 일치하지 않는 경우 'NEW' 배지 표시
        }
    });

    // 검색 입력 처리
    $('input[name="search"]').on('input', function() {
        var searchText = $(this).val().toLowerCase(); // 입력된 검색어를 소문자로 변환

        // 검색어와 일치하는 위치 항목 표시 여부
        var hasResultsAll = false;
        var hasResultsDomestic = false;
        var hasResultsInternational = false;

        // 전체 탭 검색 처리
        $('#tab-all > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                hasResultsAll = true;
            } else {
                $(this).hide();
            }
        });

        // 국내 탭 검색 처리
        $('#tab-domestic > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                hasResultsDomestic = true;
            } else {
                $(this).hide();
            }
        });

        // 해외 탭 검색 처리
        $('#tab-international > div').each(function() {
            var locationCity = $(this).find('.location-city').text().toLowerCase();
            var locationCountry = $(this).find('.location-country').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
                hasResultsInternational = true;
            } else {
                $(this).hide();
            }
        });

        // 검색 결과가 없을 경우 메시지 표시
        $('#no-results-all').toggle(!hasResultsAll && searchText.length > 0);
        $('#no-results-domestic').toggle(!hasResultsDomestic && searchText.length > 0);
        $('#no-results-international').toggle(!hasResultsInternational && searchText.length > 0);
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

        // 각 탭별로 처음에는 12개 항목만 표시하고 더보기 버튼 상태 업데이트
        showInitialItems(tabId);
        checkLoadMoreButton(tabId);
    }

    function showInitialItems(tabId) {
        var $tabContent = $(tabId + ' > div');
        $tabContent.hide(); // 모든 항목 숨김
        $tabContent.slice(0, 12).show(); // 처음 12개 항목만 표시
    }

    function checkLoadMoreButton(tabId) {
        // 더보기 버튼의 표시 여부 결정
        var $tabContent = $(tabId + ' > div');
        var visibleCount = $tabContent.filter(':visible').length;
        var totalCount = $tabContent.length;
        var $loadMoreButton = $(tabId === '#tab-all' ? '#load-more-all' :
                              tabId === '#tab-domestic' ? '#load-more-domestic' : '#load-more-international');

        // 12개 이상이 있을 경우에만 더보기 버튼 표시
        if (totalCount > 12) {
            $loadMoreButton.toggle(visibleCount < totalCount);
        } else {
            $loadMoreButton.hide();
        }
    }

    // 초기 로드 시 각 탭에 대해 12개 항목만 표시하고 더보기 버튼 확인
    showInitialItems('#tab-all');
    checkLoadMoreButton('#tab-all');

    showInitialItems('#tab-domestic');
    checkLoadMoreButton('#tab-domestic');

    showInitialItems('#tab-international');
    checkLoadMoreButton('#tab-international');

    // 더보기 버튼 클릭 처리
    $('#load-more-all-btn').click(function() {
        loadMoreContent('#tab-all');
    });

    $('#load-more-domestic-btn').click(function() {
        loadMoreContent('#tab-domestic');
    });

    $('#load-more-international-btn').click(function() {
        loadMoreContent('#tab-international');
    });

    function loadMoreContent(tabId) {
        var $tabContent = $(tabId + ' > div');
        var visibleCount = $tabContent.filter(':visible').length;
        var newVisibleCount = visibleCount + 12; // 12개 추가

        $tabContent.slice(visibleCount, newVisibleCount).show();
        checkLoadMoreButton(tabId);
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

    // 일정 만들기 버튼 클릭 처리
    $('#create-itinerary').click(function(event) {
        event.preventDefault(); // 기본 동작 막기

        var locationId = $(this).attr('data-location-id');
        var csrfToken = $('meta[name="_csrf"]').attr('content');

        // 서버에 인증 여부 확인 요청
        $.ajax({
            url: '/check-authentication',
            type: 'GET',
            dataType: 'json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
            },
            success: function(isAuthenticated) {
                if (isAuthenticated) {
                    // 인증된 사용자 -> 일정 만들기 페이지로 이동
                    window.location.href = '/selectDate?locationId=' + locationId;
                } else {
                    // 비회원 -> 로그인 경고 및 로그인 페이지로 이동
                    alert('로그인이 필요합니다.');
                    window.location.href = '/login'; // 로그인 페이지 URL로 수정
                }
            },
            error: function(xhr, status, error) {
                console.error('인증 확인 오류:', error);
                alert('오류가 발생했습니다. 나중에 다시 시도해 주세요.');
            }
        });
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
