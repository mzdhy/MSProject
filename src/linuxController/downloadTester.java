package linuxController;


import java.io.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class downloadTester {
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
}
