let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let map;
let countryName;
  //REST Countries API 엔드포인트 URL
const apiUrl = "https://restcountries.com/v3/alpha/";
// 공공데이터포털 API 키
const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';
let dayNumber;//일차
let detailPlanId;
let category;//memo, place
let markers = [];//마커들 저장
let markerIndex = 1;

$(document).ready(function() {

    const countryName = $('#countryHeading').text().trim();
    getCountryCode(countryName);


    //addRoute('장소추가' -> '추가'_db에 데이터 삽입)
    $(document).on('click', '.add-route', function(e) {
            category = 'PLACE';
            e.preventDefault();
            console.log("-------------addRoute.on.click-----------");
            const day = $(this).data('day');
            console.log("day: ", day);
            const detailPlanId = $(this).data('detailplanid');
            console.log("detailPlanId: ", detailPlanId);
            const lat = $(this).data('lat');
            console.log("lat: ", lat);
            const lng = $(this).data('lng');
            console.log("lng: ", lng);
            const placeName = $(this).closest('.card').find('.card-title').text();
            addRoute(day, placeName, category,detailPlanId, lat, lng);
    });

    //addMeMo
   $(document).on('click', '#addMemo', function(e){
        category = 'MEMO';
        e.preventDefault();
        const memoInput = $(this).siblings('input');
        const memoText = memoInput.val().trim();
        if(memoText){
            const day = $(this).closest('.input-group').attr('id').split('-')[1];
            const detailPlanId = $(this).data('detailplanid');
            console.log('#addMemo - day, detailPlanId', day, detailPlanId);
            addRoute(day,memoText, category , detailPlanId,null,null);
            memoInput.val('');
        }

   });


});//$(document).ready(function() {


//--------------------------[Google Maps API]-----------------------
function initMap(latlng) {

  console.log('initMap()의 latlng ', latlng);

  const defaultLocation = {lat: 37.0, lng: 127.5}; // 기본값: 한국 좌표
  const center = latlng && latlng.length === 2 ? { lat: latlng[0], lng: latlng[1] } : defaultLocation;

  const mapOptions = {
    zoom: 8,
    center: center,
    fullscreenControl: false, //전체화면 x
    streetViewControl: false, //거리뷰 x
    mapTypeControl: false,
  };

  map = new google.maps.Map(document.getElementById("map"), mapOptions);

  // 초기 마커 추가
  addMarkersFromHTML();

}//function initMap() {

//--------------------------[REST Countries API]-----------------------
//----국가 관련 정보(위경도)
function getCountryInfo(countryCode){
    $.ajax({
        url: apiUrl + encodeURIComponent(countryCode),
        dataType: 'json',
        success: function(data){
            if(data && data.length > 0){
                const latlng = data[0].latlng;
                console.log(latlng)
                initCountryCode(latlng);//위도, 경도
            }else{
                console.error('국가 정보를 찾을 수 없습니다.');
            }
        },
        error: function(xhr, status, error){
            console.log("REST Countries API 호출 중 오류 발생", error)
        }
    });//$.ajax
}//function getCountryCode(countryName){

//-------------------------국가에 맞춰(위도 경도) initMap
function initCountryCode(latlng){
    console.log('위도 및 경도: ', latlng);
    initMap(latlng);
}//function initCountryCode(countryCode){

//--------------------------[공공 데이터 API]-----------------------
//한글국가명 -> iso2자리 변환
function getCountryCode(countryName){
const url = 'http://apis.data.go.kr/1262000/CountryCodeService2/getCountryCodeList2?'
            +'serviceKey=' + apiKey
            + '&returnType=JSON&cond[country_nm::EQ]='+countryName ;

    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function(data){
             if (data && data.data && data.data.length > 0) {
                const countryCode = data.data[0].country_iso_alp2;
                console.log('Country Code:', countryCode);


                getCountryInfo(countryCode);//위도, 경도
                initAutocomplete(countryCode);//장소검색 호출
             } else {
                 console.error('국가 정보를 찾을 수 없습니다. 정확한 국가명인지 확인하세요.');
             }
        },
        error: function(xhr, status, error){
                    console.log("공공데이터포털 API 호출 중 오류 발생", error)
        }
    });//$.ajax({
}//function getCountryCode(countryName){

