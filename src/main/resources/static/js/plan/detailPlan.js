let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let map;
let countryName;
  //REST Countries API 엔드포인트 URL
const apiUrl = "https://restcountries.com/v3/alpha/";
let dayNumber;//일차
let detailPlanId;
let category;//memo, place
let markers = [];//마커들 저장
let markerIndex = 1;
let polyline;
let originalIcon;// 마커 강조 함수

$(document).ready(function() {

    const countryName = $('#countryHeading').text().trim();
    const countryCode = $('#countryHeading').data('countrycode');
    console.log('countryCode------------: ', countryCode);

//
//    if (countryCode) {
//        // countryCode가 존재하는 경우
//        getCountryInfo(countryCode); // 위도, 경도 구함
//        initAutocomplete(countryCode); // 장소검색 호출
//    } else {
//        // countryCode가 null인 경우
//        const latlng = '';
//        initMap(latlng); // 지도 초기화 호출
//        initAutocomplete('KR'); // 한국으로 검색 지역 설정
//    }

    setBadgeColor();

    //------------------addRoute('장소추가' -> '추가'_db에 데이터 삽입)
    $(document).on('click', '.add-route', function(e) {
            category = 'PLACE';
            e.preventDefault();
            console.log("-------------addRoute.on.click-----------");
            const day = $(this).data('day');
            const detailPlanId = $(this).data('detailplanid');
            const lat = $(this).data('lat');
            const lng = $(this).data('lng');
            const placeName = $(this).closest('.card').find('.card-title').text();
            addRoute(day, placeName, category,detailPlanId, lat, lng);
    });

    //-----------------------------addMeMo
   $(document).on('click', '#addMemo', function(e){
        category = 'MEMO';
        e.preventDefault();
        const memoInput = $(this).siblings('input');
        const memoText = memoInput.val().trim();
        if(memoText){
            const day = $(this).closest('.input-group').attr('id').split('-')[1];
            const detailPlanId = $(this).data('detailplanid');
            addRoute(day,memoText, category , detailPlanId,null,null);
            memoInput.val('');
        }

   });

   //-------------------route 삭제
        var routeIdToDelete = null;

       $(document).on('click', '.delete-icon', function() {
           routeIdToDelete = $(this).data('routeid');
           $('#deleteConfirmModal').modal('show');

           dayNumber = $(this).data('daynum');
           detailPlanId = $(this).data('detailplanid');
           category = $(this).data('category');

            var closestDiv = $(this).siblings('div').first();
            var classNames = closestDiv.attr('class');

       });

       $('#confirmDelete').on('click', function() {
           if (routeIdToDelete) {
                deleteRoute(routeIdToDelete, dayNumber, detailPlanId, category);
               $('#deleteConfirmModal').modal('hide');
           }
       });


   //----------------메모 수정
  $(document).on('click', '.memo-block .modify-icon', function() {
        let memoContent = $(this).siblings('h6').text();
        let modifyRouteId = $(this).data('routeid');
        let dayNumber = $(this).data('daynum');

        detailPlanId = $(this).data('detailplanid');

        $('#memoContent').val(memoContent);

        $('#modifyMemoModal').modal('show');

        $('#modifyMemoModal').data('currentMemo', $(this).siblings('h6'));
        $('#modifyMemoModal').data('modifyRouteId', modifyRouteId);
        $('#modifyMemoModal').data('dayNumber', dayNumber);
        $('#modifyMemoModal').data('detailPlanId', detailPlanId);
   });//

 // 수정 버튼 클릭 이벤트 처리
        $('#saveChanges').on('click', function() {
            // 모달에서 수정된 메모 내용 가져오기
            let updatedMemoContent = $('#memoContent').val();

            // 저장된 현재 메모 엘리먼트 가져오기
             let currentMemo = $('#modifyMemoModal').data('currentMemo');
             let modifyRouteId = $('#modifyMemoModal').data('modifyRouteId');
             let dayNumber = $('#modifyMemoModal').data('dayNumber');
             let detailPlanId = $('#modifyMemoModal').data('detailPlanId');
            // 현재 메모 내용 업데이트 //-> ajax 성공하면 실행되도록 수정
            currentMemo.text(updatedMemoContent);

            // 모달 닫기
            $('#modifyMemoModal').modal('hide');

            // db에 저장하는 update 메서드 호출
            modifyMemo(modifyRouteId,updatedMemoContent, dayNumber, detailPlanId);
        });




});//$(document).ready(function() {


