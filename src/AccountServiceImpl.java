import java.sql.*;
import java.util.Locale;

/**
 * Created by 21 on 04.07.2015.
 */
public class AccountServiceImpl implements AccountService {
    private Connection conn;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    static Long startTime;
    static int requestCount;

    private String GET = "select amount from goods where id = ? ;";
    private String UPDATE = "update goods set amount = amount + ? where id = ?;";
    private String GET_ID = "select id from goods where id = ?";
    private String INSERT = "insert into goods values (?,?);";

    static {
        startTime = System.nanoTime();
        requestCount = 0;
    }

    public AccountServiceImpl(Connection conn){
        this.conn = conn;


    }
    /**
     * Retrieves current balance or zero if addAmount() method was not called before for specified id
     *
     * @param id balance identifier
     */
    @Override
    public Long getAmount(Integer id) {
        try {
            preparedStatement = conn.prepareStatement(GET);
            preparedStatement.setInt(1 , id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        requestCount++;
        return null;
    }

    /**
     * Increases balance or set if addAmount() method was called first time
     *
     * @param id    balance identifier
     * @param value positive or negative value, which must be added to current balance
     */
    @Override
    public synchronized void addAmount(Integer id, Long value) {
        try {
            if(isIdConsists(id)) {
                updateAmount(id, value);
            }else {
                insertAmount(id, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getErrorCode();
        }
        requestCount++;
    }

    private void insertAmount(int id, Long value) throws SQLException{
        preparedStatement = conn.prepareStatement(INSERT);
        preparedStatement.setInt(1 , id);
        preparedStatement.setLong(2, value);
        preparedStatement.executeUpdate();
    }
    private void updateAmount (int id , Long value) throws  SQLException{
        preparedStatement = conn.prepareStatement(UPDATE);
        preparedStatement.setInt(2 , id);
        preparedStatement.setLong(1, value);
        preparedStatement.executeUpdate();
    }
    private boolean isIdConsists (int id ){
        boolean result = false;
        try {
            preparedStatement = conn.prepareStatement(GET_ID);
            preparedStatement.setInt(1 , id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                result = true;
            }else{
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String requestStatistic(){
        String result = "";
        result = ""+((System.nanoTime()-startTime))+" requests for 1 second ";
        return result;
    }
    public static void clearStat(){
        startTime = System.nanoTime();
        requestCount = 0;
    }


}