//-----------------------자동검색어 완성
function initAutocomplete(countryCode){
    const autocompleteInput = $('#autocomplete');
    const searchButton = $('#search-btn');

        bindPlaceAddButtons();

       // Google Places Autocomplete 초기화
       const autocomplete = new google.maps.places.Autocomplete(autocompleteInput[0], {
            types: ['establishment'],
            componentRestrictions: { country: countryCode }
       });

       // 장소가 변경되었을 때 처리
           autocomplete.addListener('place_changed', function() {
               const place = autocomplete.getPlace();
               if (!place.geometry) {
                   console.error("입력된 장소에 대한 세부 정보가 없습니다: '" + place.name + "'");
                   return;
               }

           });

       // 검색 버튼 클릭 이벤트 처리
       function searchPlace(dayNumber, detailPlanId) {
            const place = autocomplete.getPlace();
                if (place && place.geometry) {
                    console.log('검색된 장소 세부 정보:', place);
                    console.log('searchPlace()의 daynum', dayNumber);
                    console.log('searchPlace()의 detailPlanId', detailPlanId);
                    showPlaceOnMap(place,dayNumber, detailPlanId);

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

    //장소 검색 결과에 대한 마커 추가
    const marker = new google.maps.Marker({
        position: place.geometry.location,
        map: map
    });

    console.log("showPlaceOnMap-detailPlanId: " , detailPlanId);
    const lat =  place.geometry.location.lat();
    console.log("위도 :", lat);
    const lng =  place.geometry.location.lng();
    console.log("경도 :", place.geometry.location.lng());

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

    console.log('addRoute, ajax요청-detailPlanId', detailPlanId);
    console.log('placeName: ', placeName);
    console.log('category: ', category);
    console.log('day: ', day);
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

            refreshDetailPlan(detailPlanId, day);

        },
        error: function(xhr, status, error){
            console.log('addRoute-경로 추가 중 오류 발생: ', error);
        }
    })

}

//------------addRoute 이후 ajax로 데이터 불러옴
//-> 해당 날짜에 대한 route를 새로 불러옴
function refreshDetailPlan(detailPlanId, dayToUpdate) {
    console.log('refreshDetailPlan-dayToUpdate: ', dayToUpdate);
    $.ajax({
        url: '/api/viewRoute',
        method: 'GET',
        data: { detailPlanId: detailPlanId },
        success: function(routes) {
            console.log('Route data:', routes);
            updateRoute(dayToUpdate, routes);
        },
        error: function(xhr, status, error) {
            console.log('Detail plan data fetching error:', error);
        }
    });//$.ajax({
}//function refreshDetailPlan(planId, dayToUpdate) {