//--------------------------[Google Maps API]-----------------------
function initMap(latlng) {
    console.log('initMap()의 latlng ', latlng);

    const defaultLocation = { lat: 37.0, lng: 127.5 }; // 기본값: 한국 좌표
    const center = latlng && latlng.length === 2 ? { lat: latlng[0], lng: latlng[1] } : defaultLocation;

    const mapOptions = {
        zoom: 8,
        center: center,
        fullscreenControl: false, // 전체화면 x
        streetViewControl: false, // 거리뷰 x
        mapTypeControl: false,
    };

    map = new google.maps.Map(document.getElementById("map"), mapOptions);

    // countryCode를 사용하여 장소 검색 초기화
    const countryCode = $('#countryHeading').data('countrycode');
    initAutocomplete(countryCode);

    // 초기 마커 추가
    addMarkersFromHTML();
}

//--------------------------[REST Countries API]-----------------------
//----국가 관련 정보(위경도)
function getCountryInfo(countryCode) {
    $.ajax({
        url: apiUrl + encodeURIComponent(countryCode),
        dataType: 'json',
        success: function(data) {
            if (data && data.length > 0) {
                const latlng = data[0].latlng;
                console.log(latlng);
                initMap(latlng); // 위도, 경도 전달
            } else {
                console.error('국가 정보를 찾을 수 없습니다.');
            }
        },
        error: function(xhr, status, error) {
            console.log("REST Countries API 호출 중 오류 발생", error);
        }
    });
}


//-----------------------자동검색어 완성
function initAutocomplete(countryCode) {
    const autocompleteInput = $('#autocomplete');
    const searchButton = $('#search-btn');

    bindPlaceAddButtons();

    // Google Places Autocomplete 초기화
    const autocomplete = new google.maps.places.Autocomplete(autocompleteInput[0], {
        types: ['establishment'],
        componentRestrictions: { country: countryCode || 'KR'}
    });

    // 장소가 변경되었을 때 처리
    autocomplete.addListener('place_changed', function() {
        const place = autocomplete.getPlace();
        if (!place.geometry) {
            console.error("입력된 장소에 대한 세부 정보가 없습니다: '" + place.name + "'");
            return;
        }

        // 자동완성 검색어(장소 이름)를 입력 필드에 설정
        autocompleteInput.val(place.name);
    });

    // 검색 버튼 클릭 이벤트 처리
    function searchPlace(dayNumber, detailPlanId) {
        const place = autocomplete.getPlace();
        if (place && place.geometry) {
            console.log('검색된 장소 세부 정보:', place);
            console.log('searchPlace()의 daynum', dayNumber);
            console.log('searchPlace()의 detailPlanId', detailPlanId);
            showPlaceOnMap(place, dayNumber, detailPlanId);
        } else {
            console.error("선택된 장소가 없거나 세부 정보가 없습니다.");
        }
    }

    // 검색 버튼 클릭 이벤트 처리
    searchButton.on('click', function() {
        console.log("searchButton.on.click의 daynum: ", dayNumber);
        $('#placeInfo').css('display', 'none').html('');
        searchPlace(dayNumber, detailPlanId);
    });
}



