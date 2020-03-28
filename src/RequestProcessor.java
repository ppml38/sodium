import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;

class RequestProcessor
{
Request request;
String response=new String("error");
public static String lastid=new String("0");

public RequestProcessor(Request rq)
{
request=rq;

switch(rq.action)
{
	case "/GETID":
		getid();
	break;
	
	case "/ADD":
		response=add();
	break;

	case "/SEND":
		response=updatemsg();
	break;
	
	default:
	response="invalid";
}

}

/*
* Function that returns response of every operation performed here
*/
public String getResponse()
{
return response;
}

public String add()
{
/*
Add a patient record
Sample request: "GET /ADD?id~id243@$record~test HTTP/1.1"
*/

try
{
FileWriter wrtlog = new FileWriter("pr/"+request.val[0]+".pr");
wrtlog.write(request.val[1]);
wrtlog.close();
}
catch(Exception e)
{
e.printStackTrace();new Logger(e);
return "error";
}

return "success";
}

public void getid()
{
	if(lastid.equals("0"))
	{
	try
	{
		BufferedReader br=new BufferedReader(new FileReader(new File("dat/lst.ct")));
		String n=br.readLine();
		br.close();
		lastid=n;
		response=lastid;
	}
	catch(Exception e)
	{
		e.printStackTrace();new Logger(e);
		response="error";
	}
	}
	else
	{
		response=lastid;
	}
		

}

public void setid()
{
int a=0;
try{
BufferedReader br=new BufferedReader(new FileReader(new File("dat/lst.ct")));
String n=br.readLine();
br.close();
a=Integer.parseInt(n);
a+=1;
FileWriter fw=new FileWriter(new File("dat/lst.ct"));
fw.write(a+"");
fw.close();

}catch(Exception e){ e.printStackTrace();new Logger(e);}

lastid=""+a;
}

public String updatemsg()
{
	/*
	* Update the incoming message in messages file with timestamp
	* Sample request SEND?id~id123@msg~Hello
	*/
String id=request.val[0],msg=request.val[1],time=new SimpleDateFormat("ddMMMMHH:mm").format(Calendar.getInstance().getTime());
try{
File[] filesList = new File("msg/").listFiles();
Boolean found=false;
        for(int t=0;t<Array.getLength(filesList);t++){
		File f = filesList[t];
            if(f.isFile()){
			    if(f.getName().indexOf("m"+time.substring(0,5)+".m")==0)
			    {
				found=true;
				FileWriter wr=new FileWriter(f,true);
				wr.write("<div class=\"redder\"><div class=\"sender\">"+id+"</div>"+msg+"<div class=\"time\">"+time+"</div></div>");
				wr.close();
			   }
			}
			}
			if(!found)
			{
				try
				{
					FileWriter wrtlog = new FileWriter("msg/"+"m"+time.substring(0,5)+".m");
					wrtlog.write("<div class=\"redder\"><div class=\"sender\">"+id+"</div>"+msg+"<div class=\"time\">"+time+"</div></div>");
					wrtlog.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();new Logger(e);
					return "error";
				}
			}
setid(); //Set new last id count
}
catch(Exception e){e.printStackTrace();new Logger(e); return "error";}
return "success";
}

} 