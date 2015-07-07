import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 21 on 04.07.2015.
 */
public class Client {
    private static final int HOST = 1099;

    public static void main(String[] args) {
        int rCount=0, wCount= 0, initId=0, finalId=0;
        String fArg = (args.length < 1) ? null : args[0];
        String sArg = (args.length < 2) ? null : args[1];
        String idList = (args.length < 3) ? null : args[2];

        if (!(checkCounts(fArg) && checkCounts(sArg) && checkIdList(idList))) {
            System.out.println("incorrect parammetry");
            return;
        }
        if (idList != null){
            initId = Integer.parseInt(idList.substring(0, idList.indexOf("-")));
            finalId = Integer.parseInt(idList.substring(idList.indexOf("-") + 1, idList.length()));
        }
        try{
            rCount = Integer.parseInt(fArg);
            wCount = Integer.parseInt(sArg);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        System.out.println(" " + rCount + " " + wCount + " " + initId + " " + finalId);

        if (finalId - initId <= rCount ){
            rCount = finalId - initId ;
            wCount =0;
        }else if(finalId - initId <= rCount+ wCount ){
            wCount = finalId -initId -rCount;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(HOST);
            AccountService stub = (AccountService) registry.lookup("ServiceObj");
            ExecutorService executorService = Executors.newFixedThreadPool(rCount+wCount);
            for (int i=0;i < rCount; i++)
                executorService.execute(new RemoteReader(stub));
            for (int i=0; i < wCount; i++)
                executorService.execute(new RemoteWriter(stub));
            executorService.shutdown();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static boolean checkCounts (String count){
        boolean result = false ;
        Pattern pattern = Pattern.compile("^\\d{1,5}$");
        Matcher matcher = pattern.matcher(count);
        result = matcher.matches();
        return  result;

    }
    private static boolean checkIdList(String idList){
        boolean result = false ;
        Pattern pattern = Pattern.compile("^\\d{1,5}-\\d{1,5}$");
        Matcher matcher = pattern.matcher(idList);
        result = matcher.matches();
        return  result;
    }
}
