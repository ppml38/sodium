import java.io.*;
import java.net.*;
import java.util.*;

class Sodium
{

public static void main(String[] args)
{

Server srvr=new Server();
System.out.println("Sodium server is running");
srvr.startServer();

}

}


class Server
{

ServerSocket Srvrsckt =null;
Socket       Sckt     =null;


public void startServer()
{

	try
	{
		Srvrsckt=new ServerSocket(80);
		while(true)
		{
			Sckt    =Srvrsckt.accept();
			new Thread(new ConnectionHandler(Sckt)).start();
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		new Logger(e);
	}
			

}
}

