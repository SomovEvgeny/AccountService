import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 21 on 06.07.2015.
 */
public class RemoteReader implements  Runnable{
    Scanner scanner;
    AccountService serviceObj;
    String command;
    int id;
    Long amount;
    public RemoteReader(AccountService a){
        serviceObj = a;
        scanner = new Scanner(System.in);

    }
    @Override
    public void run() {
        System.out.println("Reader "+ Thread.currentThread().getId() + " waits for commands :");
        while (scanner.hasNextLine()){
            command=scanner.nextLine();
            if (isGetCommand(command)){
                id = Integer.parseInt(command);
                try {
                    System.out.println(serviceObj.getAmount(id));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else {
                System.err.println("Unknown command");
            }
            try {
                System.out.println("Sleep");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private  boolean isGetCommand (String str){
        Pattern pattern = Pattern.compile("^[\\d]{1,5};?$");
        Matcher matcher = pattern.matcher(str);
        return  matcher.matches();
    }

}
