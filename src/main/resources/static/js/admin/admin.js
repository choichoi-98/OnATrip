$(document).ready(function() {
    // 페이지 로드 시 모든 여행지 목록을 불러오는 함수 호출
    loadAllLocations();

    //회원정보 불러오기
      loadMembers();

    // 엔터키 누를 때 기본 동작 방지
    $('#city_input, #country_input').keydown(function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
            }
    });

     // 모든 여행지 목록을 불러오는 함수
     function loadAllLocations() {
           $.ajax({
               url: '/admin/getAllLocations',
               type: 'GET',
               dataType: 'json',
               success: function(response) {
                   response.forEach(function(location) {
                   console.log('location-id: ',location.id);
                   console.log('location-country_name', location.countryName);
                       addToLocationList(
                           location.locationType === 'domestic' ? 'domestic_list' : 'overseas_list',
                           location.id,  // ID 추가
                           location.countryName,
                           location.countryCode,
                           location.city,
                           location.description,
                           location.imagePath  // 이미지 경로를 location.imagePath로 수정

                       );
                   });

               },
               error: function(error) {
                   console.error('위치 불러오기 오류:', error);
                   alert('위치를 불러오는 도중 오류가 발생했습니다. 다시 시도해주세요.');
               }
           });
     }

     // 여행지를 목록에 추가하는 함수
     function addToLocationList(listType, countryCode, city, description, imagePath) {
           // 이미지 경로를 절대 경로로 설정
           var absoluteImagePath = 'http://localhost:9100' + imagePath;

           // 여기에서 DOM을 조작하여 목록에 추가하는 로직을 구현할 수 있습니다.
           console.log(absoluteImagePath);  // 절대 경로가 제대로 출력되는지 확인

           // 예를 들어, DOM에 이미지를 추가하는 코드
           var imageElement = $('<img>').attr('src', absoluteImagePath);
           $('#imageContainer').append(imageElement);
     }

        // 도시명 검색 버튼 클릭 이벤트 처리
        $('#city_search_btn').click(function(event) {
            event.preventDefault(); // 기본 이벤트 방지

            var city = $('#city_input').val().trim();

            // 입력된 도시명이 있는지 서버에 확인 요청
            checkCityExistence(city);
        });

     // 서버로 도시명 검색 요청을 보내는 함수
     function checkCityExistence(cityName) {
            // CSRF 토큰 가져오기
            var csrfToken = $('meta[name="_csrf"]').attr('content');

            $.ajax({
                url: '/admin/checkCity',
                type: 'POST',
                data: {
                    cityName: cityName
                },
                beforeSend: function(xhr) {
                    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken); // AJAX 요청 헤더에 CSRF 토큰 추가
                },
                success: function(response) {
                    if (response.exists) {
                        alert('이미 존재하는 도시입니다.');
                    } else {
                        alert('사용할 수 있는 도시입니다.');
                    }
                },
                error: function(error) {
                    console.error('도시명 검색 중 오류 발생:', error);
                    alert('도시명 검색 중 오류가 발생했습니다. 다시 시도해주세요.');
                }
            });
     }

    // 공공데이터포털 API 키
    const apiKey = 'q1y2LxDgfm2mCBSf2WsLlYxlAW6%2BQgPj%2FGN%2B5Evoojnqak2OLBFMZF2rTJRwyzUeOjghsPTvt%2BHZwdWk0iT%2BmA%3D%3D';

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
    function sendLocationDataToServer(countryName, countryCode, city, description, file, locationType, imageSrc) {
        var formData = new FormData();
        formData.append('countryName', countryName);
        formData.append('countryCode', countryCode);
        formData.append('city', city);
        formData.append('description', description);
        formData.append('createDate', new Date()); // createDate 추가

        // 파일이 있는 경우에만 formData에 추가
        if (file) {
            formData.append('file', file);
        }

        // locationType이 null이면 반환
        if (locationType === null) {
            alert('여행지 타입을 선택하세요.');
            return;
        }
        formData.append('locationType', locationType);

        var csrfToken = $('meta[name="_csrf"]').attr('content');

        $.ajax({
            url: '/admin/addLocation',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
            },
            success: function(response) {
                console.log('여행지 추가 성공');
                console.log('입력한 데이터:', {
                    locationType: locationType,
                    countryName: countryName,
                    countryCode: countryCode,
                    city: city,
                    description: description,
                    createDate: formData.get('createDate') // 추가된 createDate 확인
                });

                // 여행지 추가 성공 시 추가적인 UI 업데이트 등의 작업 수행
                addToLocationList(locationType === 'domestic' ? 'domestic_list' : 'overseas_list', response.id, countryName, countryCode, city, description, response.imagePath);
                resetInputs();  // 입력 필드 초기화
                resetCheckboxes();  // 체크박스 초기화
            },
            error: function(error) {
                console.error('여행지 추가 실패:', error);
                // 실패 시 에러 처리
                // 적절한 오류 메시지 표시
            }
        });
    }//sendLocationDataToServer

    // 체크박스 초기화 함수
    function resetCheckboxes() {
        $('#domestic_check, #overseas_check').prop('checked', false);
        toggleDomesticInputs();
    }

    // 여행지를 목록에 추가하는 함수 수정
    function addToLocationList(listType, id, countryName, countryCode, city, description, imagePath) {
       var $ul = $('#' + listType + ' ul');
       var $li = $('<li></li>').data('locationId', id).addClass('location-item'); // location-item 클래스 추가

       var locationInfo = '';

       if (listType === 'domestic_list') {
           locationInfo = '<span class="location-info-title">도시:</span> ' + city +
                          '<br><span class="location-info-title">설명:</span> <span class="location-description">' + description + '</span>';
       } else if (listType === 'overseas_list') {
           locationInfo = '<span class="location-info-title">국가:</span> ' + countryName +
                          '<br><span class="location-info-title">설명:</span> <span class="location-description">' + description + '</span>';
       }

       $li.append($('<span class="location-info"></span>').html(locationInfo));

       if (imagePath !== '') {
           $li.append($('<img class="preview-image">').attr('src', 'http://localhost:9100' + imagePath).attr('alt', '여행지 이미지'));
       }

       $li.append($('<button>Edit</button>').click(function () {
           editLocation(listType, countryName, countryCode, city, description, imagePath, $li);
       }));

       $li.append($('<button>Delete</button>').click(function () {
           if (confirm('정말로 삭제하시겠습니까?')) {
               deleteLocation(id, $li);
           }
       }));

       $ul.append($li);
    }//addToLocationList


    // Edit 버튼 클릭 시 호출되는 함수
    function editLocation(type, countryName, countryCode, city, description, imageSrc, $targetLi) {
        if ($targetLi.find('.edit-form').length > 0) {
            return; // 이미 수정 폼이 열려 있으면 아무 작업도 하지 않음
        }

        var originalCountryName = countryName;
        var originalCountryCode = countryCode;
        var originalCity = city;
        var originalDescription = description;
        var originalImageSrc = imageSrc;

        // 위치 타입을 $targetLi에 저장
        $targetLi.data('locationType', type);

        var $editForm = $('<div class="edit-form"></div>');

        // 여행지 타입에 따라 입력 필드 설정
        var $countryInput = $('<input type="text">').val(type === 'overseas_list' ? countryName : '');
        var $countryCodeInput = $('<input type="text">').val(type === 'overseas_list' ? countryCode : '');
        var $cityInput = $('<input type="text">').val(type === 'domestic_list' ? city : '');
        var $descriptionInput = $('<textarea>').val(description).css('width', '100%').css('height','200px'); // DESCRIPTION 입력 폭 넓히기
        var $fileInput = $('<input type="file">');

        // 기존 데이터 readonly 처리
        if (type === 'domestic_list') {
            $countryInput.prop('readonly', true);
            $countryCodeInput.prop('readonly', true);
            $cityInput.prop('readonly', true);
        } else if (type === 'overseas_list') {
            $countryInput.prop('readonly', true);
            $cityInput.prop('readonly', true);
            $countryCodeInput.prop('readonly', true);
        }



        var $previewImage = $('<img class="preview-image">').attr('src', imageSrc).attr('alt', '이미지 미리보기');
        $fileInput.change(function () {
            var reader = new FileReader();
            reader.onload = function (e) {
                $previewImage.attr('src', e.target.result);
            };
            reader.readAsDataURL(this.files[0]);
        });

        var $saveBtn = $('<button>Save</button>').click(function () {
            var formData = new FormData();

            var locationId = $targetLi.data('locationId'); // 여행지의 고유 ID 가져오기
            formData.append('id', locationId); // 수정할 여행지의 ID 추가

            if (type === 'domestic_list') {
                formData.append('city', $cityInput.val());
            } else if (type === 'overseas_list') {
                formData.append('countryName', $countryInput.val());
                formData.append('countryCode', $countryCodeInput.val());
            }

            formData.append('description', $descriptionInput.val());

            var fileInput = $targetLi.find('input[type="file"]')[0];
            if (fileInput.files.length > 0) {
                formData.append('file', fileInput.files[0]);
            }

            var csrfToken = $('meta[name="_csrf"]').attr('content');

            $.ajax({
                url: '/admin/updateLocation',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                },
                success: function(response) {
                    console.log('여행지 정보 업데이트 성공:', response);

                    // 수정된 데이터만 업데이트
                    updateLocationInfo($targetLi, $countryInput.val(), $countryCodeInput.val(), $cityInput.val(), $descriptionInput.val(), '');

                    $targetLi.find('.edit-form').remove();
                    $targetLi.find('button:contains("Edit")').show();
                    $targetLi.find('button:contains("Delete")').show();
                },
                error: function(error) {
                    console.error('여행지 정보 업데이트 실패:', error);
                    alert('여행지 정보를 업데이트하는 중 오류가 발생했습니다. 다시 시도해주세요.');
                }
            });
        });

        var $cancelBtn = $('<button>Cancel</button>').click(function () {
            $countryInput.val(originalCountryName);
            $countryCodeInput.val(originalCountryCode);
            $cityInput.val(originalCity);
            $descriptionInput.val(originalDescription);
            $previewImage.attr('src', originalImageSrc);

            $editForm.remove();

            $targetLi.find('button:contains("Edit")').show();
            $targetLi.find('button:contains("Delete")').show();
        });

        $editForm.append(
            $('<label>Country:</label>'), $countryInput, $('<br>'),
            $('<label>Country Code:</label>'), $countryCodeInput, $('<br>'),
            $('<label>City:</label>'), $cityInput, $('<br>'),
            $('<label>Description:</label>'), $descriptionInput, $('<br>'),
            $previewImage, $fileInput, $('<br>'),
            $('<div class="button-container"></div>').append($saveBtn, $cancelBtn)
        );

        $targetLi.append($editForm);

        $targetLi.find('button:contains("Edit")').hide();
        $targetLi.find('button:contains("Delete")').hide();
    }


  // 여행지 정보 업데이트 함수
  function updateLocationInfo($targetLi, countryName, countryCode, city, description, imageSrc) {
      // 위치 정보 업데이트
      var locationInfo = '';
      if (countryCode !== '') {
          locationInfo = '국가: ' + countryName + '<br>설명: ' + description;
      } else {
          locationInfo = '도시: ' + city + '<br>설명: ' + description;
      }
      $targetLi.find('.location-info').html(locationInfo);

      // 이미지 업데이트
      $targetLi.find('.preview-image').attr('src', imageSrc);
  }

    /*// Country 검색 버튼 클릭 이벤트 처리
    $('#country_search_btn').click(function(event) {
        event.preventDefault(); // 기본 이벤트 방지

        var countryName = $('#country_input').val().trim();
        if (countryName !== '') {
            console.log('-------Country_search_btn ---:검색할 국가명:', countryName);

            // AJAX 요청으로 DB에 국가 존재 여부 확인
            checkCountryExistence(countryName);
        } else {
            console.log('----------Country_search_btn: 검색창에 아무것도 없을떄')
            alert('국가명을 입력하세요.');
        }
    });*/

    // 서버로 국가명 검색 요청을 보내는 함수
    function checkCountryExistence(countryName) {
        // CSRF 토큰 가져오기
        var csrfToken = $('meta[name="_csrf"]').attr('content');
        console.log('checkCountryExistence :', countryName);
        $.ajax({
            url: '/admin/checkCountry',
            type: 'POST',
            data: {
                countryName: countryName
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken); // AJAX 요청 헤더에 CSRF 토큰 추가
            },
            success: function(response) {
                if (response.exists) {
                    alert('이미 존재하는 국가입니다.');
                    console.log('db에 국가 있음: ',response)
                } else {
                    // 국가가 DB에 없으면 API로부터 국가 코드 가져오기
                    console.log('db에 국가명 겹치는 것 없음')
                    getCountryCode(countryName);
                }
            },
            error: function(error) {
                console.error('국가명 검색 중 오류 발생:', error);
                alert('국가명 검색 중 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    }

    //한글국가명 -> iso2자리 변환
    function getCountryCode(countryName){
                               const url = 'http://apis.data.go.kr/1262000/CountryCodeService2/getCountryCodeList2?'
                                           +'serviceKey=' + apiKey
                                           + '&returnType=JSON&cond[country_nm::EQ]='+countryName ;
           $.ajax({
               url: url,
               method: 'GET',
               dataType: 'json',
               success: function(data) {
                   console.log('API 응답 데이터:', data);
                   if (data && data.data && data.data.length > 0) {
                       const countryCode = data.data[0].country_iso_alp2;
                       $('#countryCode_input').val(countryCode);
                   } else {
                       console.error('국가 정보를 찾을 수 없습니다. 정확한 국가명인지 확인하세요.');
                       alert('정확한 국가명을 입력해주세요.');
                   }
               },
             error: function(xhr, status, error) {
                 console.error('국가 코드를 가져오는 중 오류 발생:', error);
                 alert('국가 코드를 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.');
             }

           });
       }

      // 국가 입력 필드에 대한 이벤트 리스너 추가
      $('#country_search_btn').click(function(event) {
          event.preventDefault(); // 기본 이벤트 방지

          var countryName = $('#country_input').val().trim();
          if (countryName !== '') {
              console.log('검색할 국가명:', countryName);
              // 국가 존재 여부 확인 후 필요한 작업 수행
              checkCountryExistence(countryName);
          } else {
              alert('국가명을 입력하세요.');
          }
      });

      // 도시명 검색 버튼 클릭 이벤트 처리
      $('#search_city_btn').click(function(event) {
          event.preventDefault(); // 기본 이벤트 방지

          var city = $('#city_input').val().trim();

          // 입력된 도시명이 있는지 서버에 확인 요청
          checkCityExistence(city);
      });

      // 서버로 도시명 검색 요청을 보내는 함수
      function checkCityExistence(cityName) {
          // CSRF 토큰 가져오기
          var csrfToken = $('meta[name="_csrf"]').attr('content');

          $.ajax({
              url: '/admin/checkCity',
              type: 'POST',
              data: {
                  cityName: cityName
              },
              beforeSend: function(xhr) {
                  xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken); // AJAX 요청 헤더에 CSRF 토큰 추가
              },
              success: function(response) {
                  if (response.exists) {
                      alert('이미 존재하는 도시입니다.');
                  } else {
                      alert('사용할 수 있는 도시입니다.');
                  }
              },
              error: function(error) {
                  console.error('도시명 검색 중 오류 발생:', error);
                  alert('도시명 검색 중 오류가 발생했습니다. 다시 시도해주세요.');
              }
          });
      }

  // 여행지 목록 수정
  // 여행지 정보 업데이트 함수
  function updateLocationData($targetLi, countryName, countryCode, city, description, imageSrc) {
      var formData = new FormData();
      var locationId = $targetLi.data('locationId'); // 여행지의 고유 ID 가져오기
      formData.append('id', locationId); // 수정할 여행지의 ID 추가

      var locationType = $targetLi.data('locationType'); // 위치 타입 가져오기

      if (locationType === 'domestic') {
          formData.append('city', city);
      } else if (locationType === 'overseas') {
          formData.append('countryName', countryName);
          formData.append('countryCode', countryCode);
      }

      formData.append('description', description);

      var fileInput = $targetLi.find('input[type="file"]')[0];
      if (fileInput.files.length > 0) {
          formData.append('file', fileInput.files[0]);
      }

      var csrfToken = $('meta[name="_csrf"]').attr('content');

      $.ajax({
          url: '/admin/updateLocation',
          type: 'POST',
          data: formData,
          processData: false,
          contentType: false,
          beforeSend: function(xhr) {
              xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
          },
          success: function(response) {
              console.log('여행지 정보 업데이트 성공:', response);

              // 여행지 정보 업데이트 함수 호출
              updateLocationInfo($targetLi, countryName, countryCode, city, description, imageSrc);

              // 수정 폼 제거
              $targetLi.find('.edit-form').remove();

              // Edit 버튼 다시 보이기
              $targetLi.find('button:contains("Edit")').show();

              // Delete 버튼 다시 보이기
              $targetLi.find('button:contains("Delete")').show();
          },
          error: function(error) {
              console.error('여행지 정보 업데이트 실패:', error);
              alert('여행지 정보를 업데이트하는 중 오류가 발생했습니다. 다시 시도해주세요.');
          }
      });
  }

  // 여행지 삭제
  function deleteLocation(id, $li) {

      var csrfToken = $('meta[name="_csrf"]').attr('content');

      $.ajax({
          url: '/admin/deleteLocation/' + id,
          type: 'DELETE',
          beforeSend: function(xhr) {
                        xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
          },
          success: function (response) {
              console.log('여행지 삭제 성공');
              $li.remove(); // Remove the li element from the DOM on successful deletion
          },
          error: function (error) {
              console.error('여행지 삭제 실패:', error);
              alert('여행지 삭제 실페'); // Show an alert in case of error
          }
      });
  }

  //회원리스트 불러오기
  function loadMembers() {
      $.ajax({
          url: '/admin/manageMember',
          method: 'GET',
          dataType: 'json',
          success: function(data) {
              console.log('서버 응답 데이터:', data);
              var memberTableBody = $('#memberTableBody');
              memberTableBody.empty(); // 기존 내용 지우기

              $.each(data, function(index, member) {
                  var row = `
                      <tr>
                          <td class="td">${member.id}</td>
                          <td class="td">${member.email}</td>
                          <td class="td">${member.name}</td>
                          <td class="td">${member.subscribe_status}</td>
                          <td class="td"><button class="deleteButton" onclick="deleteMember(${member.id})">삭제</button></td>
                      </tr>
                  `;

                  memberTableBody.append(row);
              });
          },
          error: function(jqXHR, textStatus, errorThrown) {
              console.error('회원 목록을 가져오는 중 오류 발생:', textStatus, errorThrown); // 오류 로그
          },
          complete: function(jqXHR, textStatus) {
              console.log('AJAX 요청 완료:', textStatus); // 요청 완료 로그
          }
      });
  }


  function deleteMember(id) {
      $.ajax({
          url: '/admin/manageMember/delete/' + id,
          method: 'POST',
          dataType: "json",
          success: function() {
              loadMembers(); // 삭제 후 테이블을 다시 로드하여 갱신
              console.log("회원 삭제 성공:");
          },
          error: function(jqXHR, textStatus, errorThrown) {
              console.error('회원 삭제 중 오류가 발생했습니다:', textStatus, errorThrown);
          }
      });
  }

  // 전역 스코프에 함수 등록
  window.deleteMember = deleteMember;

});