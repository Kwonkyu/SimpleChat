var stompClient = null;

function showPostNotification(message) {
  $("#notifications").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  const socket = new SockJS('/websocket-stomp');
  stompClient = Stomp.over(socket);
  stompClient.connect(
      {'Authorization': 'SET_TOKEN_HERE'},
      function (frame) { // client id should be jwt?
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/posts",
            (postNoti) => {
              const parsed = JSON.parse(postNoti.body);
              showPostNotification(
                  `${parsed.username} wrote article ${parsed.createdPostTitle}`);
            });
      });
});