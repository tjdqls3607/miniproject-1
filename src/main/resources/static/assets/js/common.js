async function checkLogin(){
	const token = localStorage.getItem('token');
	
	if(token){
		document.querySelector("#userProfileImage").style.display='inline';
			
		document.querySelector("#btnLogin").style.display='none';
		document.querySelector("#btnSignup").style.display='none';
	} else {
		document.querySelector("#userProfileImage").style.display='none';
			
		document.querySelector("#btnLogin").style.display='inline';
		document.querySelector("#btnSignup").style.display='inline';
	}

	document.querySelector("#loginCheck").style.display='block';	
}