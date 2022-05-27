import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
//import Lastfinding.scalability.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
public class ServerFileReading {

	
	public static void downloadFile() {
		
		try {
          String host = "203.101.226.13";
          String user = "ubuntu";
          String privateKey = "C://Users/theoe/OneDrive - University of Tasmania/Sem 1 2022/KIT318 - Big Data and Cloud Computing/privatekey3.ppk"; //please provide your ppk file
          JSch jsch = new JSch();
          Session session = jsch.getSession(user, host, 22);
          Properties config = new Properties();
         // session.setPassword("KIT418@utas"); ////if password is empty please comment it
          jsch.addIdentity(privateKey);
          System.out.println("identity added "); 
          config.put("StrictHostKeyChecking", "no");
          session.setConfig(config);;
          session.connect();
       
          Channel channel = session.openChannel("sftp");
          channel.connect();
          ChannelSftp sftpChannel = (ChannelSftp) channel;
          sftpChannel.get( "/home/ubuntu/ServerFileReading2.java","D:/testjars/ServerFileReading3.java");
          
          sftpChannel.exit();
          session.disconnect();
      } catch (JSchException e) {
          e.printStackTrace();
      } catch (SftpException e) {
          e.printStackTrace();
      }
		catch(Exception e){
      	   System.out.println(e);
		}   
	}
	
	public static void uploadfile() {
	try {
          String host = "203.101.226.13"; //Add IP of vm
          String user = "ubuntu";
          String privateKey = "C://Users/theoe/OneDrive - University of Tasmania/Sem 1 2022/KIT318 - Big Data and Cloud Computing/privatekey3.ppk"; //please provide your ppk file
          JSch jsch = new JSch();
          Session session = jsch.getSession(user, host, 22);
          Properties config = new Properties();
         // session.setPassword("KIT418@utas"); ////if password is empty please comment it
          jsch.addIdentity(privateKey);
          System.out.println("identity added "); 
          config.put("StrictHostKeyChecking", "no");
          session.setConfig(config);
          session.connect();
       
          Channel channel = session.openChannel("sftp");
          channel.connect();
          ChannelSftp sftpChannel = (ChannelSftp) channel;
          sftpChannel.put("C://Users/theoe/OneDrive - University of Tasmania/Sem 1 2022/KIT318 - Big Data and Cloud Computing/ServerFileReading.java", "/home/ubuntu/ServerFileReading2.java");
         
          System.out.println("done");

          sftpChannel.exit();
          session.disconnect();
      } catch (JSchException e) {
          e.printStackTrace();
      } catch (SftpException e) {
          e.printStackTrace();
      }
		catch(Exception e){
      	   System.out.println(e);
		}   
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		downloadFile();
		uploadfile();

	}

}
