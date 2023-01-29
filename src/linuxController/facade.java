package linuxController;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;



public class facade {
 
 
	   private static  String ip;
	   private static  int port;
	   private static  String user;
	   private static  String pswd;
	   private static  ArrayList<String> output;
	   public static boolean isAuthenticated;
	   public static boolean started = false;
	   
	   
	   public static void connect(String i, int p, String u, String ps) {
		   ip = i;
		   port = p;
		   user = u;
		   pswd = ps;
	   }


	   public static String Check() {
	       try {

	           Connection conn = new Connection(ip,port);
	           conn.connect();
	           isAuthenticated = conn.authenticateWithPassword(user,
	                   pswd);
	           if (isAuthenticated == false)
	               throw new IOException("Authentication failed.");
	           Session sess = conn.openSession();
	           sess.execCommand("ps a");
	           InputStream stdout = new StreamGobbler(sess.getStdout());
	           BufferedReader br = new BufferedReader(
	                   new InputStreamReader(stdout));
	           

	           String result = "";
	           result += "Current ip: " + ip + "   Current port: " + port + "\n";
	           output= new ArrayList<String>();
	           int i = 0;
	           while (true) {

	               String line = br.readLine();
	               
	               if (line == null)
	                   break;
	               output.add(line);
	               result+=i+line+"\n";
	               i++;
	           }
	           
	           result+="\nPlease use \"http://localhost:8000/CloseProcess/?ip=192.168.108.129:22&Process1Index=1&Process2Index=3&Process3Index=4\" "
		          		+ "to close Process. \n\nProcess1Index, Process2Index ,Process3Index.... are the index of the process"
		          		+ " you want to close.\n\nFor Process1Index, Process2Index ,Process3Index, please input index of process by ascending order";
	           
	           conn.close();
	           
	           return result;
	         
	           
	           


	       } catch (IOException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();

	       }
	       

	   }
	   
	   public static String CheckAll() {
	       try {

	           Connection conn = new Connection(ip,port);
	           conn.connect();
	           isAuthenticated = conn.authenticateWithPassword(user,
	                   pswd);
	           if (isAuthenticated == false)
	               throw new IOException("Authentication failed.");
	           Session sess = conn.openSession();
	           sess.execCommand("ps a");
	           InputStream stdout = new StreamGobbler(sess.getStdout());
	           BufferedReader br = new BufferedReader(
	                   new InputStreamReader(stdout));
	           

	           String result = "";
	           result += "Current ip: " + ip + "   Current port: " + port + "\n";
	           output= new ArrayList<String>();
	           int i = 0;
	           while (true) {

	               String line = br.readLine();
	               
	               if (line == null)
	                   break;
	               output.add(line);
	               result+=i+line+"\n";
	               i++;
	           }
	           
//	           result+="\nPlease use \"http://localhost:8000/CloseProcess/?ip=192.168.108.129:22&Process1Index=1&Process2Index=3&Process3Index=4\" "
//		          		+ "to close Process. \n\nProcess1Index, Process2Index ,Process3Index.... are the index of the process"
//		          		+ " you want to close.\n\nFor Process1Index, Process2Index ,Process3Index, please input index of process by ascending order";
//	           
	           conn.close();
	           
	           return result;
	         
	           
	           


	       } catch (IOException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();

	       }
	       

	   }
	   
	   public static String remove(ArrayList<Integer> indexOfProcess) {
		   
		   try {
			   Connection conn = new Connection(ip,port);
	           conn.connect();
	           isAuthenticated = conn.authenticateWithPassword(user,
	                   pswd);
	           if (isAuthenticated == false)
	               throw new IOException("Authentication failed.");

	           
	           String result = "";
	           result += "Current ip: " + ip + "   Current port: " + port + "\n";
	           for(int i : indexOfProcess) {
	        	   if(i<=0||i>=output.size()) {
	        		   throw new IllegalArgumentException("invail process index");
	        	   }
	           }
		   		 
		          int i = 0;
		           while (true) {
		               if (i==output.size())
		                   break;
		               String line = output.get(i);
			           
		        	   if(indexOfProcess.contains(i)) {
		        		   int start = 0;
		        		   
		        		   while(line.charAt(start)<48||line.charAt(start)>57){
		        			   start++;
		        		   }
		        		   int end = start;
		        		   while(line.charAt(end)>=48&&line.charAt(end)<=57){
		        			   end++;
		        		   }
		        		   int pid = Integer.parseInt(line.substring(start,end));
		    	           Session sess = conn.openSession();
		        		   sess.execCommand("kill -9 "+pid);
		        		   sess.close();
		        	   }
		               result+=i+line+"\n";
		               i++;
		           }
		   }catch(IOException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();
		   }catch(IllegalArgumentException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();
		   }
		   return "Process close successfully";
		   

	           
	   }

