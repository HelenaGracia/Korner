

$( document ).ready(function(){
    $.get( "/numeroNotificaciones", function( data ) {
        $("#numeroNotifBadge").text(data)
    });

});