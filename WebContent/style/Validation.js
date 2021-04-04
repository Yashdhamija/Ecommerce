/**
 * 
 */




function validate() {

	var email = document.getElementById("Email").value;
	if (email.match("[a-zA-Z0-9_-]@[a-z]*.com")) {


		return true;

	}
	else {


		return false;


	}


}





function partnerValidation() { //For uid validation to be exactly 8 numbers

	var uid = document.getElementById("uid").value
	if (uid.toString().length != 8) {

		alert("Please enter an exact 8 digit number");
		return false;

	}

	else {


		return true;
	}



}

function SignUpAjax(address) {

	var request = new XMLHttpRequest();
	var data = '';
	/* add your code here to grab all parameters from form*/
	data += "firstName=" + document.getElementById("firstName").value
		+ "&";
	data += "lastName=" + document.getElementById("lastName").value +
		"&";
	data += "email=" + document.getElementById("email").value +
		"&";
	data += "password=" + document.getElementById("password").value +
		"&";
	
	data += "street=" + document.getElementById("street").value +
		"&"; 
	
	data += "province=" + document.getElementById("province").value +
		"&";
	
	data += "country=" + document.getElementById("country").value +
		"&";
	
	data += "zip=" + document.getElementById("zip").value +
		"&";
	
	data += "phone=" + document.getElementById("phone").value;

	console.log(data);

	request.open("GET", (address + "?" + data), true);
	request.onreadystatechange = function() {
		handler(request);
	};
	request.send();

}


let states = {

   Canada : [ 'Ontario','Quebec', 'Nova Scotia','New Brunswick', 'Manitoba', 'British Columbia', 'Prince Edward Island', 'Saskatchewan','Alberta','NewfoundLand'],
   'United States' : ["Alabama","Alaska","Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire", "New Jersey", "New Mexico", "New York","North Carolina","North Dakota","Ohio", "Oklahoma","Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia","Washington","West Virginia","Wisconsin","Wyoming"]




};













	function handler(request) {
		if ((request.readyState == 4) && (request.status == 200)) {
			var target = document.getElementById("result");
			target.innerHTML = request.responseText;
			console.log(request.response);


		}
		
	}
	 