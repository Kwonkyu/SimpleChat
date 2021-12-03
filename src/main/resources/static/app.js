var stompClient = null;
const connection = {}

function connect() {
  const username = document.getElementById("writer").value;
  connection.username = stompClient.subscribe('/notice/' + username, (notification) => {
    showGreeting(JSON.parse(notification.body).createdPostTitle);
  });
}

function showGreeting(message) {
  $("#notifications").append("<tr><td>" + message + "</td></tr>");
}

function disconnect() {
  const username = document.getElementById("writer").value;
  connection.username.unsubscribe();
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $( "#connect" ).click(function() { connect(); });
  $( "#disconnect" ).click(function() { disconnect(); });
  $( "#quit" ).click(function() {
    stompClient.disconnect();
  });

  const socket = new SockJS('/websocket-stomp');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) { // client id should be jwt?
    console.log('Connected: ' + frame);
  });
});