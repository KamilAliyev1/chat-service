async function signup(){

    var baseAddress = window.location.origin;
    var email = document.getElementById("email").value
    var password1 = document.getElementById("psw").value
    var password2 = document.getElementById("psw-repeat").value

    if(password1!=password2) {
        alert("passwords must be same")
        throw new Error("passwords must be same")
    }

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "email":email,
        "password":password1
    });
    
    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    const response = await fetch(baseAddress+"/v1/users",requestOptions);

    const data = await response.json();

    window.location.href = window.location.origin+'/login-page.html';


}

function setLoginUrl(){
    var a = document.getElementById("LogIn")
    a.href=window.location.origin+'/login-page.html'
}
setLoginUrl();