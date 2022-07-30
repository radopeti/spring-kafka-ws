var stompClient = null;

function connect() {
    var socket = new SockJS('/action-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/actions', function (message) {
            console.log("msg", message);
            showGreeting(message.body);
        });
    });
}

function showGreeting(message) {
    console.log(message);
    $("#actions").prepend("<tr><td>" + message + "</td></tr>");
}

$(document).ready(connect());