//----------------------지도에 검색된 장소 마킹 및 세부 정보 나타냄
function showPlaceOnMap(place, dayNumber, detailPlanId){
    //이름 ,장소 사진, 리뷰, 위경도
    //장소 검색 결과에 대한 마커 추가
    const marker = new google.maps.Marker({
        position: place.geometry.location,
        map: map
    });

    const lat =  place.geometry.location.lat();
    const lng =  place.geometry.location.lng();

    //장소의 사진
    let photoUrl = '';
    if(place.photos && place.photos.length > 0){
        photoUrl = place.photos[0].getUrl({maxWidth: 200, maxHeight: 200});
    }

    //장소 설명(리뷰)
    let description = '';
    if (place.reviews && place.reviews.length > 0) {
            description = place.reviews[0].text;
    }


    //정보창 내용 구성
    const placeInfoHtml = `
            <div class="card">
                <img src="${photoUrl}" class="card-img" alt="${place.name}" style="max-height: 250px;">
                <div class="card-body">
                   <h5 class="card-title">${place.name}</h5>
                   <p class="card-text"><small class="text-muted">${place.formatted_address}</small></p>
                </div>
                <div class="card-body" id="placeReview">
                   <p class="card-text">${description}</p>
                </div>
                <div class = "card-body">
                    <a herf="#" class="btn btn-primary add-route"
                            data-day=${dayNumber} data-detailplanid=${detailPlanId} data-lat=${lat} data-lng=${lng}>추가</a>
                </div>
            </div>
           `;

   // placeInfo 요소에 내용 추가
   $('#placeInfo').css('display', 'block');
   $('#placeInfo').html(placeInfoHtml);

   map.setCenter(place.geometry.location);
   map.setZoom(15);
}

//------------ROUTE, addRoute
function addRoute(day, placeName, category, detailPlanId, lat, lng){

    $.ajax({
        url:'/addRoute',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
                day:day,
                placeName: placeName,
                category: category,
                detailPlanId: detailPlanId,
                lat: lat,
                lng: lng
        }),
        beforeSend : function(xhr)
        { //데이터를 전송하기 전에 헤더에 csrf값을 설정
            xhr.setRequestHeader(header, token);
        },
        success: function(response){
            console.log('경로가 성공적으로 추가되었습니다.:', response);
            //입력 필드 초기화 및 비활성화
            const autocompleteInput = $('#autocomplete');
            autocompleteInput.prop('disabled', true);
            autocompleteInput.val('');
            $('#placeInfo').css('display', 'none').html('');

            refreshDetailPlan(detailPlanId, day, category);

        },
        error: function(xhr, status, error){
            console.log('addRoute-경로 추가 중 오류 발생: ', error);
        }
    })

}

//------------addRoute 이후 ajax로 데이터 불러옴
//-> 해당 날짜에 대한 route를 새로 불러옴
function refreshDetailPlan(detailPlanId, dayToUpdate, category) {
    console.log('refreshDetailPlan-dayToUpdate: ', dayToUpdate);
    console.log('refreshDetailPlan-category: ', category);

    $.ajax({
        url: '/api/viewRoute',
        method: 'GET',
        data: { detailPlanId: detailPlanId },
        success: function(routes) {
            console.log('Route data:', routes);
            updateRoute(dayToUpdate, routes, category);

        },
        error: function(xhr, status, error) {
            console.log('Detail plan data fetching error:', error);
        }
    });//$.ajax({
}//function refreshDetailPlan(planId, dayToUpdate) {

