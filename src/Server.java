
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 21 on 04.07.2015.
 */
public class Server {
    public static void main(String[] args){
        try {
            String clear = "^clear;?$";
            String show = "^show;?$";
            String exit = "^exit;?$";
            Scanner scanner = new Scanner(System.in);
            AccountServiceImpl obj = new AccountServiceImpl(getConnection());
            AccountService stub = (AccountService) UnicastRemoteObject.exportObject(obj,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("ServiceObj", stub);
            System.out.println("-- Server is started --");
            System.out.println("Type 'show' to display query statistics, \n " +
                    "or 'clear' to reset it, and 'exit' for exit from application");

            while(scanner.hasNextLine()){
                String command = scanner.nextLine();
                if (checkComLine(command, show)){
                    System.out.println(obj.queryGetStatistic());
                    System.out.println(obj.queryAddStatistic());
                    continue;
                }
                if (checkComLine(command, clear)){
                    obj.clearStat();
                    continue;
                }
                if (checkComLine(command,exit)){
                    return;
                }

            }
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
        return  DriverManager.getConnection(url, user, password);
    }
    public static boolean checkComLine (String str , String reg){
        Pattern pattern = Pattern.compile(reg);
        Matcher  matcher = pattern.matcher(str);
        return  matcher.matches();

    }
}
