let map;
let countryName;
  //REST Countries API 엔드포인트 URL
const apiUrl = "https://restcountries.com/v3/alpha/";
// 공공데이터포털 API 키
const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';

$(document).ready(function() {
  const countryName = $('#countryHeading').text().trim();
  getCountryCode(countryName);
   initMap();
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
                initCountryCode(latlng);
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
//한글국가명 -> iso3자리 변환
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
                const countryCode = data.data[0].iso_alp3;
                console.log('Country Code:', countryCode);
                getCountryInfo(countryCode);
             } else {
                 console.error('국가 정보를 찾을 수 없습니다. 정확한 국가명인지 확인하세요.');
             }
        },
        error: function(xhr, status, error){
                    console.log("공공데이터포털 API 호출 중 오류 발생", error)
        }
    });//$.ajax({
}//function getCountryCode(countryName){
