package SupervisedMetablocking;


import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Enumeration;


public class myql_connection {

	public Connection create_conection(String Database) {

		String url = null;
		String login = null;
		String senha = "1761791";
		Connection conn;

//		NetworkInterface ni;
//		String localAdress=null;
//		try {
//			ni = NetworkInterface.getByName("eth0");
//			Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
//			while(inetAddresses.hasMoreElements()) {
//				InetAddress ia = inetAddresses.nextElement();
//				if(!ia.isLinkLocalAddress()) {
//					System.out.println("IP: " + ia.getHostAddress());
//					localAdress=ia.getHostAddress();
//				}
//			}
//		} catch (SocketException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		//if(localAdress.equals("150.164.2.36")){
		url = "jdbc:mysql://localhost:3306/" + Database;
		login = "root";
		
//		}else{
//			url = "jdbc:mysql://localhost:3306/" + Database;
//			login = "root";
//		}



		

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("\nDriver carregado com sucesso!\n");
			try{
				conn = DriverManager.getConnection(url, login, senha);
				conn.setAutoCommit(false);
				return conn;
			}catch (final Exception ex)  {
				System.out.println(ex.getMessage());
				System.exit(0);
			}  
		}
		catch (final Exception ex)
		{
			System.out.println("\nDriver nao pode ser carregado!");
		}
		return null;

	}    
}