function updateRoute(dayToUpdate, routes, category) {
    console.log('------------------updateRoute----------');
    console.log(`Updating routes for day: ${dayToUpdate}`);
    console.log('updateRoute-category: ', category);
    if (routes.length > 0) {
        const firstDetailPlanId = routes[0].detailPlanId;
    } else {
        console.log('Routes array is empty.');
    }

    // 해당 일차에 해당하는 컨테이너 선택
    const detailPlanElement = $(`#date-block-${dayToUpdate} .container`).first();

    if (detailPlanElement.length === 0) {
        console.error(`day ${dayToUpdate}의 일정 컨테이너를 찾을 수 없습니다.`);
        return;
    }

    // 현재 컨테이너 비우기
    detailPlanElement.empty();

    // Route 리스트 추가
    if (routes.length === 0) {
        const emptyContainer = $('<div></div>', {
            class: 'container place-block'
        });

        detailPlanElement.append(emptyContainer);
    } else {
        routes.forEach((route, index) => {
            const routeElement = $('<div></div>', {
                class: 'container place-block',
                id: `sortable-${index + 1}`
            });

            if (route.category === 'PLACE') {
                routeElement.html(
                    `<div class="d-flex align-items-center mb-2 place-block">
                        <span class="badge badge-primary mr-2"   data-daynum="${route.day}">
                            <span>${route.markSeq}</span>
                        </span>
                        <h6 data-lat="${route.lat}" data-lng="${route.lng}" data-sortkey="${route.sortKey}" data-category="${route.category}" data-detailplanid="${route.detailPlanId}">${route.placeName}</h6>
                    </div>`
                );
            } else if (route.category === 'MEMO') {
                routeElement.html(
                    `<div class="memo-block flex-container">
                        <h6 data-lat="${route.lat}" data-lng="${route.lng}" data-sortkey="${route.sortKey}" data-category="${route.category}">${route.placeName}</h6>
                        <img class="modify-icon" src="/images/plan/edit.png" data-routeid="${route.id}" data-detailplanid="${route.detailPlanId}" data-daynum="${route.day}">
                    </div>`
                );
            }

            // Delete icon
            routeElement.append(
                `<div class="delete-icon position-absolute" data-routeid="${route.id}" data-detailplanid="${route.detailPlanId}" data-daynum="${route.day}" data-category="${route.category}" style="right: -15px; top: -3px; display: none;">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#d3d3d3" class="bi bi-x-circle" viewBox="0 0 16 16">
                        <path d="M8 0a8 8 0 1 0 0 16A8 8 0 0 0 8 0zM4.646 4.646a.5.5 0 1 1 .708-.708L8 7.293l2.646-2.647a.5.5 0 1 1 .708.708L8.707 8l2.647 2.646a.5.5 0 1 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 1 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                    </svg>
                </div>`
            );

            detailPlanElement.append(routeElement);
            setBadgeColor();
        });
    }

    // 버튼 섹션 추가
    const btnSection = $('<div class="d-flex flex-row btn-section"></div>').html(
        `<div class="btn"><button type="button" class="btn btn-outline-secondary place-add" data-day="${dayToUpdate}" data-detailplanid="${routes.length > 0 ? routes[0].detailPlanId : dayToUpdate}">장소추가</button></div>
        <div class="btn"><button type="button" class="btn btn-outline-secondary memo-add" data-day="${dayToUpdate}" data-detailplanid="${routes.length > 0 ? routes[0].detailPlanId : dayToUpdate}">메모추가</button></div>
        <div class="btn"><button type="button" class="btn btn-outline-secondary modify" data-day="${dayToUpdate}" data-detailplanid="${routes.length > 0 ? routes[0].detailPlanId : dayToUpdate}">수정</button></div>`
    );
    detailPlanElement.append(btnSection);

    bindPlaceAddButtons(routes); // 버튼 이벤트 제어

    console.log('updateRoute-category: ', category);
    if (category === 'PLACE') {
        addMarkersFromRoutes(routes);
    }
}


