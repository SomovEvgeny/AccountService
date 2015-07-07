import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by 21 on 04.07.2015.
 */
public class Server {
    public static void main(String[] args){
        try {
            AccountServiceImpl obj = new AccountServiceImpl(getConnection());
            AccountService stub = (AccountService) UnicastRemoteObject.exportObject(obj,0);
            System.out.println("--Stub successful created--");
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("ServiceObj", stub);
            System.out.println("-- Server is started --");
        }catch(SQLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException, IOException{
        Properties prop = new Properties();
        FileInputStream in = new FileInputStream("database.properties.txt");
        prop.load(in);
        String url = prop.getProperty("jdbc.url");
        String user = prop.getProperty("jdbc.user");
        String password = prop.getProperty("jdbc.password");
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
