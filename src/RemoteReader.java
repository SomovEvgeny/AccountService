import java.rmi.RemoteException;


/**
 * Created by 21 on 06.07.2015.
 */
public class RemoteReader implements  Runnable{
    AccountService serviceObj;
    int id;
    public RemoteReader(AccountService a , int id){
        this.id = id;
        serviceObj = a;
    }
    @Override
    public void run(){
            try {
                System.out.println("id = "+ id + " amount = "+serviceObj.getAmount(id));
                Thread.sleep(1000);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



}
