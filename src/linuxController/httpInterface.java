package linuxController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;





public class httpInterface {
	
	static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	static Map<String, String> UserData = new HashMap<String, String>();

	static boolean full = true;
	static boolean authorithy = false;
	static String currentUser = null;
	static ArrayList<String> Operation = new ArrayList<String>();
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		System.out.println("server started at ");
		String rootpath = System.getProperty("user.dir");
		
		FileReader s = null;
		String jsonR = null;
	
		if (Files.exists(Paths.get(rootpath+File.separator+"UserData.json"))) {
			try {
				s = new FileReader(rootpath+File.separator+"UserData.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			File file = new File("UserData.json");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Map<String, String> map = new HashMap<>();

			Gson gson = new Gson();
			String jsonW = gson.toJson(map);
			writer.println(jsonW);
			writer.close();
			try {
				s = new FileReader(rootpath+File.separator+"UserData.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		BufferedReader buffer = new  BufferedReader(s);
		try {
			jsonR = buffer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson js2 = new Gson();
		UserData = js2.fromJson(jsonR, Map.class);
		
		
		

		
		server.createContext("/Home", new Home());
		server.createContext("/Register", new Register());
		server.createContext("/LogIn", new LogIn());
		server.createContext("/SignOut", new SignOut());
		server.createContext("/AccountUpdate", new AccountUpdate());
		server.createContext("/Check", new Check());
		server.createContext("/GrabProcess", new GrabDataHandler());
		server.createContext("/GrabAllProcess", new GrabAllDataHandler());
		server.createContext("/CloseProcessByIndex", new CloseDataHandler());
		server.createContext("/CloseProcessByName", new CloseDataByNameHandler());
		server.createContext("/CustomizedInstruction", new CustomizedInstructionHandler());
		server.createContext("/SingleCustomizedInstruction", new SingleCustomizedInstructionHandler());
		server.createContext("/startShell", new startShell());
		server.createContext("/closeShell", new closeShell());
		server.createContext("/shellIns", new shellIns());
		server.createContext("/upload", new upload());
		server.createContext("/uploadWithDestinationFile", new uploadWithDestinationFile());
		server.createContext("/uploadAll", new uploadAll());
		server.createContext("/uploadAllWithDestinationFile", new uploadAllWithDestinationFile());
		server.createContext("/download", new download());
		server.createContext("/downloadWithDestinationFile", new downloadWithDestinationFile());
		server.createContext("/downloadAll", new downloadAll());
		server.createContext("/CheckCPUMemoryANDIO", new CheckCPUMemoryANDIOHandler());
		server.createContext("/History", new HistoryHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}
	
	static class Home implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);
		System.out.println("Home Handler is called ");
		String response = "Instruction: use \"http://localhost:8000/Home\" to check the instruction\r\n"
				+ "use \"http://localhost:8000/Register/?username=xxxxx&pswd=xxxxx\" to register new account\r\n"
				+ "use \"http://localhost:8000/LogIn/?username=xxxxx&pswd=xxxxx\" to login\r\n"
				+ "use \"http://localhost:8000/SignOut\" to sign out\r\n"
				+ "use \"http://localhost:8000/Check\" to check the information of saved servers\r\n"
				+ "use \"http://localhost:8000/GrabProcess/?ip=192.168.108.129:22&user=xxxxx&pswd=xxxxx\" to access server\r\n"
				+ "use \"http://localhost:8000/CloseProcess/?ip=192.168.108.129:22&Process1Index=1&Process2Index=3&Process3Index=4\" to close/delete process, the order of index must be ascending order,"
				+ " you can delete more than one server at one time\r\n"
				+ "ip refer to ip address, write port after \":\", user is the username of your server, pswd is the password of your server\r\n"
				+ "if you use the correct ip address, user, and pswd, then you can use ip address only to access your servers, for example:\"http://localhost:8000/GrabProcess/?ip=192.168.108.129:22\""
				+ "\nIf you can not use this shortcut, you can access full path multiple times to make sure the data was inserted into your database successfully";

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class Register implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);
		System.out.println("Register Handler is called ");
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		String response = null;
		if(!UserData.containsKey(params.get("username"))) {
			UserData.put(params.get("username"),params.get("pswd") );
			response = "Register successfully";
		}else {
			response = "Register fail, the username have been used";
		}

		
		
		File file = new File("UserData.json");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		String jsonW = gson.toJson(UserData);
		writer.println(jsonW);
		writer.close();
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class LogIn implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);		
		System.out.println("LogIn Handler is called ");
		String response = "";
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		if(authorithy==true) {
			response = "please sign out before you login";
		}else {
			if(UserData.get(params.get("username"))!=null&&UserData.get(params.get("username")).equals(params.get("pswd"))) {


				authorithy = true;
				currentUser = params.get("username");
				response = "login successfully";

			}else {
				currentUser = null;
				if(UserData.get(params.get("username"))==null) {
					response = "this username didn't register yet";
				}else {
					response = "incorrect password";
				}
				
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				return ;
			}
		}
		
		
		
		String rootpath = System.getProperty("user.dir");
		FileReader s2 = null;
		String jsonR2 = null;
		
		if (Files.exists(Paths.get(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json"))) {
			try {
				s2 = new FileReader(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			File file = new File(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
			file.createNewFile();
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Map<String, ArrayList<String>> map = new HashMap<>();

			Gson gson = new Gson();
			String jsonW = gson.toJson(map);
			writer.println(jsonW);
			writer.close();
			try {
				s2 = new FileReader(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		BufferedReader buffer2 = new  BufferedReader(s2);
		try {
			jsonR2 = buffer2.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson js3 = new Gson();
		map = js3.fromJson(jsonR2, Map.class);
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
		}
	}
	
	static class SignOut implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);	
		System.out.println("SignOut Handler is called ");
		String response = "";
		if(authorithy == false) {
			response ="please login before sign out";
		}else {
			authorithy = false;
			currentUser = null;
			map = null;
			response ="sign out successfully";
		}
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class AccountUpdate implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);	
		System.out.println("AccountUpdate Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before update";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		
		String rootpath = System.getProperty("user.dir");
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));

		if(map.get(params.get("ip"))==null) {
			response = "Wrong ip";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return ;
		}

		facade.connect(ip,port, params.get("user"), params.get("pswd"));


		
		
		
		
		facade.Check();			
		if(facade.isAuthenticated) {
			ArrayList<String> newAccount = new ArrayList<String>();
			newAccount.add(params.get("user"));
			newAccount.add(params.get("pswd"));
			map.put(params.get("ip"), newAccount);
			
			
			File file = new File(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			Gson gson = new Gson();
			String jsonW = gson.toJson(map);
			writer.println(jsonW);
			writer.close();
			response = "update successfully";
		}else {
			response = "wrong username and password";
		}

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
		
		
		}
	}
	
	static class Check implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);	
		System.out.println("Check Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use check process";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()) {
			String next = i.next();
			response += "ip: " + next + "   user: " + map.get(next).get(0) + "   pswd: " +  map.get(next).get(1) + "\n";
		}
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	
	static class GrabDataHandler implements HttpHandler {
			public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
			System.out.println("GrabData Handler is called ");
			String response = "";
			if(!authorithy) {
				response = "please login before use grab process";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				return;
			}
			
			
			String rootpath = System.getProperty("user.dir");
			Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
			int sep = params.get("ip").indexOf(':');
			String ip = params.get("ip").substring(0,sep);
			int port = Integer.parseInt(params.get("ip").substring(sep+1));
			if(params.get("user")==null) {
				full = false;
				if(map.get(params.get("ip"))==null) {
					response = "Current username: "+currentUser + "   " + "Current ip: " + ip + "   Current port: " + port + "\n"+"this ip didn't have username and password in database, "
							+ "please use full path to enter username and pswd before using short cut\n"
							+ "the format of full path is http://localhost:8000/GrabProcess/?ip=192.168.108.129:22&user=xxxxx&pswd=xxxxx"
							+ "";
					t.sendResponseHeaders(200, response.length());
					OutputStream os = t.getResponseBody();
					os.write(response.getBytes());
					os.close();
				}else {
					facade.connect(ip,port  ,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));

				}
			}
			else {
				full = true;
				facade.connect(ip,port, params.get("user"), params.get("pswd"));


			}
			
			
			response = "Current username: "+currentUser+"   ";
			response += facade.Check();			
			if(facade.isAuthenticated&&full&&(map.get(params.get("ip"))==null||(!map.get(params.get("ip")).get(0).equals(params.get("user"))))) {
				ArrayList<String> newAccount = new ArrayList<String>();
				newAccount.add(params.get("user"));
				newAccount.add(params.get("pswd"));
				map.put(params.get("ip"), newAccount);
				
				
				File file = new File(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				Gson gson = new Gson();
				String jsonW = gson.toJson(map);
				writer.println(jsonW);
				writer.close();
			}

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			}
	}
	
	static class GrabAllDataHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
			System.out.println("GraAllData Handler is called ");
			String response = "";
			if(!authorithy) {
				response = "please login before use GrabAllDataHandler";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				return;
			}
			

			
			
			String ip = null;
			int port = 0;
			response = "Current username: "+currentUser+"   \n";
			
			
			for(String i : map.keySet()) {
				ip = i.substring(0,i.indexOf(":"));
				port = Integer.parseInt(i.substring(i.indexOf(":")+1));
				facade.connect(ip,port  ,map.get(i).get(0),map.get(i).get(1));
				response += facade.CheckAll()+"\n";
			}
			
			

			
			


			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
	
	static class CloseDataHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("CloseData Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use CloseDataHandler";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		ArrayList<Integer> indexOfProcess = new ArrayList<Integer>();
		String rootpath = System.getProperty("user.dir");
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		System.out.println(ip+" "+port);
		if(params.get("user")==null) {
			if(map.get(params.get("ip"))==null) {
				response = "Current username: "+currentUser + "   " + "Current ip: " + ip + "   Current port: " + port+"this ip didn't have username and password in database, "
						+ "please use full path to enter username and pswd before using short cut\n"
						+ "the format of full path is http://localhost:8000/CloseProcess/?ip=192.168.108.129:22&user=xxxxx&pswd=xxxxx"
						+ "";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}else {
				facade.connect(ip,port  ,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));

			}
		}
		else {
			full = true;
			facade.connect(ip,port, params.get("user"), params.get("pswd"));


		}
				
		for(int j = 1; j <= params.size()-1;j++) {
			if(params.get("Process"+j+"Index")!=null) {
				indexOfProcess.add(Integer.parseInt(params.get("Process"+j+"Index")));
			}
		}
		
		response = "Current username: "+currentUser+"\n";
		response += facade.remove(indexOfProcess);
		if(facade.isAuthenticated&&full&&(map.get(params.get("ip"))==null||(!map.get(params.get("ip")).get(0).equals(params.get("user"))))) {
			ArrayList<String> newAccount = new ArrayList<String>();
			newAccount.add(params.get("user"));
			newAccount.add(params.get("pswd"));
			map.put(params.get("ip"), newAccount);
			
			
			File file = new File(rootpath+File.separator+"src"+File.separator+"database"+File.separator+currentUser+".json");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			Gson gson = new Gson();
			String jsonW = gson.toJson(map);
			writer.println(jsonW);
			writer.close();
		}
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
		

	}
	
	static class CloseDataByNameHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("CloseDataByName Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use CloseDataByNameHandler";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		ArrayList<String> name = new ArrayList<String>();

		String ip = null;
		int port = 0;
		response = "Current username: "+currentUser+"   \n";
		for(int j = 1; j <= params.size();j++) {
			if(params.get("ProcessName"+j)!=null) {
				name.add(params.get("ProcessName"+j));
			}
		}
		
		for(String i : map.keySet()) {
			ip = i.substring(0,i.indexOf(":"));
			port = Integer.parseInt(i.substring(i.indexOf(":")+1));
			facade.connect(ip,port,map.get(i).get(0),map.get(i).get(1));
			response += facade.removeByName(name)+"\n";
		}
				




		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
		

	}
	
	static class  CustomizedInstructionHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("CustomizedInstruction Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use CustomizedInstructionHandler";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		

		String ins = params.get("instruction");
		ins = ins.replace("_", " ");
		response = "Current username: "+currentUser+"   \n";

		for(String s: map.keySet()) {
			if(map.get(s).get(0).equals(params.get("user"))&&map.get(s).get(1).equals(params.get("pswd"))) {
				String ip = s.substring(0,s.indexOf(":"));
				int port = Integer.parseInt(s.substring(s.indexOf(":")+1));
				facade.connect(ip,port,map.get(s).get(0),map.get(s).get(1));
				response += "Current ip: " + ip + "   Current port: " + port + "\n";
				response += facade.instruction(ins)+"\n";
			}
		}
				




		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class  SingleCustomizedInstructionHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("SingleCustomizedInstruction Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use SingleCustomizedInstructionHandler";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		

		String ins = params.get("instruction");
		ins = ins.replace("_", " ");
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));


		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		response += "Current ip: " + ip + "   Current port: " + port + "\n";
		response += facade.instruction(ins)+"\n";

				




		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class  CheckCPUMemoryANDIOHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("CheckCPUMemoryANDIO Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use CheckCPUMemoryANDIOHandler";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		

		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));


		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		response += "Current ip: " + ip + "   Current port: " + port + "\n";
		response += "CPU and Memory:\n";
		response += facade.instruction("vmstat -n 1 1")+"\n";
		response += "Memory detail:\n" + facade.instruction("free -m") + "\n";
		response += "IO:\n" + facade.instruction("ifstat");
		

				




		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class startShell implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("startShell Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use startShell process";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.link();

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class closeShell implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("closeShell Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use closeShell process";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		response += facade.closeShell();
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class shellIns implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("shellIns Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use shellIns";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		

		String ins = params.get("instruction");
		ins = ins.replace("_", " ");
		response = "Current username: "+currentUser+"   \n";
		

		response += facade.shellIns(ins)+"\n";

				




		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class upload implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("upload Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use upload";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.upload(params.get("local"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class uploadWithDestinationFile implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("uploadWithDestinationFile Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use uploadWithDestinationFile";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.upload(params.get("local"), params.get("remote"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class uploadAll implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("uploadAll Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use uploadAll";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.uploadAll();
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class uploadAllWithDestinationFile implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("uploadAllWithDestinationFile Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use uploadAllWithDestinationFile";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.uploadAll(params.get("folderName"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class download implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("download Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use download";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.download(params.get("fileName"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class downloadAll implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("downloadAll Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use download";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.downloadAll(params.get("folderName"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class downloadWithDestinationFile implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
			Operation.add(requestUrl);	
		System.out.println("downloadWithDestinationFile Handler is called ");
		String response = "";
		if(!authorithy) {
			response = "please login before use downloadWithDestinationFile";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			return;
		}
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		response = "Current username: "+currentUser+"   \n";
		int sep = params.get("ip").indexOf(':');
		String ip = params.get("ip").substring(0,sep);
		int port = Integer.parseInt(params.get("ip").substring(sep+1));
		
		facade.connect(ip,port,map.get(params.get("ip")).get(0),map.get(params.get("ip")).get(1));
		
		response += facade.download(params.get("fileName"),params.get("folderName"));
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	
	static class HistoryHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
		String requestUrl ="http://localhost:8000"+ t.getRequestURI().toString();
		Operation.add(requestUrl);	
		System.out.println("History Handler is called ");
		String response = "";
		for(int i = 1; i<=Operation.size();i++) {
			response += i + ": " + Operation.get(i-1) + "\n";
		}
		
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		}
	}
	public static Map<String, String> queryToMap(String query) {
		if (query == null) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String[] entry = param.split("=");
			if (entry.length > 1) {
				result.put(entry[0], entry[1]);
			} else {
				result.put(entry[0], "");
			}
		}
		return result;
	}
	
	


}
