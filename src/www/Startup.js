var main=function()
{
	//Initial refresh the messages
assignevents();
refmsg();
setInterval(refmsg, 5000);
}


$(document).ready(main);