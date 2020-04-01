import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;

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
		//outToClient=new PrintWriter(socket.getOutputStream(), true);
		clientSentence=inFromClient.readLine();
		if(clientSentence!=null)
		{
			//new Logger(clientSentence,socket);
			if(clientSentence.indexOf("?")!=-1)
			{
				outToClient=new PrintWriter(socket.getOutputStream(), true);
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
	
//System.out.println(name);
//System.out.println(fl.getName());
	if(fl.exists())
	{
	try
	{
		//Image,video and other binary files
		if(Arrays.asList(new String[]{"ico","tif","tiff","gif","jpg","jpeg","jif","jfif","jp2,","jpx","j2k","j2c","fpx","pcd","png","pdf"}).contains(fl.getName().split("\\.")[1]))
			{
				BufferedOutputStream out = new BufferedOutputStream( socket.getOutputStream() );
				BufferedInputStream reader = new BufferedInputStream( new FileInputStream( fl ) );
				// send OK headers and content length using f.length()
				byte[] buffer = new byte[ 4096 ];
				int bytesRead;
				while ( (bytesRead = reader.read(buffer)) != -1 )
				{
					out.write( buffer, 0, bytesRead );
				}
				reader.close();
				out.flush();
				out.close();
			}
			else{
		//Other text file
		opnScanner = new BufferedReader(new FileReader(fl));
		outToClient=new PrintWriter(socket.getOutputStream(), true);
		String s;
		while((s = opnScanner.readLine()) != null)
		{
                  outToClient.println(s);
		}
		opnScanner.close();
	        outToClient.println("\r\n");
			}

	}
	catch(Exception e)
	{
		e.printStackTrace();
		new Logger(e);
	}
	}
	else
	{
		try{outToClient=new PrintWriter(socket.getOutputStream(), true);}catch(Exception e){}
		outToClient.println("Invalid request");
		outToClient.println("\r\n");
	}
	
}

}