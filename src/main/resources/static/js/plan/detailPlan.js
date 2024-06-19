function myMap() {
    var mapOptions = {
        center: new google.maps.LatLng(51.508742, -0.120850),
        zoom: 5
    };

    var map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
}

$(document).ready(function() {
    const $toggleSearch = $('#toggleSearch');
    const $searchSection = $('#search-section');
    const $mapSection = $('#map-section');
    const $toggleIcon = $('#toggleIcon');

    $toggleSearch.on('click', function() {
        if ($searchSection.hasClass('active')) {
            $searchSection.removeClass('active');
            $mapSection.removeClass('active');
            $toggleIcon.attr('class', 'fi fi-rr-angle-double-small-right');
        } else {
            $searchSection.addClass('active');
            $mapSection.addClass('active');
            $toggleIcon.attr('class', 'fi fi-rr-angle-double-small-left');
        }
    });
});