/*-------------------------------------장소 및 메모 추가 버튼 제어
   -'장소추가'(place-add)버튼 클릭 시
     1) 해당 버튼만 활성화('active')
     2) 'day1'(data-day)의 1 값 전역변수 dayNumber에 할당
            -> searchPlace-> showPlaceOnMap를 거쳐 "placeInfo"의 data-day=${dayNumber}에 할당
            -> 최종적으로 addRoute에서 1일차에 장소나 메모 등 route를 저장할 때 쓰임(Route의 day_num)
     3) 검색창(autocomplete)활성화 (disabled를 false로)
     => 동적으로 추가된 버튼들에도 동일한 이벤트 핸들러 바인딩을 위함.
*/
function bindPlaceAddButtons(routes) {

    const autocompleteInput = $('#autocomplete');

    // 장소추가, 메모추가, 수정 버튼
    const placeAddBtn = $('.place-add');
    const memoAddBtn = $('.memo-add');
    const modifyBtn = $('.modify');

    //'장소추가'버튼
    placeAddBtn.off('click').on('click', function() {
       $('.delete-icon').hide();
       $('.modify-icon').hide();
       $('.memoInput-section').remove();

        placeAddBtn.removeClass('active');
        $(this).addClass('active');
        memoAddBtn.removeClass('active');
        modifyBtn.removeClass('active');
        modifyBtn.text('수정');

        dayNumber = $(this).attr('data-day');
        detailPlanId = $(this).attr('data-detailplanid');
        category = 'PLACE';

        autocompleteInput.prop('disabled', false);
        autocompleteInput.val(''); // 입력 필드 초기화
        autocompleteInput.focus();

        $('#placeInfo').css('display', 'none').html(''); // placeInfo 초기화
    });

    //'메모추가'버튼
    memoAddBtn.off('click').on('click', function(){
        $('.delete-icon').hide();
        $('.modify-icon').hide();

        placeAddBtn.removeClass('active');
        modifyBtn.removeClass('active');
        modifyBtn.text('수정');
        autocompleteInput.prop('disabled', true);
        autocompleteInput.val('');

        const memoInputSectionId = '#memoInputSection-' + dayNumber;
        memoAddBtn.removeClass('active');
        $(this).addClass('active');

        dayNumber = $(this).attr('data-day');

        detailPlanId = $(this).attr('data-detailplanid');

        category = 'MEMO';
        console.log("메모추가 btn 클릭 이벤트 : ", dayNumber);

        //메모 입력 섹션 생성
        if($(memoInputSectionId).length == 0 ){
            let memoInputSection =
                `<div id="memoInputSection-${dayNumber}" class="input-group mb-3 memoInput-section">
                    <input type="text" class="form-control" placeholder="메모를 입력하세요" aria-label="Memo" aria-describedby="button-addon2">
                    <button class="btn btn-outline-secondary" type="button" id="addMemo" data-detailplanid=${detailPlanId}>추가</button>
                </div>`;
            $(this).closest('.btn-section').before(memoInputSection);
        } else {
            $(memoInputSectionId).remove();
            $(this).removeClass('active');
        }
                $('#placeInfo').css('display', 'none').html(''); // placeInfo 초기화

    });//

    //수정 버튼
    modifyBtn.off('click').on('click', function(){
        console.log('------------modifyBtn 수정 버튼 클릭 -----------');
        const container = $(this).closest('.container');
        const deleteIcons = container.find('.delete-icon');
        const modifyIcons = container.find('.modify-icon');
        $('.memoInput-section').remove();

        let sortable = container.data('sortable-instance');

        if($(this).hasClass('active')){//수정 끝
            $(this).removeClass('active');
            $(this).text('수정');
            deleteIcons.hide();
            modifyIcons.hide();

            // drag and drop 비활성화
            if (sortable) {
                disableDragAndDrop(sortable);
                container.removeData('sortable-instance');
            }
            console.log('bindAddPlace~~~ updateRouteSequence 호출, container = ', container);
            updateRouteSequence(container);

        } else {//수정 시작
            $('.delete-icon').hide();
            $('.modify-icon').hide();

            placeAddBtn.removeClass('active');
            memoAddBtn.removeClass('active');

            autocompleteInput.val('');
            autocompleteInput.prop('disabled', true);
            $('#placeInfo').css('display', 'none').html('');

            modifyBtn.removeClass('active');
            modifyBtn.text('수정');
            $(this).addClass('active');
            $(this).text('완료');

            deleteIcons.show();
            modifyIcons.show();

            // drag and drop 활성화
            sortable = enableDragAndDrop(container.get(0));
            container.data('sortable-instance', sortable);

        }//else
    });//modifyBtn

}//function bindPlaceAddButtons() {


