package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "C##MKJ"; // 테이블이 생성된 계정 (C##AISOFT에 다시 만드셨다면 C##AISOFT로 변경)
    private static final String PASSWORD = "여기에_비밀번호_입력"; // 실제 Oracle 비밀번호로 수정!

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}