var stompClient = null;

function connect() {
    var socket = new SockJS('/action-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connection successful: ' + frame);
        showSuccessMessage();
        stompClient.subscribe('/topic/actions', function (action) {
            showAction(action.body);
        });
    });
}

function showAction(action) {
    console.log(action);
    $("#actions").prepend("<tr><td>" + action + "</td></tr>");
}

function showSuccessMessage() {
    $("#head").text("Actions - (websocket connection established)");
}

$(document).ready(connect());