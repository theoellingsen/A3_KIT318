
import java.util.Base64;
import java.util.List;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.image.Image;
import org.openstack4j.openstack.OSFactory;

public class CloudTutorial
{
	OSClientV3 os= null;
	public CloudTutorial() {
		os = OSFactory.builderV3()//Setting up openstack client with  -OpenStack factory
				.endpoint("https://keystone.rc.nectar.org.au:5000/v3")//Openstack endpoint
				.credentials("shoskin@utas.edu.au", "ZmNlMzIxMmVjMmVlNWI5",Identifier.byName("Default"))//Passing credentials
				.scopeToProject( Identifier.byId("2fcf213d627b4251895a1b9a1b029eb0"))//Project id
				.authenticate();//verify the authentication
	}
	//Creating a new instance or VM or Server
	public String[] createServer() {
		String script = Base64.getEncoder().encodeToString(("#!/bin/bash\n" + "sudo mkdir ~/ubuntu/temp").getBytes());//encoded with Base64. Creates a temporary directory
		ServerCreate server = Builders.server()//creating a VM server
				.name("Assignment3")//VM or instance name
				.flavor("406352b0-2413-4ea6-b219-1a4218fd7d3b")//flavour id
				.image("356ff1ed-5960-4ac2-96a1-0c0198e6a999")// -image id
				.keypairName("id_rsa")//key pair name
				.addSecurityGroup("944f413d-0915-4f93-b623-6e8d9a8cf45b")	//Security group ID (allow SSH)
				.userData(script)
				.build();//build the VM with above configuration
			
		
		Server booting=os.compute().servers().boot( server);
		String ipAddress=booting.getAccessIPv4();
		String id=booting.getId();
		return new String[] {ipAddress,id};
	}
	public String getIP(String serverid)
	{
		String ip=os.compute().servers().get(serverid).getAccessIPv4();
		while(ip==null||ip.length()==0) {
			try {
				Thread.sleep(1000);
				//System.out.println("Waiting");
			}
		catch(InterruptedException e) {e.printStackTrace();}
			ip=os.compute().servers().get(serverid).getAccessIPv4();
		}
		return ip;
	}
	public void getflavors()
	{
		List<? extends Flavor> flavors = os.compute().flavors().list();
		for(Flavor f: flavors)
		{
			System.out.println(f.getId().toString()+" "+f.getName());
		}
		
	}
	
//Delete a Server
	public void deleteServer( String serverid) {
		os.compute().servers().delete( serverid);//delete the VM orserver
	}
	
	/*public static void main( String [] args)
	{
		CloudTutorial openstack =new CloudTutorial();//Build the  -openstack client and authenticate
         openstack.getflavors();
				String[] serverDets = openstack.createServer();//Creating a new VM
		String serverIP=serverDets[0];
		String serverid=serverDets[1];
		String test2=openstack.getIP(serverid);
		System.out.println("IP "+serverIP+" "+test2);
		System.out.println(" Successfully Created Virtual Machine(VM) with server id"+ serverid +" and temp folder inside VM Please log in to nectar cloud to verify ");
		try {
			Thread.sleep(300000);//wait for 5 minutes to create VM
		} catch( InterruptedException e) {
			e.printStackTrace();
		}
		openstack.deleteServer(serverid);//Delete the created server
		System.out.println(" Successfully deleted Virtual Machine(VM) of server id"+ serverid);
	}*/
}