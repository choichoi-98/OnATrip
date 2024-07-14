$(document).ready(function() {
    // 페이지 로드 시 모달 숨김 처리
    $('#modal-root').hide();

    // 검색 입력 처리
    $('input[name="search"]').on('input', function() {
        var searchText = $(this).val().toLowerCase(); // 입력된 검색어를 소문자로 변환

        // 모든 위치 항목 숨기기
        $('#tab-all > div').hide();
        $('#tab-domestic > div').hide();
        $('#tab-international > div').hide();

        // 검색어와 일치하는 위치 항목 표시
        $('#tab-all > div').each(function() {
            var locationCity = $(this).find('.font-Montserrat:nth-child(1)').text().toLowerCase();
            var locationCountry = $(this).find('.text-sm').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
            }
        });

        $('#tab-domestic > div').each(function() {
            var locationCity = $(this).find('.font-Montserrat:nth-child(1)').text().toLowerCase();
            var locationCountry = $(this).find('.text-sm').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
            }
        });

        $('#tab-international > div').each(function() {
            var locationCity = $(this).find('.font-Montserrat:nth-child(1)').text().toLowerCase();
            var locationCountry = $(this).find('.font-Montserrat:nth-child(2)').text().toLowerCase();
            if (locationCity.includes(searchText) || locationCountry.includes(searchText)) {
                $(this).show();
            }
        });
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
    $('#tab-all, #tab-domestic, #tab-international').on('click', 'div', function() {
        var locationId = $(this).attr('data-location-id'); // 위치 ID 가져오기
        console.log(locationId);
        showModal(locationId);
    });

    // 모달 닫기 처리
    $('#modal-root').on('click', '.close-modal-btn, #close-button-inside-modal, .bg-black', function() {
        closeModal();
    });

    // 모달 열기 함수 수정
    function showModal(locationId) {
        // 선택된 위치의 세부 정보를 가져와서 모달에 표시하는 로직
        // 여기서는 모달 컨테이너를 페이드인하여 보여주는 예시
        $('#modal-root').fadeIn(); // 모달 컨테이너 표시

        // 선택된 위치의 세부 정보를 가져와서 모달 내용을 업데이트하는 로직 추가 필요
        // 예를 들어 AJAX를 사용하여 서버에서 데이터를 가져오는 경우
        // 이 예시에서는 고정된 컨텐츠를 보여주도록 구현되어 있습니다.
    }

    // 모달 닫기 함수
    function closeModal() {
        $('#modal-root').fadeOut(); // 모달 닫기
    }
});
