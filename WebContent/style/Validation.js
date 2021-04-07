/**
 * 
 */




function loginValidation() {



let username =  document.getElementById("Username");
let password = document.getElementById("signinpassword");
if(username.length > 0 && username.length <=40 &&  
password.length > 0 && password.length <=40 ) {
return true;
}

else {
return false;
}

}

 function unfade(element) {
    var op = 0.1;  // initial opacity
    element.style.display = 'block';
    var timer = setInterval(function () {
        if (op >= 1){
            clearInterval(timer);
        }
        element.style.opacity = op;
        element.style.filter = 'alpha(opacity=' + op * 100 + ")";
        op += op * 0.1;
    }, 10);
}



function userRegisterValidation() {



let fname =  document.getElementById("firstName").value;
let lname = document.getElementById("lastName").value;
let email  = document.getElementById("email");
let password  = document.getElementById("password");
let streetAddress = document.getElementById("street");
let city = document.getElementById("city");
let zip = document.getElementById("zip");
let phone = document.getElementById("phone");
let regexNum = new RegExp("[0-9]+")
let regexOnlyLetter =  new RegExp("/^[a-zA-Z]+$/");





 if ( (fname.length > 0 &&  fname.length <= 40)  &&  (lname.length > 0 && lname.length <= 40) ) {
 
  
   return true;
 }
   
 else {
 
  
	return false;
 	
 }
   





}




function paymentValidation() {

let name =  document.getElementById("cc-name").value;



 if( (name.length > 0 &&  name.length <= 40) ) {
    
    if(errorMessage) {
       errorMessage.remove();
     }
    return true;
 }
 
 
  else {


  return false;
 
 }


}






function SignUpAjax(address) {

	var request = new XMLHttpRequest();
	var data = '';


	console.log(data);

	request.open("GET", (address + "?" + data), true);
	request.onreadystatechange = function() {
		handler(request);
	};
	request.send();

}



function handler(request) {
	if ((request.readyState == 4) && (request.status == 200)) {
		var target = document.getElementById("result");
		target.innerHTML = request.responseText;
		console.log(request.response);


	}

}
