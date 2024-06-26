// 공공데이터포털 API 키
const apiKey = 'GqsVgyYpktxrLRv1S%2FvX5a%2BU0How0aXThcAAfkLelSha90ZyXvuhuZYsF5T1A5btMEBpUyTq6Dj8UWEKKQ%2BVOQ%3D%3D';

$(document).ready(function() {
       $('#country_input').keypress(function(event) {
                       if (event.which === 13) { // 13 is the Enter key
                           event.preventDefault(); // Prevent default form submission (if any)

                           var countryName = $(this).val();
                           console.log(countryName);

                           getCountryCode(countryName);

                   });


});//$(document).ready(function()
//--------------------------[공공 데이터 API]-----------------------
//한글국가명 -> iso2자리 변환
function getCountryCode(countryName){
                           const url = 'http://apis.data.go.kr/1262000/CountryCodeService2/getCountryCodeList2?'
                                       +'serviceKey=' + apiKey
                                       + '&returnType=JSON&cond[country_nm::EQ]='+countryName ;

                           // Make the API call using jQuery's ajax method
                            $.ajax({
                                   url: url,
                                   method: 'GET',
                                   dataType: 'json',
                                   success: function(data){
                                        if (data && data.data && data.data.length > 0) {
                                           const countryCode = data.data[0].country_iso_alp2;
                                           console.log('Country Code:', countryCode);


                                        } else {
                                            console.error('국가 정보를 찾을 수 없습니다. 정확한 국가명인지 확인하세요.');
                                        }
                                   },
                                   error: function(xhr, status, error){
                                               console.log("공공데이터포털 API 호출 중 오류 발생", error)
                                   }
                               });//$.ajax({
                          }