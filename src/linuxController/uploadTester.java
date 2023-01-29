package linuxController;

import com.jcraft.jsch.*;
import java.io.*;

public class uploadTester {

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


            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            File localFolder = new File(localDirectory);
            for (File file : localFolder.listFiles()) {
            	
                sftp.put(localDirectory+File.separator+file.getName(), destinationFile);
            }

            sftp.exit();
            session.disconnect();

            System.out.println("File uploaded successfully.");
            return "AllFile uploaded successfully";
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String uploadAll(String hostname, String username, 
    		String password,int port) {

		String rootpath = System.getProperty("user.dir");
		String localDirectory = rootpath+File.separator+"uploadAll";
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
            File localFolder = new File(localDirectory);
            for (File file : localFolder.listFiles()) {
            	
                sftp.put(localDirectory+File.separator+file.getName(), destinationFile);
            }


            sftp.exit();
            session.disconnect();

            System.out.println("File uploaded successfully.");
            return "AllFile uploaded successfully";
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
