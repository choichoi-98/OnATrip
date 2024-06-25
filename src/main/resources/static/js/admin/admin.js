   $(document).ready(function() {
        // 탭 전환 함수
        function openCity(evt, cityName) {
            $(".tabcontent").hide();
            $(".tablinks").removeClass("active");
            $("#" + cityName).show();
            $(evt.currentTarget).addClass("active");
        }

        // DOMContentLoaded 이벤트 핸들러
        $(function() {
            var domesticCheckbox = $('#domestic_check');
            var overseasCheckbox = $('#overseas_check');

            if (domesticCheckbox.prop('checked')) {
                toggleDomesticInputs();
            } else {
                toggleOverseasInputs();
            }

            $(".tablinks").eq(0).click(); // 기본적으로 첫 번째 탭을 활성화
        });

        // 국내 체크박스 처리 함수
        function toggleDomesticInputs() {
            var countryInput = $('#country_input');
            var overseasCheckbox = $('#overseas_check');

            if ($('#domestic_check').prop('checked')) {
                countryInput.prop('disabled', false).removeClass('disabled-input');
                overseasCheckbox.prop('checked', false); // 해외 체크박스 해제
            } else {
                countryInput.prop('disabled', true).addClass('disabled-input');
            }
        }

        // 초기화 버튼 클릭 처리
        $('#reset_btn').click(function() {
            $('#country_input, #city_input, #file_input').val('');

            $('input[name="locationType"]').prop('checked', false);

            if ($('#domestic_check').prop('checked')) {
                toggleDomesticInputs();
            } else if ($('#overseas_check').prop('checked')) {
                toggleOverseasInputs();
            }
        });

        // 해외 체크박스 처리 함수
        function toggleOverseasInputs() {
            var countryInput = $('#country_input');
            var domesticCheckbox = $('#domestic_check');

            if ($('#overseas_check').prop('checked')) {
                countryInput.prop('disabled', false);
                domesticCheckbox.prop('checked', false); // 국내 체크박스 해제
            } else {
                countryInput.prop('disabled', true);
            }
        }

        // 여행지 추가 버튼 클릭 처리
        $('#add_location_btn').click(function() {
            var locationType = $('input[name="locationType"]:checked').val();
            var countryInput = $('#country_input').val();
            var cityInput = $('#city_input').val();
            var fileInput = $('#file_input').val();

            if (locationType && ((locationType === 'domestic' && cityInput && fileInput) ||
                (locationType === 'overseas' && countryInput && cityInput && fileInput))) {

                var listItem = $('<li>');
                var listContent = $('<div>').addClass('location-info');
                var listImage = $('<img>').attr('src', 'placeholder.jpg'); // 이미지 경로 수정 필요
                var locationName = $('<h4>').text(locationType === 'domestic' ? '국내' : '해외');
                var locationDetails = $('<p>').text(locationType === 'domestic' ?
                    '도시: ' + cityInput : '나라: ' + countryInput + ', 도시: ' + cityInput);

                listContent.append(locationName);
                listContent.append(locationDetails);
                listItem.append(listImage);
                listItem.append(listContent);

                var editButton = $('<button>').text('수정').addClass('btn');
                editButton.click(function() {
                    editLocation(listItem);
                });

                var deleteButton = $('<button>').text('삭제').addClass('btn cancel');
                deleteButton.click(function() {
                    deleteLocation(listItem);
                });

                listItem.append(editButton);
                listItem.append(deleteButton);

                if (locationType === 'domestic') {
                    $('#domestic_list ul').append(listItem);
                } else if (locationType === 'overseas') {
                    $('#overseas_list ul').append(listItem);
                }

                $('#reset_btn').click();
            } else {
                alert('모든 필드를 입력하고 체크박스를 선택해주세요.');
            }
        });

        // 여행지 수정 처리 함수
        function editLocation(listItem) {
            var locationType = listItem.find('h4').text() === '국내' ? 'domestic' : 'overseas';
            var country = '';
            var city = '';

            if (locationType === 'domestic') {
                city = listItem.find('.location-info > p').text().replace('도시: ', '');
            } else {
                var details = listItem.find('.location-info > p').text().split(', ');
                country = details[0].replace('나라: ', '');
                city = details[1].replace('도시: ', '');
            }

            var editForm = $('<div>').addClass('edit-form').html(`
                <label>
                    <input type="checkbox" id="edit_domestic_check" name="edit_locationType" value="domestic">
                    국내
                </label>
                <label>
                    <input type="checkbox" id="edit_overseas_check" name="edit_locationType" value="overseas">
                    해외
                </label>
                <br>
                <label for="edit_country_input">나라:</label><br>
                <input type="text" id="edit_country_input" name="edit_country" placeholder="나라를 입력하세요" value="${country}">
                <br>
                <label for="edit_city_input">도시:</label><br>
                <input type="text" id="edit_city_input" name="edit_city" placeholder="도시를 입력하세요" value="${city}">
                <br>
                <label for="edit_file_input">이미지:</label><br>
                <input type="file" id="edit_file_input" name="edit_file">
                <br>
                <button id="save_edit_btn" class="btn">저장</button>
                <button id="cancel_edit_btn" class="btn cancel">취소</button>
            `);

            listItem.find('.location-info').hide().after(editForm);

            var editDomesticCheckbox = editForm.find('#edit_domestic_check');
            var editOverseasCheckbox = editForm.find('#edit_overseas_check');

            // 수정 폼에서 국내, 해외 체크박스 중 하나만 선택되도록 제어
            if (locationType === 'domestic') {
                editDomesticCheckbox.prop('checked', true);
                editOverseasCheckbox.prop('disabled', true);
            } else {
                editOverseasCheckbox.prop('checked', true);
                editDomesticCheckbox.prop('disabled', true);
            }

            $('#cancel_edit_btn').click(function() {
                listItem.find('.location-info').show();
                editForm.remove();
            });

            $('#save_edit_btn').click(function() {
                var editedLocationType = $('input[name="edit_locationType"]:checked').val();
                var editedCountry = $('#edit_country_input').val();
                var editedCity = $('#edit_city_input').val();
                var editedImage = $('#edit_file_input').val(); // 파일 업로드 처리 필요

                if (editedLocationType && ((editedLocationType === 'domestic' && editedCity && editedImage) ||
                    (editedLocationType === 'overseas' && editedCountry && editedCity && editedImage))) {
                    listItem.find('h4').text(editedLocationType === 'domestic' ? '국내' : '해외');
                    listItem.find('.location-info > p').text(editedLocationType === 'domestic' ?
                        '도시: ' + editedCity : '나라: ' + editedCountry + ', 도시: ' + editedCity);

                    editForm.remove();
                    listItem.find('.location-info').show();

                    // 수정 완료 후 체크박스 다시 활성화
                    editDomesticCheckbox.prop('disabled', false);
                    editOverseasCheckbox.prop('disabled', false);
                } else {
                    alert('모든 필드를 입력하고 체크박스를 선택해주세요.');
                }
            });
        }

        // 여행지 삭제 처리 함수
        function deleteLocation(listItem) {
            listItem.remove();
        }

        // Location 탭 전환 함수
        function openLocationTab(evt, tabName) {
            $(".location-list").removeClass("active");
            $(".location-tab").removeClass("active");
            $("#" + tabName).addClass("active");
            $(evt.currentTarget).addClass("active");
        }
    });