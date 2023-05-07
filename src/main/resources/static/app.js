var stompClient = null;
var baseAddress = 'http://localhost:8080';

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    var socket = new SockJS(baseAddress + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}


function subscribeToChat(to){
    stompClient.subscribe("/topic/"+to, function (message) {
        handleReceivedMessage(JSON.parse(message.body));
    });
}


function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var text = document.getElementById('text').value;
    stompClient.send("/chat/1", {},
        JSON.stringify({'message':text}));
}

function handleReceivedMessage(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message.sender + ": " + message.message ));
    response.appendChild(p);
}

function getChatsFromApi(){

    var requestOptions = {
      method: 'GET',
      redirect: 'follow'
    };

    fetch("http://localhost:8080/v1/users/chats", requestOptions)
      .then(response => response.json())
      .then(result => result.forEach(addChatsToHtml))
      .catch(error => console.log('error', error));
}

function addChatsToHtml(chat){
    var chats = document.getElementById('chats');
    var button = document.createElement('button');
    button.innerHTML = chat.id;
    button.onclick = function(){
        subscribeToChat(chat.id);
    };
    chats.appendChild(button);
}