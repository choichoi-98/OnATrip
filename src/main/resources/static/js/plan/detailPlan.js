let map;
let countryName;
  //REST Countries API 엔드포인트 URL
const apiUrl = "https://restcountries.com/v3/alpha/";
// 공공데이터포털 API 키
const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';
let globalCountryCode;

$(document).ready(function() {
    const countryName = $('#countryHeading').text().trim();
    getCountryCode(countryName);

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

 //------------------------마커------------------------
//  const marker = new google.maps.Marker({
//    position: { lat: -34.397, lng: 150.644 },//마킹 좌표
//    map: map,
//  });
//
//  const infowindow = new google.maps.InfoWindow({
//    content: "<p>Marker Location:" + marker.getPosition() + "</p>",
//  });
//
//  google.maps.event.addListener(marker, "click", () => {
//    infowindow.open(map, marker);
//  });
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

                globalCountryCode = countryCode;

                getCountryInfo(countryCode);//위도, 경도
                initAutocomplete(countryCode);
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

       // Google Places Autocomplete 초기화
       const autocomplete = new google.maps.places.Autocomplete(autocompleteInput[0], {
            types: ['establishment'],
            componentRestrictions: { country: globalCountryCode }
       });

       // 장소가 변경되었을 때 처리
           autocomplete.addListener('place_changed', function() {
               const place = autocomplete.getPlace();
               if (!place.geometry) {
                   console.error("입력된 장소에 대한 세부 정보가 없습니다: '" + place.name + "'");
                   return;
               }
               // 장소 세부 정보가 place 객체에 있습니다.
               console.log('장소 세부 정보:', place);
           });

                // 검색 버튼 클릭 이벤트 처리
                function searchPlace() {
                    const place = autocomplete.getPlace();
                    if (place && place.geometry) {
                        console.log('검색된 장소 세부 정보:', place);
                        showPlaceOnMap(place);
                    } else {
                        console.error("선택된 장소가 없거나 세부 정보가 없습니다.");
                    }
                }

                searchButton.on('click', function() {
                    searchPlace();
                });

           // 검색 버튼 클릭 이벤트 처리
           searchButton.on('click', function() {
                   searchPlace();
               });

               // 엔터 키 눌렀을 때 검색 처리
                   autocompleteInput.on('keypress', function(e) {
                       if (e.which == 13) { // 엔터 키 코드
                           e.preventDefault();
                           searchPlace();
                       }
                   });
}

function showPlaceOnMap(place){

    //마커 추가
    const marker = new google.maps.Marker({
        position: place.geometry.location,
        map: map
    });

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
                <img src="${photoUrl}" class="card-img" alt="${place.name}">
                <div class="card-body">
                   <h5 class="card-title">${place.name}</h5>
                   <p class="card-text"><small class="text-muted">${place.formatted_address}</small></p>
                <div class="card-body">
                   <p class="card-text">${description}</p>
                </div>
            </div>
           `;

    //정보창 추가
//    const infowindow = new google.maps.InfoWindow({
//        content: contentString
//    });

   // placeInfo 요소에 내용 추가
   $('#placeInfo').html(placeInfoHtml);

   map.setCenter(place.geometry.location);
   map.setZoom(15);
}