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

	public static String downloadFile(String filename) {
		String file_contents = null;
		
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
          
          InputStream stream = sftpChannel.get("/home/ubuntu/" + filename);
          StringBuffer sb = new StringBuffer();
          try {
              BufferedReader br = new BufferedReader(new InputStreamReader(stream));
              
              String line;
              
              while ((line = br.readLine()) != null) {
            	  sb.append(line);
              }
            
              file_contents = sb.toString();
          } finally {
              stream.close();
          }
          
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
		
		return file_contents;
	}
	
	public static void uploadfile(String file_path) {
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
          sftpChannel.put(file_path, "/home/ubuntu/" + file_path.split("/")[file_path.split("/").length -1]); // Finds the file name in the directory after the last '/' character
          // If not working, perhaps change to '\'
          
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

	
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		downloadFile();
		uploadfile();

	}*/

}