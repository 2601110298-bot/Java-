package test;

import java.sql.Date;

public class Exercise {
    private int exercise_id;
    private String user_id;
    private String body_info;
    private String workout_info;
    private Date exercise_date;
    private String diet;

    public int getExercise_id() { return exercise_id; }
    public void setExercise_id(int exercise_id) { this.exercise_id = exercise_id; }
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getBody_info() { return body_info; }
    public void setBody_info(String body_info) { this.body_info = body_info; }
    public String getWorkout_info() { return workout_info; }
    public void setWorkout_info(String workout_info) { this.workout_info = workout_info; }
    public Date getExercise_date() { return exercise_date; }
    public void setExercise_date(Date exercise_date) { this.exercise_date = exercise_date; }
    public String getDiet() { return diet; }
    public void setDiet(String diet) { this.diet = diet; }
}