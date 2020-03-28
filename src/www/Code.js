function showloading()
{
$("#hold").css("visibility","visible");
}
function hideloading()
{
$("#hold").css("visibility","hidden");
}

function dateformat(n)
{
	return n<10? '0'+n:n;
}

function ShowLocalDate()
{
	const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"];
var dNow = new Date();
var localdate= dateformat(dNow.getDate())+''+monthNames[dNow.getMonth()] +''+ dNow.getHours() + ':' + dNow.getMinutes();
return localdate;
}


function addpr()
{
/*
Add a patient record
Sample request: "GET /ADD?id~id243@$record~test HTTP/1.1"
*/

if(($.trim($("#pr").val())!="")&($.trim($("#pid").val())!=""))
{
	showloading();
	$.get("ADD",
		  "id~"+$("#pid").val()+"@$record~"+encodeURIComponent($.trim($("#pr").val())),
					function(data)
					{
						data=$.trim(data);
						if(data=="success")
						{
						hideloading();
						$("#status").css("display","inline-block");
						setTimeout(function(){ $("#status").css("display","none"); }, 3000);
						}
						else alert("Error connecting Server");
					});
				   
}
else{
	alert("Please enter both patient id and record details");
}

}

var firstTime=true;
function scrolldown()
{
	var div = document.getElementById("discussion");
	if(firstTime)
	{div.scrollTop=div.scrollHeight; firstTime=false;}
	else
	{
	if((div.scrollHeight-div.scrollTop)<(2*div.clientHeight)){
    div.scrollTop=div.scrollHeight;
	}
	else
	{
		//Show new message arrived
		$("#godown").css("display","inline-block");
	}
	}
}

function forcescrolldown()
{
	var div = document.getElementById("discussion");
	div.scrollTop=div.scrollHeight;
	$("#godown").css("display","none");
}

function showpr()
{
/*
Read patient record from server and show in text area
Sample request: "GET /id123.pr HTTP/1.1"
*/

if($.trim($("#pid").val())!="")
{
	showloading();
	$.get($.trim($("#pid").val())+".pr","",
					function(data)
					{
						$("#pr").val("");
						$("#pr").val(decodeURIComponent(data));
						hideloading();
					});
				   
}
else{
	alert("Please enter patient id");
}

}
/*************************************
Message auto sync functions
**************************************/
var req_complete=true;
var lastid=0;

function refresh(){
if(req_complete)
{
	//Start the request
	showloading();
	req_complete=false;
	$.get("m"+ShowLocalDate().substring(0,5)+".m","",
					function(data)
					{
						if(decodeURIComponent($.trim(data))=="Invalid request")
						{
							$("#discussion").html("");
							hideloading();
							scrolldown();
							req_complete=true;
						}
						else
						{
							$("#discussion").html("");
							$("#discussion").append(decodeURIComponent($.trim(data)));
							hideloading();
							scrolldown();
							req_complete=true;
						}
					});
}
}

function refmsg()
{
/*
Read patient record from server and show in text area
Sample request: "GET /id123.pr HTTP/1.1"
*/
if(req_complete)
{
	req_complete=false;
	//Check if there is any update on the message
	$.get("GETID","a~a",function(data){
					data=$.trim(data);
					if(data=="error")
					{
						//There is an error in the server. so dont disturb
					}
				else
				{
					if(lastid!=data)
					{
						lastid=data;
						req_complete=true; //Important as refresh function checks if previous request is complete
						refresh();
					}
					else{
						//NO new message received
					}
				}
					req_complete=true;
				   });
}
}

/************************************/



function sendmsg()
{
/*
Send message to server and update the chat screen
Sample request: "GET /id123.pr HTTP/1.1"
*/

if(($.trim($("#uid").val())!="")&($.trim($("#msg").val())!=""))
{
	showloading();
	$.get("SEND",
		  "id~"+$("#uid").val()+"@msg~"+encodeURIComponent($.trim($("#msg").val())),
					function(data)
					{
						data=$.trim(data);
						if(data=="success")
						{
						hideloading();
						//$("#discussion").append("<div class=\"redder\"><div class=\"sender\">"+$("#uid").val()+"</div>"+$("#msg").val()+"<div class=\"time\">"+ShowLocalDate()+"</div></div>");
						$("#msg").val("");
						//scrolldown();
						refmsg();
						}
						else alert("Error connecting Server");
					});
				   
}
else
{
	alert("Please enter both your id and message");
}

}


function assignevents()
{

$("#addbtn").click(
function()
{
addpr();
}
);

$("#updatebtn").click(
function()
{
addpr();
}
);

$("#showbtn").click(
function()
{
showpr();
}
);

$("#msg").keyup(function(event) {
    if (event.keyCode === 13) {
        sendmsg();
    }
});

$("#uid").keyup(function(event) {
    if (event.keyCode === 13) {
        sendmsg();
    }
});

$("#pid").keyup(function(event) {
    if (event.keyCode === 13) {
        showpr();
    }
});

$("#sendbtn").click(
function()
{
sendmsg();
}
);

$("#refbtn").click(
function()
{
refmsg();
}
);

$("#godown").click(
function()
{
forcescrolldown();
}
);

}

