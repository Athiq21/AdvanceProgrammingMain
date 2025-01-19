package jic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public DBConnection() {
        super();
    }

    public static String sIP = "jdbc:mysql://localhost:3306/vehicle";
    public static String loginName = "root";
    public static String loginPwd  = "athiqrashiq1";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    public static void main(String args[]){
        getConnection();
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(sIP, loginName, loginPwd);
            System.out.println("Connection established successfully!");
        } catch (SQLException e) {
            System.out.println("Error while establishing connection: " + e.getMessage());
        }
        return conn;
    }
}
