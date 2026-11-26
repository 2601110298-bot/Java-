package test;

import java.sql.*;

public class MemberRepository {
    public boolean insertMember(Member m) {
        String sql = "INSERT INTO MEMBER (id, password, age, body_info) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getId());
            pstmt.setString(2, m.getPassword());
            pstmt.setInt(3, m.getAge());
            pstmt.setString(4, m.getBody_info());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Member login(String id, String password) {
        String sql = "SELECT * FROM MEMBER WHERE id = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getString("body_info")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMember(Member m) {
        String sql = "UPDATE MEMBER SET password = ?, age = ?, body_info = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getPassword());
            pstmt.setInt(2, m.getAge());
            pstmt.setString(3, m.getBody_info());
            pstmt.setString(4, m.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}