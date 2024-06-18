function myMap() {
    var mapOptions = {
        center: new google.maps.LatLng(51.508742, -0.120850),
        zoom: 5
    };

    var map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
}

$(document).ready(function(){
    // 다른 초기화 코드가 여기로 올 수 있습니다.
});
