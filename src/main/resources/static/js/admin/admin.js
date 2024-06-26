$(document).ready(function () {
    // 탭 전환 함수
    function openCity(evt, cityName) {
        $(".tabcontent").hide();
        $(".tablinks").removeClass("active");
        $("#" + cityName).show();
        $(evt.currentTarget).addClass("active");
    }

    window.openCity = openCity;  // Add to global scope

    // 기본적으로 첫 번째 탭을 활성화
    $(".tablinks").first().addClass("active");
    $(".tabcontent").first().show();

    function resetInputs() {
        $('#country_input').val('').removeClass('disabled-input').prop('readonly', false);
        $('#countryCode_input').val('').removeClass('disabled-input').prop('readonly', false);
        $('#city_input').val('').removeClass('disabled-input').prop('readonly', false);
        $('#description_input').val('');
        $('#file_input').val('');
    }

    function toggleDomesticInputs() {
        resetInputs();
        if ($('#domestic_check').is(':checked')) {
            $('#domestic_check').prop('checked', true);  // 자기 자신은 항상 체크된 상태로 유지
            $('#overseas_check').prop('checked', false); // 다른 체크박스는 해제
            $('#country_input').addClass('disabled-input').prop('readonly', true);
            $('#countryCode_input').addClass('disabled-input').prop('readonly', true);
            $('#city_input').removeClass('disabled-input').prop('readonly', false);
        } else {
            // 체크 해제될 경우 모든 입력 필드 활성화
            $('#country_input').removeClass('disabled-input').prop('readonly', false);
            $('#countryCode_input').removeClass('disabled-input').prop('readonly', false);
            $('#city_input').removeClass('disabled-input').prop('readonly', false);
        }
    }

    function toggleOverseasInputs() {
        resetInputs();
        if ($('#overseas_check').is(':checked')) {
            $('#overseas_check').prop('checked', true);  // 자기 자신은 항상 체크된 상태로 유지
            $('#domestic_check').prop('checked', false); // 다른 체크박스는 해제
            $('#country_input').removeClass('disabled-input').prop('readonly', false);
            $('#countryCode_input').addClass('disabled-input').prop('readonly', true);
            $('#city_input').addClass('disabled-input').prop('readonly', true);
        } else {
            // 체크 해제될 경우 모든 입력 필드 활성화
            $('#country_input').addClass('disabled-input').prop('readonly', true);
            $('#countryCode_input').addClass('disabled-input').prop('readonly', true);
            $('#city_input').removeClass('disabled-input').prop('readonly', false);
        }
    }

    // 이벤트 리스너
    $('#domestic_check').change(toggleDomesticInputs);
    $('#overseas_check').change(toggleOverseasInputs);

    // 여행지 추가 버튼 클릭 이벤트 처리
    $('#add_location_btn').click(function () {
        var country = $('#country_input').val().trim();
        var city = $('#city_input').val().trim();
        var description = $('#description_input').val().trim();
        var fileInput = document.getElementById('file_input');
        var tabId = $('#domestic_check').is(':checked') ? 'domestic_list' : 'overseas_list';

        // 입력 검증 로직 추가
        if ($('#domestic_check').is(':checked') && (city === '' || description === '' || fileInput.files.length === 0)) {
            alert('도시, 설명, 이미지를 모두 입력해야 합니다.');
            return; // 필드가 비어있으면 추가 작업을 중단
        }

        if ($('#overseas_check').is(':checked') && (country === '' || description === '' || fileInput.files.length === 0)) {
            alert('국가, 설명, 이미지를 모두 입력해야 합니다.');
            return; // 필드가 비어있으면 추가 작업을 중단
        }

        // 이미지 파일 정보를 가져오기
        var imageSrc = '';
        if (fileInput.files.length > 0) {
            var reader = new FileReader();
            reader.onload = function (e) {
                imageSrc = e.target.result;
                // 여행지 목록에 추가
                addToLocationList(tabId, country, city, description, imageSrc);
                // 추가 후 입력 필드 초기화
                resetInputs();
                resetCheckboxes();
            };
            reader.readAsDataURL(fileInput.files[0]);
        } else {
            // 이미지가 없는 경우도 추가
            addToLocationList(tabId, country, city, description, imageSrc);
            // 추가 후 입력 필드 초기화
            resetInputs();
            resetCheckboxes();
        }
    });

    function resetCheckboxes() {
        $('#domestic_check').prop('checked', false);
        $('#overseas_check').prop('checked', false);
        toggleDomesticInputs();
    }

    // 추가된 여행지 정보를 목록에 추가하는 함수
    function addToLocationList(tabId, country, city, description, imageSrc) {
        var $ul = $('#' + tabId + ' ul');
        var $li = $('<li></li>');
        var locationInfo = '';

        // 국내와 해외에 따라 보여줄 내용 변경
        if (tabId === 'domestic_list') {
            locationInfo = '<span class="location-info">' + city + ' - ' + description + '</span>';
        } else {
            locationInfo = '<span class="location-info">' + country + ' - ' + description + '</span>';
        }

        // 이미지가 있는 경우 이미지 추가
        if (imageSrc !== '') {
            var $img = $('<img>').attr('src', imageSrc).attr('alt', '여행지 이미지');
            $li.append($img);
        }

        // 위치 정보 추가
        $li.append(locationInfo);

        // 수정 버튼 추가
        var $editBtn = $('<button>Edit</button>').click(function () {
            // 수정 기능 구현 - 필요에 따라 input 태그 등을 추가하고 데이터를 입력할 수 있도록 구현
            var $editForm = $('<div class="edit-form"></div>');
            var $countryInput = $('<input>').attr('type', 'text').val(country);
            var $cityInput = $('<input>').attr('type', 'text').val(city);
            var $descriptionInput = $('<input>').attr('type', 'text').val(description);
            var $fileInput = $('<input>').attr('type', 'file');

            $editForm.append($countryInput, $cityInput, $descriptionInput, $fileInput);
            $li.append($editForm);

            $fileInput.change(function () {
                var reader = new FileReader();
                reader.onload = function (e) {
                    imageSrc = e.target.result;
                    $img.attr('src', imageSrc);
                };
                reader.readAsDataURL(this.files[0]);
            });

            var $saveBtn = $('<button>Save</button>').click(function () {
                country = $countryInput.val();
                city = $cityInput.val();
                description = $descriptionInput.val();
                $li.find('.location-info').text((tabId === 'domestic_list' ? city : country) + ' - ' + description);
                $editForm.remove();
            });
            var $cancelBtn = $('<button>Cancel</button>').click(function () {
                $editForm.remove();
            });

            $editForm.append($saveBtn, $cancelBtn);
        });
        $li.append($editBtn);

        // 삭제 버튼 추가
        var $deleteBtn = $('<button>Delete</button>').click(function () {
            // 삭제 기능 구현
            $li.remove();
        });
        $li.append($deleteBtn);

        // 목록에 추가
        $ul.append($li);
    }

    // 취소 버튼 클릭 이벤트 처리
    $('#reset_btn').click(function () {
        resetInputs();
        resetCheckboxes();
    });

    // 첫 번째 체크박스 초기 설정
    toggleDomesticInputs();

    // 여행지 탭 전환 함수
    function openLocationTab(evt, tabName) {
        $(".location-list").removeClass("active");
        $(".location-tab").removeClass("active");
        $("#" + tabName).addClass("active");
        $(evt.currentTarget).addClass("active");
    }

    window.openLocationTab = openLocationTab;  // Add to global scope
});
