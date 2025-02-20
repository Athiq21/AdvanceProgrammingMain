//package jic;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DBConnection {
//    public DBConnection() {
//        super();
//    }
//
//    public static String AthiqID = "jdbc:mysql://localhost:3306/vehicle";
//    public static String loginName = "root";
//    public static String loginPwd  = "athiqrashiq1";
//
//    public static void main(String [] args){
//        getConnection();
//    }
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//        }
//    }
//
//    public static Connection getConnection() {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(AthiqID, loginName, loginPwd);
//            System.out.println("WORK.");
//        } catch (SQLException e) {
//        }
//        return conn;
//    }
//}
//
//package jic;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class DBConnection {
//
//    private static DBConnection dbIsntance;
//    private static Connection c;
//    private static Statement stmt;
//    public final static String AthiqID = "jdbc:mysql://localhost:3306/vehicle";
//    public final static String loginName = "root";
//    public final static String loginPwd = "athiqrashiq1";
//
//    public DBConnection() {
//        super();
//    }
//
//    private static void Database() {
//
//    }
//        static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//        }
//    }
//    public static DBConnection getInstance() {
//        if (dbIsntance == null) {
//            dbIsntance = new DBConnection();
//        }
//        return dbIsntance;
//    }
//
//    public Connection getConnection() {
//
//        if (c == null) {
//            try {
//                c = DriverManager.getConnection(AthiqID, loginName, loginPwd);
//                System.out.println("DB WORK");
//            } catch (SQLException ex) {
//                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        return c;
//    }
//}


package jic;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
//import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {
//
//    private static final Dotenv env = Dotenv.configure().directory("/").load();

    private static DBConnection dbIsntance;
//    private static Connection c;
    private static Statement stmt;
    public final static String ATHIQID = "jdbc:mysql://localhost:3306/vehicle";
    public final static String LOGINNAME = "root";
    public final static String LOGINPW = "athiqrashiq1";

    public DBConnection() {
        super();
    }
    private static void Database() {

    }
        static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
    }
    public static synchronized DBConnection getInstance() {
        if (dbIsntance == null) {
            dbIsntance = new DBConnection();
        }
        return dbIsntance;
    }

    public Connection getConnection() {
        Connection c = null;
        if (c == null) {
            try {
                c = DriverManager.getConnection(ATHIQID, LOGINNAME, LOGINPW);
                System.out.println("DB WORK");
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "NO WORK", ex);
            }
        }

        return c;
    }

    public static void env() {
//        System.out.println("ID: " + env.get("DB_ID"));
//        System.out.println("loginName: " + env.get("LOGIN_NAME"));
//        System.out.println("loginPwd: " + env.get("LOGIN_PW"));
//        System.out.println("SECRET_KEY: " + env.get("SECRET_KEY"));
    }

    public static void main(String [] args){
  env();
    }
}
