import java.rmi.RemoteException;

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
                Thread.sleep(1000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
