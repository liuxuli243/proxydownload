/**所有页面的ajax*/

$.ajax({		
	type: "GET",
	url: "/pc/sso/user/getUserByToken",
	success: function(data) {
		
	},
	error: function (XMLHttpRequest, textStatus, errorThrown) {
		if(XMLHttpRequest.status=='403'){
			window.location.href="/login.html"
		}
	}
});

function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}