package linuxController;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class uploadTester {
	
	public static class UploadThread implements Runnable {
	    private String destinationFile;
	    private String path;
	    private Session session;

	    public UploadThread(Session session, String path, String destinationFile) {
	        this.session = session;
	        this.destinationFile = destinationFile;
	        this.path = path;
	    }

	    @Override
	    public void run() {
	        try {
	            Channel channel = session.openChannel("sftp");
	            channel.connect();
	            ChannelSftp sftp = (ChannelSftp) channel;
	            sftp.put(path, destinationFile);
	            sftp.exit();
	        } catch (SftpException | JSchException e) {
	            e.printStackTrace();
	        }
	    }
	}

    public static String upload(String hostname, String username, 
    		String password,int port,String sourceFile) {

		String rootpath = System.getProperty("user.dir");
        sourceFile = rootpath+"/"+"upload/"+sourceFile;
        String destinationFile = "/root";

        try {

            JSch jsch = new JSch();

            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();


            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            sftp.put(sourceFile, destinationFile);

            sftp.exit();
            session.disconnect();

            System.out.println("File uploaded successfully.");
            return "File uploaded successfully";
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String upload(String hostname, String username, 
    		String password,int port,String sourceFile, String destinationFile) {

		String rootpath = System.getProperty("user.dir");
        sourceFile = rootpath+"/"+"upload/"+sourceFile;
        destinationFile = "/root" + "/"+destinationFile;

        try {

            JSch jsch = new JSch();

            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();


            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            sftp.put(sourceFile, destinationFile);

            sftp.exit();
            session.disconnect();

            System.out.println("File uploaded successfully.");
            return "File uploaded successfully";
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String uploadAll(String hostname, String username, 
    		String password,int port,String folderName) {

		String rootpath = System.getProperty("user.dir");
		String localDirectory = rootpath+File.separator+"uploadAll";
        String destinationFile = "/root"+"/"+folderName;

        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//            ChannelSftp sftp = (ChannelSftp) channel;
            File localFolder = new File(localDirectory);
            List<Thread> threads = new ArrayList<>();
            for (File file : localFolder.listFiles()) {
                Thread thread = new Thread(new UploadThread(session, localDirectory+File.separator+file.getName(),destinationFile));
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            
            session.disconnect();

            System.out.println("Files uploaded successfully.");
            return "All files uploaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    
    public static String uploadAll(String hostname, String username, String password, int port) {
        String rootpath = System.getProperty("user.dir");
        String localDirectory = rootpath + File.separator + "uploadAll";
        String destinationFile = "/root";

        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//            ChannelSftp sftp = (ChannelSftp) channel;
            File localFolder = new File(localDirectory);
            List<Thread> threads = new ArrayList<>();
            for (File file : localFolder.listFiles()) {
                Thread thread = new Thread(new UploadThread(session, localDirectory+File.separator+file.getName(),destinationFile));
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            
            session.disconnect();

            System.out.println("Files uploaded successfully.");
            return "All files uploaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
