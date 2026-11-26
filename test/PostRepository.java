package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    public boolean insertPost(Post post) {
        String sql = "INSERT INTO POST (post_id, user_id, title, content, created_at) " +
                "VALUES (SEQ_POST.NEXTVAL, ?, ?, ?, SYSDATE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getUser_id());
            pstmt.setString(2, post.getTitle());
            pstmt.setString(3, post.getContent());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM POST ORDER BY post_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Post p = new Post();
                p.setPost_id(rs.getInt("post_id"));
                p.setUser_id(rs.getString("user_id"));
                p.setTitle(rs.getString("title"));
                p.setContent(rs.getString("content"));
                p.setCreated_at(rs.getDate("created_at"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePost(Post post) {
        String sql = "UPDATE POST SET title = ?, content = ? WHERE post_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getPost_id());
            pstmt.setString(4, post.getUser_id());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePost(int postId, String userId) {
        String sql = "DELETE FROM POST WHERE post_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}