	   public static String removeByName(ArrayList<String> name) {
		   Check();
		   try {
			   Connection conn = new Connection(ip,port);
	           conn.connect();
	           isAuthenticated = conn.authenticateWithPassword(user,
	                   pswd);
	           if (isAuthenticated == false)
	               throw new IOException("Authentication failed.");

	           
	           String result = "";
	           result += "Current ip: " + ip + "   Current port: " + port + "\n";

		   		 
		          int i = 1;
		          int l = output.get(0).indexOf("COMMAND");
		          System.out.println(l);
		           while (true) {
		               if (i==output.size())
		                   break;
		               String line = output.get(i);
		               String r = output.get(i).substring(l);
			           System.out.println(line);
		        	   if(name.contains(r)) {
		        		   int start = 0;
		        		   
		        		   while(line.charAt(start)<48||line.charAt(start)>57){
		        			   start++;
		        		   }
		        		   int end = start;
		        		   while(line.charAt(end)>=48&&line.charAt(end)<=57){
		        			   end++;
		        		   }
		        		   int pid = Integer.parseInt(line.substring(start,end));
		    	           Session sess = conn.openSession();
		        		   sess.execCommand("kill -9 "+pid);
		        		   sess.close();
		        	   }
		               result+=i+line+"\n";
		               i++;
		           }
		   }catch(IOException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();
		   }catch(IllegalArgumentException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();
		   }
		   return "Process close successfully";
		   

	           
	   }
	   
	   public static String instruction(String ins) {
	       try {

	           Connection conn = new Connection(ip,port);
	           conn.connect();
	           isAuthenticated = conn.authenticateWithPassword(user,
	                   pswd);
	           if (isAuthenticated == false)
	               throw new IOException("Authentication failed.");
	           Session sess = conn.openSession();
	           sess.execCommand(ins);
	           InputStream stdout = new StreamGobbler(sess.getStdout());
	           BufferedReader br = new BufferedReader(
	                   new InputStreamReader(stdout));
	           

	           String result = "";
	           output= new ArrayList<String>();
	           int i = 0;
	           while (true) {

	               String line = br.readLine();
	               
	               if (line == null)
	                   break;
	               output.add(line);
	               result+=line+"\n";
	               i++;
	           }
	           
           
	           conn.close();
	           
	           result += "\nsuccessful\n";
	           return result;
	         
	           
	           


	       } catch (IOException e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();

	       }
	   }
	   
	   public static String link() {
    	   if(!multipleIns.link) {
    		   
	    	   String s = "Current ip: " + ip + "   Current port: " + port + "\n"+multipleIns.link(ip,user,pswd,port);
	    	   if(s.contains("start successfully")) {
	    		   started = true;
	    	   }
	    	   return s;
    	   }else {
    		   return "please use \"http://localhost:8000/closeShell\" to close before start";
    	   }
	   }
	   
	   public static String shellIns(String ins) {
		   if(!started) {
			   return "please use \"http://localhost:8000/startShell\" to start";
		   }
	       try {
	    	   
	    	   return multipleIns.ins(ins);
	           
	           


	       } catch (Exception e) {
	           e.printStackTrace(System.err);
	           return e.getMessage();

	       }
	   }
	   
	   
	   public static String closeShell() {
		   if(!started) {
			   return "please use \"http://localhost:8000/startRemoteShell\" to start before close";
		   }else {
			   multipleIns.close(); 
			   started = false;
			   return "close successfully";
		   }

	   }
	   
	   public static String upload(String sourceFile, String destinationFile) {
		   return uploadTester.upload(ip, user, pswd, port, sourceFile, destinationFile);
	   }
	   
	   public static String upload(String sourceFile) {
		   return uploadTester.upload(ip, user, pswd, port, sourceFile);
	   }
	   
	   public static String uploadAll(String folderName) {
		   return uploadTester.uploadAll(ip, user, pswd, port, folderName);
	   }
	   
	   public static String uploadAll() {
		   return uploadTester.uploadAll(ip, user, pswd, port);
	   }
	   
	   public static String download(String Filename, String destinationFile) {
		   return downloadTester.download(ip, user, pswd, port, Filename, destinationFile);
	   }
	   
	   public static String download(String Filename) {
		   return downloadTester.download(ip, user, pswd, port, Filename);
	   }
	   

	

}