package linuxController;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import linuxController.uploadTester.UploadThread;

public class downloadTester {
	
	public static class DownloadThread implements Runnable {
	    private String destinationFile;
	    private String path;
	    private ChannelSftp.LsEntry entry;
	    private Session session;

	    public DownloadThread(String path,ChannelSftp.LsEntry entry ,String destinationFile, Session session) {
	        this.entry = entry;
	        this.destinationFile = destinationFile;
	        this.path = path;
	        this.session = session;
	    }

	    @Override
	    public void run() {
	        try {
	             Channel channel = session.openChannel("sftp");
	             channel.connect();

	             ChannelSftp sftpChannel = (ChannelSftp) channel;
                 sftpChannel.get(path, destinationFile);
  	             sftpChannel.exit();
	        } catch (SftpException | JSchException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
    public static String download(String host, String user, 
    		String password,int port,String remoteFile) {
    	
        remoteFile = "/root/"+remoteFile;
		String rootpath = System.getProperty("user.dir");
        String localFile = rootpath+File.separator+"download";
        System.out.println(rootpath);
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            sftpChannel.get(remoteFile, localFile);
            sftpChannel.exit();
            session.disconnect();

            System.out.println("File downloaded successfully from " + host);
            return "File downloaded successfully from " + host;
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String download(String host, String user, 
    		String password,int port,String remoteFile,String Folder) {
    	
        remoteFile = "/root/"+Folder+"/"+remoteFile;
		String rootpath = System.getProperty("user.dir");
        String localFile = rootpath+File.separator+"download";
        System.out.println(rootpath);
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            sftpChannel.get(remoteFile, localFile);
            sftpChannel.exit();
            session.disconnect();

            System.out.println("File downloaded successfully from " + host);
            return "File downloaded successfully from " + host;
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String downloadAll(String host, String user, 
    		String password,int port,String Folder) {
    	
        String remoteFolder = "/root/"+Folder;
		String rootpath = System.getProperty("user.dir");
        String localFile = rootpath+File.separator+"downloadAll";
        System.out.println(rootpath);
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftpChannel = (ChannelSftp) channel;
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remoteFolder);
          
            List<Thread> threads = new ArrayList<>();
            for (ChannelSftp.LsEntry entry : list) {
                if (!entry.getAttrs().isDir()) {
                	
                	String fileName = entry.getFilename();
                	Thread thread = new Thread(new DownloadThread(remoteFolder + "/" + fileName, entry,localFile,session));
                	threads.add(thread);
                	thread.start();
                }
            }
            for (Thread thread : threads) {
                thread.join();
            }

            sftpChannel.exit();
            session.disconnect();

            System.out.println("File downloaded successfully from " + host);
            return "File downloaded successfully from " + host;
        } catch (JSchException | SftpException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
