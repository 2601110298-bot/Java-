package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {
    public String recommendWorkout(String bodyInfo) {
        if (bodyInfo.contains("감량") || bodyInfo.contains("다이어트")) {
            return "[추천 운동] 유산소 인터벌 40분 + 전신 서킷 | [추천 식단] 닭가슴살 샐러드, 고구마 150g";
        } else if (bodyInfo.contains("근력") || bodyInfo.contains("증량")) {
            return "[추천 운동] 3대 운동(스쿼트/벤치/데드) 5세트 | [추천 식단] 소고기/계란 흰자, 현미밥 200g";
        }
        return "[추천 운동] 코어 스트레칭 및 걷기 30분 | [추천 식단] 일반식(나트륨 줄이기) + 단백질 보충";
    }

    public boolean insertExercise(Exercise ex) {
        String sql = "INSERT INTO EXERCISE (exercise_id, user_id, body_info, workout_info, exercise_date, diet) " +
                "VALUES (SEQ_EXERCISE.NEXTVAL, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ex.getUser_id());
            pstmt.setString(2, ex.getBody_info());
            pstmt.setString(3, ex.getWorkout_info());
            pstmt.setDate(4, ex.getExercise_date());
            pstmt.setString(5, ex.getDiet());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Exercise> getExercisesByUser(String userId) {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT * FROM EXERCISE WHERE user_id = ? ORDER BY exercise_date DESC, exercise_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Exercise ex = new Exercise();
                ex.setExercise_id(rs.getInt("exercise_id"));
                ex.setUser_id(rs.getString("user_id"));
                ex.setBody_info(rs.getString("body_info"));
                ex.setWorkout_info(rs.getString("workout_info"));
                ex.setExercise_date(rs.getDate("exercise_date"));
                ex.setDiet(rs.getString("diet"));
                list.add(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateExercise(Exercise ex) {
        String sql = "UPDATE EXERCISE SET workout_info = ?, diet = ?, exercise_date = ? WHERE exercise_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ex.getWorkout_info());
            pstmt.setString(2, ex.getDiet());
            pstmt.setDate(3, ex.getExercise_date());
            pstmt.setInt(4, ex.getExercise_id());
            pstmt.setString(5, ex.getUser_id());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExercise(int exerciseId, String userId) {
        String sql = "DELETE FROM EXERCISE WHERE exercise_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exerciseId);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}