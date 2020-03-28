import java.io.*;
import java.net.*;
import java.util.*;

class Logger
{
	String Remote = new String("");
	File fl=new File("log/log.txt");


Logger(String msg, Socket Sckt)
{
	try
	{
		FileWriter wrtlog = new FileWriter(fl,true);
		Remote+=Sckt==null?" ":Sckt.getInetAddress().toString()+":"+Sckt.getPort();
		wrtlog.write(new Date().toString()+"\t"+Remote+"\t\""+msg+"\"");
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

Logger(String msg)
{
	try
	{
		FileWriter wrtlog = new FileWriter(fl,true);
		wrtlog.write(new Date().toString()+"\t \t"+msg);
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

Logger(Exception e)
{
	try
	{
		FileWriter wrtlog = new FileWriter(fl,true);
		wrtlog.write(new Date().toString()+"\t \t"+e.toString());
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
	}
	catch(Exception f)
	{
		f.printStackTrace();
	}
}


}