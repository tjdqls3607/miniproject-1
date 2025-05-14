async function checkLogin(){
	const token = localStorage.getItem('token');
	const loginCheck = document.getElementById("loginCheck");
	const btnLogin = document.getElementById("btnLogin");
	const btnSignup = document.getElementById("btnSignup");
	const btnLogout = document.getElementById("btnLogout");
	const userProfileImage = document.getElementById("userProfileImage");


	if(token){
		document.querySelector("#userProfileImage").style.display='inline';
		document.querySelector("#btnLogin").style.display='none';
		document.querySelector("#btnSignup").style.display='none';
		if (btnLogout) btnLogout.style.display = "inline-block";
	} else {
		document.querySelector("#userProfileImage").style.display='none';
		document.querySelector("#btnLogin").style.display='inline';
		document.querySelector("#btnSignup").style.display='inline';
		if (btnLogout) btnLogout.style.display = "none";
	}

	document.querySelector("#loginCheck").style.display='block';	
}

function makeDateStr(dateTime){
	const date = new Date(dateTime);
    return ((date.getMonth() + 1) + "월") + (date.getDate() + "일 ") 
	+ (date.getHours() + "시") + (date.getMinutes().toString().padStart(2,"0") + "분");
}