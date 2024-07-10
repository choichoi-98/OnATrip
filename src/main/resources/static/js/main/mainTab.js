$(document).ready(function() {
    // 초기 설정: 전체 탭이 선택되어 있음
    $('#tab-all-btn').addClass('text-lightScheme-primary');
    $('#tab-all-btn').addClass('border-lightScheme-primary');

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
