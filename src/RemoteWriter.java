import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 21 on 06.07.2015.
 */
public class RemoteWriter implements Runnable {
    AccountService serviceObj;
    int id;

    public RemoteWriter(AccountService a, int id){
        serviceObj = a;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            serviceObj.addAmount(id, (long) 1);
            Thread.sleep(100);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
