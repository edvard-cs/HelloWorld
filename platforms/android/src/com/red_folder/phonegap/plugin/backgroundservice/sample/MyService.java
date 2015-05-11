package com.red_folder.phonegap.plugin.backgroundservice.sample;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyService extends BackgroundService{
	public static final String SERVER_HOST = "192.168.1.102";
	public static final int SERVER_PORT = 5222;
	public static final String SERVICE = "dell";
	private static final Context context = null;
	private static XMPPConnection xmppConnection;
	
	protected static void connect(String server, int port, String s) throws Exception {
        xmppConnection = new XMPPConnection(new ConnectionConfiguration(server, port,s));
        xmppConnection.connect();
    }
	public void disconnect(){
        if(xmppConnection != null){
            xmppConnection.disconnect();
            interrupt();
        }
    }
	private void interrupt() {
		// TODO Auto-generated method stub
		
	}
	
	public static void login(String username, String password) throws Exception{
        connect(SERVER_HOST, SERVER_PORT, SERVICE);
        xmppConnection.login(username, password);
    }
	 public static JSONObject listeningForMessages() throws JSONException{
		 JSONObject result = new JSONObject();
	        PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class));
	        PacketCollector collector = xmppConnection.createPacketCollector(filter);
	        while (true) {
	            Packet packet = collector.nextResult();
	            if (packet instanceof Message) {
	                Message message = (Message) packet;
	                if (message != null && message.getBody() != null)
	                    System.out.println("Received message from "
	                            + packet.getFrom() + " : "
	                            + (message != null ? message.getBody() : "NULL"));
	                result.put("Sender",packet.getFrom());
	                //result.put("Message",message.getBody());
	                Log.d("CoreXmppApi.Info","Got The Message From :"+ packet.getFrom());
	                Log.d("CoreXmppApi.Info","The Message Are :"+ message.getBody());
		            return result;
	            }
	            
	        }  
	 }
	 
	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();

		   // Following three lines simply produce a text string with Hello World and the date & time (UK format)
		  
		  
		  try {
		      login("admin", "admin");
		      Log.d("MyService","Berhasil Login");
		      JSONObject data = listeningForMessages();
		      Log.d("CoreXmppApi.Info","Logger For : "+data);
		      

		      
		      String sender = data.getString("Sender");
		      //String bodymessage = data.getString("Message");
		      Log.d("CoreXmppApi.Info","Logger For incoming: "+sender+" ");
			  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			  String now = df.format(new Date(System.currentTimeMillis())); 
			  String msg = "Hello World - its currently " + now + " Data ";
		        result.put("Message", "Sender  :"+sender+" Body : ");
				  Log.d("MyService","Logger");
		    } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		  
		  
		  // We output the message to the logcat

		  // We also provide the same message in our JSON Result

		   return result;   
	}

	@Override
	protected JSONObject getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setConfig(JSONObject config) {
		// TODO Auto-generated method stub
		
	}

}
