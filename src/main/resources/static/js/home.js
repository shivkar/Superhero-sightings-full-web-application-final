/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    loadLocations();

    //Load locations from server
    function loadLocations() {
        //Send an api request
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/sightingsmap ',
            success: function (locations) {
                initMap(locations);
            },
            error: function () {
                alert('FAILURE!');
            }
        });
    
    function initMap(locations) {
        // The location
        const initLoc = {lat: 43.54245, lng: -78.72037}
        const map = new google.maps.Map(document.getElementById("map"), {
            center: initLoc, zoom: 4, })
        //Add locations and markers to map
        for (i = 0; i < locations.length; i++) {
            var latitude = parseFloat(locations[i].location.latitude);
            var longitude = parseFloat(locations[i].location.longitude);
            var position = {lat: latitude, lng: longitude};
             var label = "<div class='info'>"+locations[i].superhero.name +" <br/> " + locations[i].location.locname   + "</div>";
        
            const marker = new google.maps.Marker({
                position,
                map
            });
            const infoWindow = new google.maps.InfoWindow({
                content: label,
                disableAutoPan: true
            });
            marker.addListener("click", () => {
                infoWindow.open(map, marker);
            });

            
        }
    }
}
});