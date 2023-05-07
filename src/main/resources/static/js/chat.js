var stompClient = null;
var baseAddress = window.location.origin;
var token = JSON.parse(localStorage.getItem("token")).accessToken;
function connect() {
    const headers = {
        Authorization: `Bearer ${token}`
    };
    
    var socket = new SockJS(baseAddress + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function(frame) {
        console.log('Connected: ' + frame);
    });
}
connect();

var userId = null;
var chatId = null;


async function getUser(){
  var myHeaders = new Headers();

  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", `Bearer ${token}`)

  
  var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  };

  const response = await fetch(baseAddress+"/v1/users",requestOptions);

  const data = await response.json();

  userId = data.id;

}
getUser();
var from = 0;
async function getMessages(){
  try {
    var myHeaders = new Headers();

    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", `Bearer ${token}`)
  
    
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };

    const response = await fetch(baseAddress+"/v1/chats/"+chatId+"/messages?from="+from,requestOptions);
    const data = await response.json();
    from=from+1;
    
    const list = document.getElementById("messageList");

    list.innerHTML=""


  data.forEach(element => {
    var temp = "message-data text-right"
    var temp2 = "message other-message float-right"
    var temp3 = "</br></br></br>";
    if(element.userId==userId){temp ="message-data";temp2="message my-message";temp3=""};
    
    const li = document.createElement("li")

    li.className="clearfix";
    li.innerHTML = `
    <li class="clearfix">
      <div class="${temp}">
        <a>${element.userId}</a>
      </div>
      <div class="${temp2}">${element.message}</div>
      <div class="${temp}">
      ${temp3}<span class="message-data-time">${element.creationTime}</span>
      </div>
    </li>
    
    `;
    list.appendChild(li);
  });
  } catch (error) {
    console.error(error);
  }
}

function handleReceivedMessage(message) {
  const list = document.getElementById("messageList");
  var temp = "message-data text-right"
    var temp2 = "message other-message float-right"
    var temp3 = "</br></br></br>";
    if(message.userId==userId){temp ="message-data";temp2="message my-message";temp3=""};

    const li = document.createElement("li")

    li.className="clearfix";
    li.innerHTML = `
    <li class="clearfix">
      <div class="${temp}">
        <a>${message.userId}</a>
      </div>
      <div class="${temp2}">${message.message}</div></br>
      <div class="${temp}">
      ${temp3}<span class="message-data-time">${message.creationTime}</span>
      </div>
    </li>
    
    `;
    list.appendChild(li);
    setTimeout(()=>{
      document.getElementsByClassName("chat")[0].scrollTo(0, document.getElementsByClassName("chat")[0].scrollHeight);
  
    }, 100);
}

function subscribeToChat(){
  const headers = {
    Authorization: `Bearer ${token}`
  };
  stompClient.subscribe("/topic/"+chatId, function (message) {
      handleReceivedMessage(JSON.parse(message.body));
  },headers);
}


function chatClick(event){
  const list = document.getElementById("chat-list");
  for (let index = 0; index < list.children.length; index++) {
    const element = list.children[index];
    if(element.className=="clearfix active")
        element.className = "clearfix";
  }
  document.getElementById("userName2").innerHTML=event.currentTarget.children[1].children[0].innerHTML;
  chatId = event.currentTarget.children[1].children[0].innerHTML;
  getMessages();
  subscribeToChat();
  document.getElementById("userFoto").children[0].src=event.currentTarget.children[0].src;
  event.currentTarget.className = "clearfix active";
  
}



async function getChats() {
  try {

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", `Bearer ${token}`)
  
    
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };


    const response = await fetch(baseAddress+"/v1/users/chats",requestOptions);
    const data = await response.json();


  const list = document.getElementById("chat-list");

  list.innerHTML=""
  var i = true;
  data.forEach(element => {
    const li = document.createElement("li")

    li.className="clearfix";
    li.addEventListener("click",chatClick)
    li.innerHTML = `
            <img src="https://bootdey.com/img/Content/avatar/avatar2.png" alt="avatar">
            <div class="about">
              <div class="name">${element.id}</div>
                <div class="status"> <i class="fa fa-circle online"></i> online </div>
            </div>
    
    `;
    list.appendChild(li);
    if(i){
      i=false
      li.click();
    }
  });


  setTimeout(()=>{
    document.getElementsByClassName("chat")[0].scrollTo(0, document.getElementsByClassName("chat")[0].scrollHeight);

  }, 100);




  } catch (error) {
    console.error(error);
  }
}



function sendMessage() {
  var text = document.getElementById('messageInput').value;
  const headers = {
    Authorization: `Bearer ${token}`
};
  stompClient.send("/chat/"+chatId, headers,
      JSON.stringify({'message':text}));
}







getChats();






async function loadMoreMessages(){
  try {
    var myHeaders = new Headers();

    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", `Bearer ${token}`)
  
    
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };

    const response = await fetch(baseAddress+"/v1/chats/"+chatId+"/messages?from="+from,requestOptions);
    const data = await response.json();
    from=from+1;
    
    const list = document.getElementById("messageList");


  data.forEach(element => {
    var temp = "message-data text-right"
    var temp2 = "message other-message float-right"
    var temp3 = "</br></br></br>";
    if(element.userId==userId){temp ="message-data";temp2="message my-message";temp3=""};
    
    const li = document.createElement("li")

    li.className="clearfix";
    li.innerHTML = `
    <li class="clearfix">
      <div class="${temp}">
        <a>${element.userId}</a>
      </div>
      <div class="${temp2}">${element.message}</div>
      <div class="${temp}">
      ${temp3}<span class="message-data-time">${element.creationTime}</span>
      </div>
    </li>
    
    `;
    list.insertBefore(li,list.firstChild);
  });
  } catch (error) {
    console.error(error);
  }
}


document.getElementsByClassName("chat")[0].addEventListener('scroll', function() {
  if (document.getElementsByClassName("chat")[0].scrollTop === 0) {
    loadMoreMessages();
  }
});



function disconnect(){
  if(stompClient != null) {
    const headers = {
      Authorization: `Bearer ${token}`
    };
    stompClient.disconnect(function(){
      console.log("Disconnected")
    },headers);
  }
  ;
}

window.onbeforeunload = disconnect;



