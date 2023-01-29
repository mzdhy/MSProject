package linuxController;

import com.jcraft.jsch.*;
import java.io.*;

public class multipleIns {


	private static Session session;
	private static java.util.Properties config;
	private static Channel channel;
	private static InputStream in;
	private static OutputStream out;
	public static boolean link = false;
	    public static String link(String host, String user, String password,int port) {
	        try {
	            JSch jsch = new JSch();



	            session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	            config = new java.util.Properties();
	            config.put("StrictHostKeyChecking", "no");
	            session.setConfig(config);
	            session.connect();
	            
	            channel = session.openChannel("shell");
	            channel.connect();
	            in = channel.getInputStream();
	            out = channel.getOutputStream();
	            
	            link = true;
	            

	            return "start successfully";



	        }catch(Exception e){
	        	System.out.println(e.getMessage());
	        	return e.getMessage();
	        }
	    }
	    
	    public static String ins(String command) {
	    	String s = "";
	    	try {
	            out.write((command + "\n").getBytes());
	            out.flush();
	            Thread.sleep(1000);
	            byte[] tmp = new byte[1024];
	            while (in.available() > 0) {
	                int i = in.read(tmp, 0, 1024);
	                if (i < 0) break;
	                System.out.print(new String(tmp, 0, i));
	                s+=new String(tmp, 0, i);
	            }
	    	}catch(Exception e) {
	    		System.out.println(e.getMessage());
	    		return e.getMessage();
	    	}
	    	
	    	return s;

	    }
	    public static void close() {
            channel.disconnect();
            session.disconnect();
	    }

}
