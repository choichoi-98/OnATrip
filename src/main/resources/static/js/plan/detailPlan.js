let map;
let countryName;
  //REST Countries API 엔드포인트 URL
const apiUrl = "https://restcountries.com/v3/alpha/";
// 공공데이터포털 API 키
const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';
let dayNumber;
let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function() {

    const countryName = $('#countryHeading').text().trim();
    getCountryCode(countryName);


    //addRoute
    $(document).on('click', '.add-route', function(e) {
            e.preventDefault();
            const day = $(this).data('day');
            const placeName = $(this).closest('.card').find('.card-title').text();
            addRoute(day, placeName);
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

//초기화 함수
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

//자동검색어 완성
function initAutocomplete(countryCode){
    const autocompleteInput = $('#autocomplete');
    const searchButton = $('#search-btn');

    //장소추가 btn 클릭시 검색창 focus
       const placeAddBtn = $('.place-add');
       placeAddBtn.on('click', function() {
           const placeAddBtn = $('.place-add');

           placeAddBtn.removeClass('active');

           $(this).addClass('active');
            dayNumber = $(this).attr('data-day');
            console.log("장소추가 btn 클릭 이벤트 : ",dayNumber);

            autocompleteInput.prop('disabled',false);
            autocompleteInput.val('');//입력 필드 초기화
            autocompleteInput.focus();

            $('#placeInfo').css('display', 'none').html('');//placeInfo 초기화

       });

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
               console.log('장소 세부 정보:', place);
           });

       // 검색 버튼 클릭 이벤트 처리
       function searchPlace(dayNumber) {
            const place = autocomplete.getPlace();
                if (place && place.geometry) {
                    console.log('검색된 장소 세부 정보:', place);
                    console.log('searchPlace()의 daynum', dayNumber);
                    showPlaceOnMap(place,dayNumber);

                } else {
                      console.error("선택된 장소가 없거나 세부 정보가 없습니다.");
                }
       }


       // 검색 버튼 클릭 이벤트 처리
       searchButton.on('click', function() {
             console.log("searchButton.on.click의 daynum: ", dayNumber);
             $('#placeInfo').css('display', 'none').html('');
             searchPlace(dayNumber);
       });

       // 엔터 키 눌렀을 때 검색 처리
       autocompleteInput.on('keypress', function(e) {
                  if (e.which == 13) { // 엔터 키 코드
                      e.preventDefault();
                      console.log("autocompleteInput.on.keypress의 daynum: ", dayNumber);
                      $('#placeInfo').css('display', 'none').html('');
                      searchPlace(dayNumber);
                  }
       });


}

//----------------지도에 검색된 장소 마킹 및 세부 정보 나타냄
function showPlaceOnMap(place, dayNumber){

    //마커 추가
    const marker = new google.maps.Marker({
        position: place.geometry.location,
        map: map
    });

        console.log("위도 경도 값?", place.geometry.location);
    //장소의 사진
    let photoUrl = '';
    if(place.photos && place.photos.length > 0){
        photoUrl = place.photos[0].getUrl({maxWidth: 200, maxHeight: 200});
    }

    //장소 설졍
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
                    <a herf="#" class="btn btn-primary add-route" data-day=${dayNumber}>추가</a>
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
function addRoute(day, placeName){
    $.ajax({
        url:'/addRoute',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({day:day, placeName: placeName}),
        beforeSend : function(xhr)
        { //데이터를 전송하기 전에 헤더에 csrf값을 설정
            xhr.setRequestHeader(header, token);
        },
        success: function(response){
            console.log('경로가 성공적으로 추가되었습니다.:', response);
        },
        error: function(xhr, status, error){
            console.log('addRoute-경로 추가 중 오류 발생: ', error);
        }
    })

}