package com.cg.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Main {
	
	public static void main(String[] args) throws Exception
	{
		try(ServerSocket serverSocket = new ServerSocket(8080))
		{
			System.out.println("Server started succesfully! Listening for messages");
			while(true)
			{
				try(Socket client = serverSocket.accept())
				{
					System.out.println("Debug info: "+ client.toString());
					
					InputStreamReader stream = new InputStreamReader(client.getInputStream());
					BufferedReader reader = new BufferedReader(stream);
					
					StringBuilder request = new StringBuilder();
					String line;
					line = reader.readLine();
					while(line.trim().length()>0)
					{
						request.append(line + "\r\n");
						line = reader.readLine();
					}
					System.out.println("--REQUEST--");
					System.out.println(request);
					
					File targetFile = new File("C:/Users/kaprakas/Workspace_Projects/Xcorps/Output/request.txt");
		            targetFile.createNewFile();

		            Writer targetFileWriter = new FileWriter(targetFile);
		            targetFileWriter.write(request.toString());
		            targetFileWriter.close();
					
					String firstLine = request.toString().split("\n")[0];
					String route = firstLine.split(" ")[1];
					if(route.equals("/welcome"))
					{
						OutputStream clientOutput = client.getOutputStream();
						clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
						clientOutput.write(("\r\n").getBytes());
						clientOutput.write(("<b>Welcome to the server<b>").getBytes());
						clientOutput.flush();
					}
					else if(route.equals("/pic"))
					{
						//System.out.println(System.getProperty("user.dir"));
						Path path = Paths.get("assets/pic1.jpg");
						OutputStream clientOutput = client.getOutputStream();
						clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
						clientOutput.write(("\r\n").getBytes());
						clientOutput.write(Files.readAllBytes(path));
						clientOutput.flush();
					}
					else
					{
						OutputStream clientOutput = client.getOutputStream();
						clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
						clientOutput.write(("\r\n").getBytes());
						clientOutput.write(("<b>What do you want?<b>").getBytes());
						clientOutput.flush();
					}
					client.close();
					
				}
			}
				
		}
	}
	

}