//------------------------------------------정적 페이지 마커 추가 함수
function addMarkersFromHTML() {
    console.log('-----------------------------addMarkersFromHTML 실행');

    // 각 날짜별로 마커와 폴리라인을 저장할 배열 초기화
    const markersByDay = {};
    const polylinesByDay = {};

    // 각 h6 태그에서 data-lat 및 data-lng 속성을 가진 요소를 반복 처리
    $('h6[data-lat][data-lng]').each(function() {
        const lat = $(this).attr('data-lat');
        const lng = $(this).attr('data-lng');
        const placeName = $(this).text();
        const category = $(this).attr('data-category');
        const sortKey = $(this).attr('data-sortkey');
        const day = $(this).attr('data-daynum'); // 날짜 정보 가져오기

        // lat, lng 정보가 없거나 category가 'MEMO'인 경우 건너뛰기
        if (!lat || !lng || category === 'MEMO') {
            return;
        }

        const position = { lat: parseFloat(lat), lng: parseFloat(lng) };

        // 해당 날짜의 마커 배열에 추가
        if (!markersByDay[day]) {
            markersByDay[day] = [];
            markersByDay[day].markerIndex = 1; // 날짜별 마커 인덱스 초기화
        }

        // 마커 생성 및 라벨 설정
        const marker = new google.maps.Marker({
            position: position,
            map: map,
            title: placeName,
            sortKey: sortKey,
            label: {
                text: String(markersByDay[day].markerIndex),
                color: 'white',
                fontSize: '12px',
                fontWeight: 'bold'
            },
            // 날짜별로 색상 지정
            icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 8,
                fillColor: getColorForDay(day),
                fillOpacity: 1,
                strokeColor: 'white',
                strokeWeight: 1,
                strokeOpacity: 0.8
            }
        });
        // 각 마커에 마우스 이벤트 추가
//        google.maps.event.addListener(marker, 'mouseover', function() {
//            highlightMarker(marker);
//        });
//
//        google.maps.event.addListener(marker, 'mouseout', function() {
//            resetMarker(marker);
//        });

        markersByDay[day].push(marker);
        markersByDay[day].markerIndex++; // 날짜별 마커 인덱스 증가
    });//$('h6[data-lat][data-lng]').each(function() {

    // 각 날짜별로 마커 배열을 순회하여 폴리라인 생성 및 설정
    Object.keys(markersByDay).forEach(day => {
        const dayMarkers = markersByDay[day];

        // sortKey 기준으로 마커 정렬
        dayMarkers.sort((a, b) => a.sortKey.localeCompare(b.sortKey));

        // 해당 날짜의 폴리라인 생성
        const pathCoordinates = dayMarkers.map(marker => marker.getPosition());
        const polyline = new google.maps.Polyline({
            path: pathCoordinates,
            geodesic: true,
            strokeColor: getColorForDay(day), // 날짜별로 폴리라인 색상 지정
            strokeOpacity: 1.0,
            strokeWeight: 2
        });

        // 해당 날짜의 폴리라인을 지도에 추가
        polylinesByDay[day] = polyline;
        polyline.setMap(map);

        // 해당 날짜의 마커들에 맞춰 지도 화면 조정
        if (dayMarkers.length > 0) {
            const bounds = new google.maps.LatLngBounds();
            dayMarkers.forEach(marker => bounds.extend(marker.getPosition()));
            map.fitBounds(bounds);
        }
    });//Object.keys(markersByDay).forEach(day => {



}//function addMarkersFromHTML() {

