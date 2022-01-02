var stompClient = null;
const connection = {}

function drawChat(message) {
  $("#notifications").append("<tr><td>" + message + "</td></tr>");
}

function sendChat() {
  stompClient.send(
      // "/user/" + $( "#target" ).val() + "/chats",
      "/app/chats/send/" + $("#target").val(),
      {},
      JSON.stringify({'content': $( "#text" ).val()}));
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $( "#quit" ).click(function() {
    stompClient.disconnect();
  });
  $( "#send" ).click(function() {
    sendChat();
  });

  const socket = new SockJS('/websocket-stomp');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // subscribe to incoming messages
    stompClient.subscribe('/user/chats',
        (notification) => {
          const parsed = JSON.parse(notification.body);
          drawChat(parsed.sender + ": " + parsed.message);
        });
  });

});