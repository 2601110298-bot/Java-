package test;

public class Member {
    private String id;
    private String password;
    private int age;
    private String body_info;

    public Member() {}
    public Member(String id, String password, int age, String body_info) {
        this.id = id;
        this.password = password;
        this.age = age;
        this.body_info = body_info;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBody_info() { return body_info; }
    public void setBody_info(String body_info) { this.body_info = body_info; }
}