//-------------------------------------------동적으로 구성된 마커 추가
function addMarkersFromRoutes(routes) {
    console.log('-------------------------addMarkersFromRoutes 실행');

    // 기존 마커 제거
    markers.forEach(marker => marker.setMap(null));
    markers = [];
    let markerIndex = 1; // 마커 인덱스 초기화

    // 모든 routes에서 위치 정보를 가져와서 마커 추가
    routes.forEach(route => {
        const lat = route.lat; // route 객체에서 위도 정보 가져오기
        const lng = route.lng; // route 객체에서 경도 정보 가져오기
        const placeName = route.placeName; // route 객체에서 장소 이름 가져오기
        const sortKey = route.sortKey; // route 객체에서 정렬 키 가져오기
        const day = route.day;
        console.log('addMarkersFromRoute - day : ', day);

        // category가 'MEMO'인 경우 건너뛰기
        if (!lat || !lng || route.category === 'MEMO') {
            return;
        }

        const position = { lat: parseFloat(lat), lng: parseFloat(lng) };

        // 마커 생성 및 라벨 설정
        const marker = new google.maps.Marker({
            position: position,
            map: map,
            title: placeName,
            sortKey: sortKey,
            label: {
                text: String(markerIndex),
                color: 'white',
                fontSize: '12px',
                fontWeight: 'bold'
            },
            // 날짜별로 색상 지정 (getColorForDay 함수 활용)
            icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 8,
                fillColor: getColorForDay(day),
                fillOpacity: 1,
                strokeColor: 'white',
                strokeWeight: 1,
                strokeOpacity: 0.8
            }
        });

        markers.push(marker);
        markerIndex++;
    });

    // 마커 sortKey 기준으로 정렬
    markers.sort((a, b) => a.sortKey.localeCompare(b.sortKey));

    // 기존 폴리라인 제거
    if (polyline) {
        polyline.setMap(null);
    }

    // 마커 연결 폴리라인 생성
    const pathCoordinates = markers.map(marker => marker.getPosition());
    polyline = new google.maps.Polyline({
        path: pathCoordinates,
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2
    });

    polyline.setMap(map);

    // 지도 중심을 마커들의 중앙으로 이동
    if (markers.length > 0) {
        const bounds = new google.maps.LatLngBounds();
        markers.forEach(marker => bounds.extend(marker.getPosition()));
        map.fitBounds(bounds);
    }
}

//--------------------------------deleteRoute 루트 삭제
function deleteRoute(routeIdToDelete, dayNumber, detailPlanId, category){
    console.log('deleteRoute-------------- category : ', category)
    $.ajax({
            url: '/deleteRoute',
            method: 'POST',
            data: { routeId: routeIdToDelete },
            success: function(response) {
                console.log('db route 데이터 삭제 성공')
                refreshDetailPlan(detailPlanId, dayNumber, category);

            },
            beforeSend : function(xhr)
                    { //데이터를 전송하기 전에 헤더에 csrf값을 설정
                        xhr.setRequestHeader(header, token);
            },
            error: function(xhr, status, error) {
                console.log('delete Route Error:', error);
            }
    });//$.ajax({

}//detleteRoute(routeIdToDelete)

//---------------------------modifyMemo 메모 수정
function modifyMemo(modifyRouteId, updatedMemoContent, detailPlanId, dayNumber){
    $.ajax({
           url: '/modifyMemo',
           method: 'POST',
           data: { modifyRouteId: modifyRouteId, updatedMemoContent: updatedMemoContent},
           success: function(response) {
               if(response.status == 'success') {
                    console.log('메모가 성공적으로 업데이트되었습니다.');
                    refreshDetailPlan(detailPlanId, dayNumber);

               }else{
                    console.log('메모 업데이트에 실패했습니다.');
               }
           },
           beforeSend : function(xhr)
                { //데이터를 전송하기 전에 헤더에 csrf값을 설정
                   xhr.setRequestHeader(header, token);
           },
           error: function(xhr, status, error) {
                console.log('메모 업데이트 중 오류 발생:', error);
           }
    });//$.ajax({

};//function modifyMemo(modifyRouteId, updatedMemoContent){


