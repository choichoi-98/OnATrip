// openCity 함수 정의
function openCity(evt, cityName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}

// 초기 실행 함수
document.addEventListener("DOMContentLoaded", function () {
    var domesticCheckbox = document.getElementById('domestic_check');
    if (domesticCheckbox.checked) {
        toggleDomesticInputs();
    } else {
        toggleOverseasInputs(); // 초기 상태에서는 해외 체크박스가 선택되어 있을 수 있으므로 이 경우도 고려
    }

    document.getElementsByClassName("tablinks")[0].click();
});

// 국내 체크박스 선택 시 나라 입력란 활성화 처리
function toggleDomesticInputs() {
    var countryInput = document.getElementById('country_input');
    if (document.getElementById('domestic_check').checked) {
        countryInput.disabled = false;
    } else {
        countryInput.disabled = true;
    }
}

// 해외 체크박스 선택 시 나라 입력란 비활성화 처리
function toggleOverseasInputs() {
    var countryInput = document.getElementById('country_input');
    if (document.getElementById('overseas_check').checked) {
        countryInput.disabled = true;
    } else {
        countryInput.disabled = false;
    }
}

// 여행지 추가 버튼 클릭 시 처리
document.getElementById('add_location_btn').addEventListener('click', function () {
    var locationType = document.querySelector('input[name="locationType"]:checked');
    var countryInput = document.getElementById('country_input').value;
    var cityInput = document.getElementById('city_input').value;
    var fileInput = document.getElementById('file_input').value;

    if (locationType && ((locationType.value === 'domestic' && cityInput && fileInput) ||
        (locationType.value === 'overseas' && countryInput && cityInput && fileInput))) {
        var listItem = document.createElement('li');
        var listContent = document.createElement('div');
        var listImage = document.createElement('img');
        var locationName = document.createElement('h4');
        var locationDetails = document.createElement('p');

        listContent.className = 'location-info';
        listImage.src = 'placeholder.jpg'; // 이미지 경로 수정 필요
        locationName.textContent = locationType.value === 'domestic' ? '국내' : '해외';
        locationDetails.textContent = locationType.value === 'domestic' ?
            '도시: ' + cityInput : '나라: ' + countryInput + ', 도시: ' + cityInput;

        listContent.appendChild(locationName);
        listContent.appendChild(locationDetails);
        listItem.appendChild(listImage);
        listItem.appendChild(listContent);

        if (locationType.value === 'domestic') {
            document.getElementById('domestic_list').querySelector('ul').appendChild(listItem);
        } else if (locationType.value === 'overseas') {
            document.getElementById('overseas_list').querySelector('ul').appendChild(listItem);
        }

        // 초기화
        document.getElementById('reset_btn').click();
    } else {
        alert('모든 필드를 입력하고 체크박스를 선택해주세요.');
    }
});

// 초기화 버튼 클릭 시 처리
document.getElementById('reset_btn').addEventListener('click', function () {
    var countryInput = document.getElementById('country_input');
    var cityInput = document.getElementById('city_input');
    var fileInput = document.getElementById('file_input');
    var checkboxes = document.querySelectorAll('input[name="locationType"]');

    countryInput.value = '';
    cityInput.value = '';
    fileInput.value = '';

    checkboxes.forEach(function (checkbox) {
        checkbox.checked = false;
    });

    toggleDomesticInputs(); // 초기화 후 국내 체크박스 상태에 따라 입력란 활성화/비활성화 처리
    toggleOverseasInputs(); // 초기화 후 해외 체크박스 상태에 따라 입력란 활성화/비활성화 처리
});

// Location 탭 전환
function openLocationTab(evt, tabName) {
    var i, locationLists, locationTabs;
    locationLists = document.getElementsByClassName("location-list");
    for (i = 0; i < locationLists.length; i++) {
        locationLists[i].classList.remove("active");
    }
    locationTabs = document.getElementsByClassName("location-tab");
    for (i = 0; i < locationTabs.length; i++) {
        locationTabs[i].classList.remove("active");
    }
    document.getElementById(tabName).classList.add("active");
    evt.currentTarget.classList.add("active");
}

// 국내, 해외 체크박스 1개만 선택되도록
var checkboxes = document.querySelectorAll('input[name="locationType"]');
checkboxes.forEach(function (checkbox) {
    checkbox.addEventListener('change', function () {
        checkboxes.forEach(function (cb) {
            if (cb !== checkbox) {
                cb.checked = false;
            }
        });

        // 체크박스에 따라 입력 필드 활성화/비활성화 처리
        toggleDomesticInputs();
        toggleOverseasInputs();
    });
});
