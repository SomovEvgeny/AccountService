import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 21 on 06.07.2015.
 */
public class RemoteWriter implements Runnable {
    Scanner scanner;
    AccountService serviceObj;
    String command;
    int id;
    Long amount;

    public RemoteWriter(AccountService a){
        serviceObj = a;
        scanner = new Scanner(System.in);
    }

    private  boolean isAddCommand (String str){
        Pattern pattern = Pattern.compile("^[\\d]{1,5}\\s[\\d]{1,5};?$");
        Matcher matcher = pattern.matcher(str);
        return  matcher.matches();
    }

    @Override
    public void run() {
        System.out.println("Writer "+ Thread.currentThread().getId() + " waits for commands :");
        while (scanner.hasNextLine()){
            command = scanner.nextLine();
            if(isAddCommand(command)){
                id = Integer.parseInt(command.substring(0, command.indexOf(" ")));
                amount = Long.parseLong(command.substring(command.indexOf(" ")+1,command.length()));
                try {
                    serviceObj.addAmount(id,amount);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else {
                System.err.println("Unknown command");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
