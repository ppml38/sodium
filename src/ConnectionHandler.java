import java.io.*;
import java.net.*;
import java.util.*;

class ConnectionHandler implements Runnable
{

Socket socket=null;

PrintWriter      outToClient;
BufferedReader   inFromClient;

String clientSentence;

public ConnectionHandler(Socket sct)
{
socket=sct;
}

public void run()
{
	try{
		inFromClient=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outToClient=new PrintWriter(socket.getOutputStream(), true);
		clientSentence=inFromClient.readLine();
		if(clientSentence!=null)
		{
			//new Logger(clientSentence,socket);
			if(clientSentence.indexOf("?")!=-1)
			{
				outToClient.println(new RequestProcessor(new Request(clientSentence)).getResponse());
				outToClient.println("\r\n");
			}
			else
				send(clientSentence.split("\\s")[1]);
			
			socket.close();
		}
	}
	catch(Exception e){e.printStackTrace();new Logger(e);}
}

public void send(String name)
{
	File fl;
	BufferedReader opnScanner;

	if(name.equals("/")){
		fl=new File("www/index.html");
	}
	else{
		if(name.substring(name.length()-3).equals(".pr"))
		{
			fl=new File("pr/"+name.substring(1));
		}
		else
		{
			if(name.substring(name.length()-2).equals(".m"))
			{
				fl=new File("msg/"+name.substring(1));
			}
			else
			{
				fl=new File("www/"+name.substring(1));
			}
		}
	}
	

	if(fl.exists())
	{
	try
	{

		opnScanner = new BufferedReader(new FileReader(fl));
		String s;
		while((s = opnScanner.readLine()) != null)
		{
                  outToClient.println(s);
		}
		opnScanner.close();
	        outToClient.println("\r\n");

	}
	catch(Exception e)
	{
		e.printStackTrace();
		new Logger(e);
	}
	}
	else
	{
		outToClient.println("Invalid request");
		outToClient.println("\r\n");
	}
	
}

}