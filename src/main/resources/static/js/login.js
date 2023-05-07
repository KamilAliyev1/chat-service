async function login() {
    event.preventDefault();
    
    var email = document.getElementById("username").value;
    var password = document.getElementById("password").value;


    console.log(email+" "+password)
    var requestOptions = {
        method: 'POST',
        redirect: 'follow'
      };

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    
    var raw = JSON.stringify({
        "username":email,
        "password":password
    });
    
    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };
    
    var response = await fetch(window.location.origin+"/v1/tokens/credential", requestOptions).then()
    const data = await response.json();

    localStorage.setItem("token",JSON.stringify(data))

    window.location.href = window.location.origin+'/chat-page';

}

function setSignupUrl(){
  var a = document.getElementById("signUP")
  a.href=window.location.origin+'/sign-up.html'
}
setSignupUrl()