$(document).ready(function () {
    // 공공데이터포털 API 키
    const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';

    // 탭 전환 함수
    function openCity(evt, cityName) {
        $(".tabcontent").hide();
        $(".tablinks").removeClass("active");
        $("#" + cityName).show();
        $(evt.currentTarget).addClass("active");
    }

    // 위치 탭 전환 함수
    function openLocationTab(evt, tabName) {
        $(".location-list").hide();
        $(".location-tab").removeClass("active");
        $("#" + tabName).show();
        $(evt.currentTarget).addClass("active");
    }

    // 전역 스코프에 함수 등록
    window.openCity = openCity;
    window.openLocationTab = openLocationTab;

    // 초기화 함수
    function resetInputs() {
        $('#country_input, #countryCode_input, #city_input').val('').removeClass('disabled-input').prop('readonly', false);
        $('#description_input, #file_input').val('');
        $('.preview-image').attr('src', '').hide();
    }

    // 국내 여행지 입력 필드 상태 토글 함수
    function toggleDomesticInputs() {
        resetInputs();
        if ($('#domestic_check').is(':checked')) {
            $('#domestic_check').prop('checked', true);
            $('#overseas_check').prop('checked', false);
            $('#country_input, #countryCode_input').addClass('disabled-input').prop('readonly', true);
            $('#city_input').removeClass('disabled-input').prop('readonly', false);
        } else {
            $('#country_input, #countryCode_input, #city_input').removeClass('disabled-input').prop('readonly', false);
        }
    }

    // 해외 여행지 입력 필드 상태 토글 함수
    function toggleOverseasInputs() {
        resetInputs();
        if ($('#overseas_check').is(':checked')) {
            $('#overseas_check').prop('checked', true);
            $('#domestic_check').prop('checked', false);
            $('#country_input').removeClass('disabled-input').prop('readonly', false);
            $('#countryCode_input, #city_input').addClass('disabled-input').prop('readonly', true);
        } else {
            $('#country_input, #countryCode_input, #city_input').removeClass('disabled-input').prop('readonly', false);
        }
    }

    // 전역 스코프에 함수 등록
    window.toggleDomesticInputs = toggleDomesticInputs;
    window.toggleOverseasInputs = toggleOverseasInputs;

    // 체크박스 변경 이벤트 리스너 등록
    $('#domestic_check').change(toggleDomesticInputs);
    $('#overseas_check').change(toggleOverseasInputs);

    // 여행지 추가 버튼 클릭 이벤트 처리
    $('#add_location_btn').click(function (event) {
        event.preventDefault(); // 폼 서브미션 방지

        var country = $('#country_input').val().trim();
        var countryCode = $('#countryCode_input').val().trim();
        var city = $('#city_input').val().trim();
        var description = $('#description_input').val().trim();
        var fileInput = document.getElementById('file_input');

        var locationType = $('#domestic_check').is(':checked') ? 'domestic' : ($('#overseas_check').is(':checked') ? 'overseas' : null);

        // 입력 유효성 검사
        if (locationType === 'domestic' && (city === '' || description === '' || fileInput.files.length === 0)) {
            alert('도시, 설명, 이미지를 모두 입력해야 합니다.');
            return;
        }

        if (locationType === 'overseas' && (country === '' || description === '' || fileInput.files.length === 0)) {
            alert('국가, 설명, 이미지를 모두 입력해야 합니다.');
            return;
        }

        // 이미지 파일 처리 및 추가 함수 호출
        var imageSrc = '';
        if (fileInput.files.length > 0) {
            var reader = new FileReader();
            reader.onload = function (e) {
                imageSrc = e.target.result;
                sendLocationDataToServer(country, countryCode, city, description, fileInput.files[0], locationType, imageSrc);
            };
            reader.readAsDataURL(fileInput.files[0]);
        } else {
            sendLocationDataToServer(country, countryCode, city, description, null, locationType, imageSrc);
        }
    });

    // 서버로 데이터 전송 함수 (AJAX)
    function sendLocationDataToServer(country, countryCode, city, description, file, locationType, imageSrc) {
        var formData = new FormData();
        formData.append('country', country);
        formData.append('countryCode', countryCode);
        formData.append('city', city);
        formData.append('description', description);
        if (file) {
            formData.append('file', file);
        }
        formData.append('locationType', locationType); // 이 부분에서 locationType을 직접 사용

        // CSRF 토큰 가져오기
        var csrfToken = $('meta[name="_csrf"]').attr('content');

        $.ajax({
            url: '/admin/addLocation',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken); // AJAX 요청 헤더에 CSRF 토큰 추가
            },
            success: function (response) {
                console.log('여행지 추가 성공');
                console.log('입력한 데이터:', {
                    locationType: locationType,
                    country: country,
                    countryCode: countryCode,
                    city: city,
                    description: description,
                    imageSrc: imageSrc
                });

                // 여행지 추가 성공 시 추가적인 UI 업데이트 등의 작업 수행
                addToLocationList(locationType === 'domestic' ? 'domestic_list' : 'overseas_list', country, countryCode, city, description, imageSrc);
                resetInputs();  // 입력 필드 초기화
                resetCheckboxes();  // 체크박스 초기화
            },
            error: function (error) {
                console.error('여행지 추가 실패:', error);
                // 실패 시 에러 처리
                // 적절한 오류 메시지 표시
            }
        });
    }

    // 체크박스 초기화 함수
    function resetCheckboxes() {
        $('#domestic_check, #overseas_check').prop('checked', false);
        toggleDomesticInputs();
    }

    // 여행지 목록에 추가하는 함수
    function addToLocationList(tabId, country, countryCode, city, description, imageSrc) {
        var $ul = $('#' + tabId + ' ul');
        var $li = $('<li></li>');

        // 위치 정보 추가
        var locationInfo = tabId === 'domestic_list' ? city + ' - ' + description : country + ' - ' + description;
        $li.append($('<span class="location-info"></span>').text(locationInfo));

        // 이미지 추가
        if (imageSrc !== '') {
            $li.append($('<img class="preview-image">').attr('src', imageSrc).attr('alt', '여행지 이미지'));
        }

        // 수정 버튼 추가
        $li.append($('<button>Edit</button>').click(function () {
            editLocation(country, countryCode, city, description, imageSrc, $li);
        }));

        // 삭제 버튼 추가
        $li.append($('<button>Delete</button>').click(function () {
            $li.remove();
        }));

        // 목록에 추가
        $ul.append($li);
    }

    // 수정 폼 생성 및 처리 함수
    function editLocation(country, countryCode, city, description, imageSrc, $targetLi) {
        // 수정 폼 요소 생성
        var $editForm = $('<div class="edit-form"></div>');
        var $countryInput = $('<input type="text">').val(country);
        var $countryCodeInput = $('<input type="text">').val(countryCode);
        var $cityInput = $('<input type="text">').val(city);
        var $descriptionInput = $('<textarea>').val(description);
        var $fileInput = $('<input type="file">');

        // 이미지 미리보기 엘리먼트
        var $previewImage = $('<img class="preview-image">').attr('src', imageSrc).attr('alt', '이미지 미리보기');

        // 이미지 변경 시 미리보기 업데이트
        $fileInput.change(function () {
            var reader = new FileReader();
            reader.onload = function (e) {
                $previewImage.attr('src', e.target.result);
            };
            reader.readAsDataURL(this.files[0]);
        });

        // 저장 버튼 클릭 이벤트 처리
        var $saveBtn = $('<button>Save</button>').click(function () {
            country = $countryInput.val();
            countryCode = $countryCodeInput.val();
            city = $cityInput.val();
            description = $descriptionInput.val();
            imageSrc = $previewImage.attr('src');

            // 여행지 정보 업데이트 함수 호출
            updateLocationInfo($targetLi, country, countryCode, city, description, imageSrc);

            // 수정 폼 제거
            $editForm.remove();
        });

        // 취소 버튼 클릭 이벤트 처리
        var $cancelBtn = $('<button>Cancel</button>').click(function () {
            $editForm.remove();
        });

        // 수정 폼에 요소 추가
        $editForm.append(
            $('<label>Country:</label>'), $countryInput, $('<br>'),
            $('<label>Country Code:</label>'), $countryCodeInput, $('<br>'),
            $('<label>City:</label>'), $cityInput, $('<br>'),
            $('<label>Description:</label>'), $descriptionInput, $('<br>'),
            $previewImage, $fileInput, $('<br>'),
            $saveBtn, $cancelBtn
        );

        // 수정 폼을 목록 요소 아래에 추가
        $targetLi.append($editForm);
    }

    // 여행지 정보 업데이트 함수
    function updateLocationInfo($targetLi, country, countryCode, city, description, imageSrc) {
        // 위치 정보 업데이트
        var locationInfo = country !== '' ? country + ' - ' + description : city + ' - ' + description;
        $targetLi.find('.location-info').text(locationInfo);

        // 이미지 업데이트
        $targetLi.find('.preview-image').attr('src', imageSrc);
    }

    // 한글국가명 -> iso2자리 변환
    function getCountryCode(countryName) {
        const url = 'http://apis.data.go.kr/1262000/CountryCodeService2/getCountryCodeList2?'
            + 'serviceKey=' + apiKey
            + '&returnType=JSON&cond[country_nm::EQ]=' + countryName;

        console.log('API 호출 URL:', url); // URL 확인용 로그 추가

        // Make the API call using jQuery's ajax method
        $.ajax({
            url: url,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                console.log('API 응답 데이터:', data); // API 응답 확인용 로그 추가
                if (data && data.data && data.data.length > 0) {
                    const countryCode = data.data[0].country_iso_alp2;
                    console.log('Country Code:', countryCode);
                    $('#countryCode_input').val(countryCode); // 국가 코드를 입력 필드에 설정
                } else {
                    console.error('국가 정보를 찾을 수 없습니다. 정확한 국가명인지 확인하세요.');
                }
            },
            error: function (xhr, status, error) {
                console.error('공공데이터포털 API 호출 중 오류 발생', error); // 오류 메시지 확인용 로그 추가
            }
        });
    }


    // 해외 여행지 국가명 입력 후 엔터 키를 누르면 국가 코드 검색
    $('#country_input').keypress(function (event) {
        if (event.which === 13) { // 13 is the Enter key
            event.preventDefault(); // Prevent default form submission (if any)
            var countryName = $(this).val();
            console.log(countryName);
            getCountryCode(countryName);
        }
    });
});