//------------------------------------- Drag and Drop으로 일정 순서 바꾸기
function enableDragAndDrop(container) {
    let sortable = new Sortable(container, {
        animation: 150,
        ghostClass: 'sortable-ghost',
        draggable: '.place-block',
        onEnd: function(evt) {
            const itemEl = evt.item;  // dragged HTMLElement
            console.log(`Item ${itemEl.textContent.trim()} moved from ${evt.oldIndex} to ${evt.newIndex}`);

            // log the move event
        }
    });
    return sortable;
}//function enableDragAndDrop() {

function disableDragAndDrop(sortable) {
    sortable.destroy();
}//function disableDragAndDrop(sortables) {

//-------------routeSequence update 로직(순서 변경 db 반영)
function updateRouteSequence(container){
    let routeDataList = [];
    console.log('----------updateRouteSequence 시작-------------')
    container.children('.place-block, .memo-block').each(function(index){
        const routeSequence = index + 1; //1부터 시작


         routeId = $(this).find('.delete-icon').data('routeid');
         category = $(this).find('.delete-icon').data('category');
         dayNumber = $(this).find('.delete-icon').data('daynum');
         detailPlanId = $(this).find('.delete-icon').data('detailplanid');


        console.log('routeId : ', routeId);
        console.log('category : ', category);
        console.log('dayNumber : ', dayNumber);
        console.log('detailPlanId : ', detailPlanId);

        routeDataList.push({
            id: routeId,
            routeSequence: routeSequence,
            category: category,
            day_number: dayNumber
        });

    });//container.children

    $.ajax({
        url: '/updateRouteSequence',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(routeDataList),
        success: function(response) {
            console.log('routeSequence update 성공');

            refreshDetailPlan(detailPlanId, dayNumber, category);

        },
        beforeSend: function(xhr) {
            // 데이터 전송 전에 헤더에 csrf 값 설정
            xhr.setRequestHeader(header, token);

        },
        error: function(xhr, status, error) {
            console.log('routeSequence update 실패, ', error);
        }
    });

};//function updateRouteSequence(container){


// ----------------------페이지 로딩 시 초기 베지 배경색 설정
function setBadgeColor() {
    const badgeElements = $('.badge');

    badgeElements.each(function() {
        const dayNumber = $(this).attr('data-daynum');
        console.log('-------------setBadgeColor dayNumber :', dayNumber);
        const color = getColorForDay(dayNumber); // JavaScript 함수 호출
        $(this).css({
            'background-color': color,
            'color': '#fff',
            'border-radius': '50%',
            'padding': '6px 10px'
        });
    });
}

//---------------날짜에 따라 색상 결정 함수
function getColorForDay(day) {
    console.log('----getColorForDay - day : ', day);
    switch (day) {
        case '1':
            return 'red';
        case '2':
            return 'blue';
        default:
            return 'green'; // 기본값으로 설정할 색상
    }
}
// 예시로 각 케이스의 반환 값을 확인
console.log(getColorForDay('1')); // 콘솔에 'red' 출력
console.log(getColorForDay('2')); // 콘솔에 'blue' 출력
console.log(getColorForDay('3')); // 콘솔에 'green' 출력
console.log(getColorForDay(''));  // 콘솔에 'green' 출력 (기본값)
//--------------마커 강조 함수
    // 마커 강조 함수
//    function highlightMarker(marker) {
//        if (originalIcon) {
//            resetMarker(originalIcon); // 이전 강조된 마커를 원래 아이콘으로 복원
//        }
//
//        originalIcon = marker;
//        marker.originalIcon = marker.getIcon();
//
//        marker.setIcon({
//            path: google.maps.SymbolPath.CIRCLE,
//            scale: 10, // 강조된 크기
//            fillColor: 'yellow', // 강조된 색상
//            fillOpacity: 1,
//            strokeColor: 'orange',
//            strokeWeight: 2,
//            strokeOpacity: 0.9
//        });
//
//        // 마커가 중심에 오도록 지도의 포커스 이동
//        map.panTo(marker.getPosition());
//    }
//
//    // 마커 리셋 함수
//    function resetMarker(marker) {
//        marker.setIcon(marker.originalIcon);
//    }