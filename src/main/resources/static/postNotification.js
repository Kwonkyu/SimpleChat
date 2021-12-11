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
      {'Authorization': 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZW1iZXIxIiwiaWF0IjoxNjM5MjQzNDU5fQ.7kR3HG0781KFF45vyiLGtkHxfF-uf7DmMXe2M2IT-9s'},
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