function updateRoute(dayToUpdate, routes) {
    console.log(`Updating routes for day: ${dayToUpdate}`);

    // 해당 일차에 해당하는 컨테이너 선택
    const detailPlanElement = $(`#date-block-${dayToUpdate} .container`).first();

    if (detailPlanElement.length === 0) {
        console.error(`day ${dayToUpdate}의 일정 컨테이너를 찾을 수 없습니다.`);
        return;
    }

    // 현재 컨테이너 비우기
    detailPlanElement.empty();

    // Route 리스트 추가
    routes.forEach(route => {
        const routeElement = $('<div class="container place-block"></div>').html(
            `<h6 data-lat="${route.lat}" data-lng="${route.lng}" data-sortkey="${route.sortKey}">${route.placeName}</h6>`
        );
        detailPlanElement.append(routeElement);
    });

    // 버튼 추가
    const btnSection = $('<div class="d-flex flex-row btn-section"></div>').html(
        `<div class="btn"><button type="button" class="btn btn-outline-secondary place-add" data-day="${dayToUpdate}" data-detailplanid="${routes[0].detailPlanId}">장소추가</button></div>
        <div class="btn"><button type="button" class="btn btn-outline-secondary memo-add" data-day="${dayToUpdate}" data-detailplanid="${routes[0].detailPlanId}">메모추가</button></div>`
    );
    detailPlanElement.append(btnSection);

    bindPlaceAddButtons();//버튼 이벤트 제어

    //'장소추가'인 경우에만 마커 추가(지도)
    if(routes.some(route => {
        console.log('updateRoute-category: ', route.category);
                return route.category =='PLACE'
                })) {

        addMarkersFromRoutes(routes); //마커 동적 추가
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
function bindPlaceAddButtons() {
    const autocompleteInput = $('#autocomplete');

    // 장소, 메모 추가 버튼
    const placeAddBtn = $('.place-add');
    const memoAddBtn = $('.memo-add');

    //'장소추가'버튼
    placeAddBtn.off('click').on('click', function() {

        placeAddBtn.removeClass('active');
        $(this).addClass('active');
        memoAddBtn.removeClass('active');

        dayNumber = $(this).attr('data-day');
        console.log('placeAddBtn-dayNumber:', dayNumber);

        detailPlanId = $(this).attr('data-detailplanid');
        console.log('placeAddBtn-detailPlanId: ', detailPlanId);

        category = 'PLACE';
        console.log("장소추가 btn 클릭 이벤트 : ", dayNumber);

        autocompleteInput.prop('disabled', false);
        autocompleteInput.val(''); // 입력 필드 초기화
        autocompleteInput.focus();

        $('#placeInfo').css('display', 'none').html(''); // placeInfo 초기화
    });

    //'메모추가'버튼
    memoAddBtn.off('click').on('click', function(){
        placeAddBtn.removeClass('active');
        autocompleteInput.prop('disabled', true);
        autocompleteInput.val('');


        const memoInputSectionId = '#memoInputSection-' + dayNumber;
        memoAddBtn.removeClass('active');
        $(this).addClass('active');


        dayNumber = $(this).attr('data-day');
        console.log('memoAddBtn-dayNumber: ', dayNumber);

        detailPlanId = $(this).attr('data-detailplanid');
        console.log('memoAddBtn-detailPlanId: ',detailPlanId );

        category = 'MEMO';
        console.log("메모추가 btn 클릭 이벤트 : ", dayNumber);

        //메모 입력 섹션 생성
        if($(memoInputSectionId).length == 0 ){
            let memoInputSection =
                `<div id="memoInputSection-${dayNumber}" class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="메모를 입력하세요" aria-label="Memo" aria-describedby="button-addon2">
                    <button class="btn btn-outline-secondary" type="button" id="addMemo" data-detailplanid=${detailPlanId}>추가</button>
                </div>`;
            $(this).closest('.btn-section').before(memoInputSection);
        } else {
            $(memoInputSectionId).remove();
        }
    });//
}

//------------------------------------------초기 마커 추가 함수
function addMarkersFromHTML(){
    console.log('-----------------------------addMarkersFromHTML 실행')
    markerIndex=1;
    // 모든 h6 태그에서 lat과 lng 값을 가져와서 마커 추가
    $('h6[data-lat][data-lng]').each(function() {
        const lat = parseFloat($(this).attr('data-lat'));
        const lng = parseFloat($(this).attr('data-lng'));
        const placeName = $(this).text();
        const position = { lat: lat, lng: lng };
        const sortKey = $(this).attr('data-sortkey');

        //마커
        const marker = new google.maps.Marker({
            position: position,
            map: map,
            title: placeName,
            sortKey: sortKey,
            label:{
                text: String(markerIndex),
                color: 'white',
                fontSize: '12px',
                fontWeight: 'bold'
            }
        });

        markers.push(marker);
        markerIndex++;
    });

    // 마커 sortKey 기준으로 정렬
    markers.sort((a,b) => {
        return a.sortKey.localeCompare(b.sortKey);
    });

    //마커 연결 폴리라인
    const pathCoordinates = markers.map(marker => marker.getPosition());
    const polyline = new google.maps.Polyline({
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

//-------------------------------------------동적으로 구성된 마커 추가
function addMarkersFromRoutes(routes) {
    console.log('-------------------------addMarkersFromRoutes 실행');

    // 기존 마커 제거
    markers.forEach(marker => marker.setMap(null));
    markers = [];
    markerIndex=1;


    // 새로운 마커 추가
    routes.forEach(route => {
        const lat = parseFloat(route.lat);
        const lng = parseFloat(route.lng);
        const placeName = route.placeName;
        const position = { lat: lat, lng: lng };
        const sortKey = route.sortKey;

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
            }
        });

        markers.push(marker);
        markerIndex++;
    });

    // 마커 sortKey 기준으로 정렬
    markers.sort((a,b) => {
            return a.sortKey.localeCompare(b.sortKey);
    });

    //마커 연결 폴리라인
    const pathCoordinates = markers.map(marker => marker.getPosition());
        const polyline = new google.maps.Polyline({
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