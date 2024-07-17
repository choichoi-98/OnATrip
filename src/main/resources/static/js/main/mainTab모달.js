$(document).ready(function() {
    // 검색어 입력란에 입력될 때마다 이벤트 발생
    $('input[name="search"]').on('input', function() {
        var searchText = $(this).val().toLowerCase(); // 입력된 검색어를 소문자로 변환

        // 모든 위치를 숨김
        $('#tab-all > div').hide();
        $('#tab-domestic > div').hide();
        $('#tab-international > div').hide();

        // 검색어와 일치하는 위치만 보여줌
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

    // 탭 버튼 클릭 이벤트 처리
    $('#tab-all-btn').click(function() {
        // 탭 버튼 스타일 변경
        $(this).addClass('text-lightScheme-primary').removeClass('text-gray-500');
        $(this).addClass('border-lightScheme-primary').removeClass('border-transparent');

        // 다른 탭 스타일 초기화
        $('#tab-domestic-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-international-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-domestic-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');
        $('#tab-international-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');

        // 탭 내용 변경
        $('#tab-all').show();
        $('#tab-domestic').hide();
        $('#tab-international').hide();
    });

    $('#tab-domestic-btn').click(function() {
        // 탭 버튼 스타일 변경
        $(this).addClass('text-lightScheme-primary').removeClass('text-gray-500');
        $(this).addClass('border-lightScheme-primary').removeClass('border-transparent');

        // 다른 탭 스타일 초기화
        $('#tab-all-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-international-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-all-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');
        $('#tab-international-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');

        // 탭 내용 변경
        $('#tab-all').hide();
        $('#tab-domestic').show();
        $('#tab-international').hide();
    });

    $('#tab-international-btn').click(function() {
        // 탭 버튼 스타일 변경
        $(this).addClass('text-lightScheme-primary').removeClass('text-gray-500');
        $(this).addClass('border-lightScheme-primary').removeClass('border-transparent');

        // 다른 탭 스타일 초기화
        $('#tab-all-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-domestic-btn').addClass('text-gray-500').removeClass('text-lightScheme-primary');
        $('#tab-all-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');
        $('#tab-domestic-btn').addClass('border-transparent').removeClass('border-lightScheme-primary');

        // 탭 내용 변경
        $('#tab-all').hide();
        $('#tab-domestic').hide();
        $('#tab-international').show();
    });
});
