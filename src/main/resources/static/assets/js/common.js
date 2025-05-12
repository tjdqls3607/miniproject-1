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

function makeDateStr(dateTime){
	const date = new Date(dateTime);
    return ((date.getMonth() + 1) + "월") + (date.getDate() + "일 ") 
	+ (date.getHours() + "시") + (date.getMinutes().toString().padStart(2,"0") + "분");
}