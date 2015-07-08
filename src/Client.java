
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
        int rCount=0;
        int wCount= 0;
        int initGetId= 0;
        int finalGetId= 0;
        int initAddId = 0;
        int finalAddId= 0;
        String fArg = (args.length < 1) ? null : args[0];
        String sArg = (args.length < 2) ? null : args[1];
        String idGetList = (args.length < 3) ? null : args[2];
        String idAddList = (args.length < 4) ? null : args[2];
        // Проверка введенных данных на валидность
        try {
            if (!(checkCounts(fArg) && checkCounts(sArg) && checkIdList(idGetList) && checkIdList(idAddList))) {
                System.out.println("incorrect parammetry");
                return;
            }
        }catch (NullPointerException e ){
            System.out.println("Enter all parameters");
            e.printStackTrace();
        }
        // Получение начальных и конечных значений для диапазонов id
        try {
            if (idGetList != null) {
                initGetId = Integer.parseInt(idGetList.substring(0, idGetList.indexOf("-")));
                finalGetId = Integer.parseInt(idGetList.substring(idGetList.indexOf("-") + 1, idGetList.length()));
            }
            if (idAddList != null) {
                initAddId = Integer.parseInt(idGetList.substring(0, idGetList.indexOf("-")));
                finalAddId = Integer.parseInt(idGetList.substring(idGetList.indexOf("-") + 1, idGetList.length()));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //Получение кол-ва потоков
        try{
            rCount = (fArg == null)? 0: Integer.parseInt(fArg);
            wCount = (sArg == null)? 0: Integer.parseInt(sArg);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if (finalGetId - initGetId <= rCount ){
            rCount = finalGetId - initGetId ;
        }
        if (finalAddId - initAddId <= wCount ){
            wCount = finalGetId - initGetId ;
        }
        System.out.println("rCount = "+ rCount + "wCount = " + wCount + "initGetId= "+initGetId + "initAddId"+initAddId);
        try {
            Registry registry = LocateRegistry.getRegistry(HOST);
            AccountService stub = (AccountService) registry.lookup("ServiceObj");
            ExecutorService executorService = Executors.newFixedThreadPool(rCount+wCount);

            //Запуск потоков
            for (int i=0; i < wCount; i++)
                executorService.execute(new RemoteWriter(stub, initAddId++));
            for (int i=0;i < rCount; i++)
                executorService.execute(new RemoteReader(stub,initGetId++));
            executorService.shutdown();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static boolean checkCounts (String count){
        Pattern pattern = Pattern.compile("^\\d{1,5}$");
        Matcher matcher = pattern.matcher(count);
        return matcher.matches();

    }
    private static boolean checkIdList(String idList){
        Pattern pattern = Pattern.compile("^\\d{1,5}-\\d{1,5}$");
        Matcher matcher = pattern.matcher(idList);
        return matcher.matches();
    }
}
