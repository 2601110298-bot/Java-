package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ExerciseAppGUI extends JFrame {
    private Member currentUser = null;
    private final MemberRepository memberRepo = new MemberRepository();
    private final ExerciseRepository exerciseRepo = new ExerciseRepository();
    private final PostRepository postRepo = new PostRepository();

    private JLabel lblStatus, lblRecommend;
    private DefaultTableModel exModel, postModel;
    private JTable exTable, postTable;

    public ExerciseAppGUI() {
        setTitle("운동 및 식단 관리 시스템 (JDBC Term Project)");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        lblStatus = new JLabel(" 현재 상태: 로그인이 필요합니다. [회원/인증] 탭에서 로그인 또는 가입하세요.");
        lblStatus.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblRecommend = new JLabel(" 맞춤형 추천 가이드: 로그인 후 신체정보에 따른 운동/식단이 표시됩니다.");
        lblRecommend.setForeground(new Color(0, 102, 204));
        topPanel.add(lblStatus);
        topPanel.add(lblRecommend);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. 회원 가입 / 로그인 / 정보수정", createMemberPanel());
        tabbedPane.addTab("2. 운동 및 식단 관리 (CRUD)", createExercisePanel());
        tabbedPane.addTab("3. 운동 커뮤니티 (CRUD)", createPostPanel());
        add(tabbedPane, BorderLayout.CENTER);
        refreshPostTable();
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel loginBox = new JPanel(new GridLayout(5, 2, 10, 10));
        loginBox.setBorder(BorderFactory.createTitledBorder("회원 로그인"));
        JTextField tfLoginId = new JTextField();
        JPasswordField tfLoginPw = new JPasswordField();
        JButton btnLogin = new JButton("로그인");

        loginBox.add(new JLabel("회원아이디:")); loginBox.add(tfLoginId);
        loginBox.add(new JLabel("비밀번호:")); loginBox.add(tfLoginPw);
        loginBox.add(new JLabel("")); loginBox.add(btnLogin);

        JPanel regBox = new JPanel(new GridLayout(6, 2, 10, 10));
        regBox.setBorder(BorderFactory.createTitledBorder("회원가입 / 내 정보 수정"));
        JTextField tfId = new JTextField();
        JTextField tfPw = new JTextField();
        JTextField tfAge = new JTextField();
        JTextField tfBody = new JTextField();
        JButton btnJoin = new JButton("신규 회원가입");
        JButton btnUpdate = new JButton("내 정보 수정 (로그인 상태)");

        regBox.add(new JLabel("회원아이디 (필수):")); regBox.add(tfId);
        regBox.add(new JLabel("비밀번호 (필수):")); regBox.add(tfPw);
        regBox.add(new JLabel("나이 (숫자):")); regBox.add(tfAge);
        regBox.add(new JLabel("신체정보 (예: 175cm/70kg/감량):")); regBox.add(tfBody);
        regBox.add(btnJoin); regBox.add(btnUpdate);

        btnLogin.addActionListener(e -> {
            Member m = memberRepo.login(tfLoginId.getText(), new String(tfLoginPw.getPassword()));
            if (m != null) {
                currentUser = m;
                lblStatus.setText(" 로그인 성공: " + m.getId() + "님 (나이: " + m.getAge() + "세, 신체정보: " + m.getBody_info() + ")");
                lblRecommend.setText(" " + exerciseRepo.recommendWorkout(m.getBody_info()));
                tfId.setText(m.getId());
                tfPw.setText(m.getPassword());
                tfAge.setText(String.valueOf(m.getAge()));
                tfBody.setText(m.getBody_info());
                refreshExerciseTable();
                refreshPostTable();
                JOptionPane.showMessageDialog(this, "환영합니다, " + m.getId() + "님!");
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        });

        btnJoin.addActionListener(e -> {
            try {
                Member m = new Member(tfId.getText(), tfPw.getText(), Integer.parseInt(tfAge.getText()), tfBody.getText());
                if (memberRepo.insertMember(m)) {
                    JOptionPane.showMessageDialog(this, "회원가입 완료! 추천 운동: " + exerciseRepo.recommendWorkout(m.getBody_info()));
                } else {
                    JOptionPane.showMessageDialog(this, "가입 실패 (중복 아이디 등 확인)");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인하세요 (나이는 숫자 입력).");
            }
        });

        btnUpdate.addActionListener(e -> {
            if (currentUser == null) { JOptionPane.showMessageDialog(this, "먼저 로그인하세요."); return; }
            try {
                currentUser.setPassword(tfPw.getText());
                currentUser.setAge(Integer.parseInt(tfAge.getText()));
                currentUser.setBody_info(tfBody.getText());
                if (memberRepo.updateMember(currentUser)) {
                    lblStatus.setText(" 로그인 중: " + currentUser.getId() + "님 (나이: " + currentUser.getAge() + "세, 신체정보: " + currentUser.getBody_info() + ")");
                    lblRecommend.setText(" " + exerciseRepo.recommendWorkout(currentUser.getBody_info()));
                    JOptionPane.showMessageDialog(this, "회원 정보가 수정되었습니다.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "수정 입력값을 확인하세요.");
            }
        });

        panel.add(loginBox);
        panel.add(regBox);
        return panel;
    }

    private JPanel createExercisePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        exModel = new DefaultTableModel(new String[]{"기록번호", "회원ID", "신체정보", "운동정보", "운동일자", "식단"}, 0);
        exTable = new JTable(exModel);
        panel.add(new JScrollPane(exTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        JTextField tfDate = new JTextField("2026-11-26");
        JTextField tfWorkout = new JTextField();
        JTextField tfDiet = new JTextField();
        JButton btnAdd = new JButton("기록 등록");
        JButton btnUpdate = new JButton("선택 수정");
        JButton btnDelete = new JButton("선택 삭제");
        JButton btnRefresh = new JButton("새로고침");

        form.add(new JLabel("운동일자(YYYY-MM-DD):")); form.add(tfDate);
        form.add(new JLabel("운동정보:")); form.add(tfWorkout);
        form.add(new JLabel("식단내역:")); form.add(tfDiet);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        exTable.getSelectionModel().addListSelectionListener(e -> {
            int row = exTable.getSelectedRow();
            if (row != -1) {
                tfWorkout.setText(exModel.getValueAt(row, 3).toString());
                tfDate.setText(exModel.getValueAt(row, 4).toString());
                tfDiet.setText(exModel.getValueAt(row, 5) != null ? exModel.getValueAt(row, 5).toString() : "");
            }
        });

        btnAdd.addActionListener(e -> {
            if (currentUser == null) { JOptionPane.showMessageDialog(this, "로그인 필요"); return; }
            try {
                Exercise ex = new Exercise();
                ex.setUser_id(currentUser.getId());
                ex.setBody_info(currentUser.getBody_info());
                ex.setWorkout_info(tfWorkout.getText());
                ex.setExercise_date(Date.valueOf(tfDate.getText()));
                ex.setDiet(tfDiet.getText());
                if (exerciseRepo.insertExercise(ex)) {
                    refreshExerciseTable();
                    JOptionPane.showMessageDialog(this, "운동 기록 저장 완료!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "날짜 형식(YYYY-MM-DD)을 확인하세요.");
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = exTable.getSelectedRow();
            if (currentUser == null || row == -1) return;
            Exercise ex = new Exercise();
            ex.setExercise_id((int) exModel.getValueAt(row, 0));
            ex.setUser_id(currentUser.getId());
            ex.setWorkout_info(tfWorkout.getText());
            ex.setExercise_date(Date.valueOf(tfDate.getText()));
            ex.setDiet(tfDiet.getText());
            if (exerciseRepo.updateExercise(ex)) {
                refreshExerciseTable();
                JOptionPane.showMessageDialog(this, "수정 완료!");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = exTable.getSelectedRow();
            if (currentUser == null || row == -1) return;
            int id = (int) exModel.getValueAt(row, 0);
            if (exerciseRepo.deleteExercise(id, currentUser.getId())) {
                refreshExerciseTable();
                JOptionPane.showMessageDialog(this, "삭제 완료!");
            }
        });

        btnRefresh.addActionListener(e -> refreshExerciseTable());
        return panel;
    }

    private JPanel createPostPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        postModel = new DefaultTableModel(new String[]{"글번호", "작성자ID", "글제목", "글내용", "작성일자"}, 0);
        postTable = new JTable(postModel);
        panel.add(new JScrollPane(postTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        JTextField tfTitle = new JTextField();
        JTextField tfContent = new JTextField();
        form.add(new JLabel("글제목:")); form.add(tfTitle);
        form.add(new JLabel("글내용:")); form.add(tfContent);

        JPanel btnPanel = new JPanel();
        JButton btnWrite = new JButton("게시글 등록");
        JButton btnEdit = new JButton("본인 글 수정");
        JButton btnDel = new JButton("본인 글 삭제");
        JButton btnList = new JButton("전체 조회");
        btnPanel.add(btnWrite); btnPanel.add(btnEdit); btnPanel.add(btnDel); btnPanel.add(btnList);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        postTable.getSelectionModel().addListSelectionListener(e -> {
            int row = postTable.getSelectedRow();
            if (row != -1) {
                tfTitle.setText(postModel.getValueAt(row, 2).toString());
                tfContent.setText(postModel.getValueAt(row, 3).toString());
            }
        });

        btnWrite.addActionListener(e -> {
            if (currentUser == null) { JOptionPane.showMessageDialog(this, "로그인 필요"); return; }
            Post p = new Post();
            p.setUser_id(currentUser.getId());
            p.setTitle(tfTitle.getText());
            p.setContent(tfContent.getText());
            if (postRepo.insertPost(p)) {
                refreshPostTable();
                JOptionPane.showMessageDialog(this, "게시글 등록 완료!");
            }
        });

        btnEdit.addActionListener(e -> {
            int row = postTable.getSelectedRow();
            if (currentUser == null || row == -1) return;
            Post p = new Post();
            p.setPost_id((int) postModel.getValueAt(row, 0));
            p.setUser_id(currentUser.getId());
            p.setTitle(tfTitle.getText());
            p.setContent(tfContent.getText());
            if (postRepo.updatePost(p)) {
                refreshPostTable();
                JOptionPane.showMessageDialog(this, "수정 완료!");
            } else {
                JOptionPane.showMessageDialog(this, "본인이 작성한 글만 수정할 수 있습니다.");
            }
        });

        btnDel.addActionListener(e -> {
            int row = postTable.getSelectedRow();
            if (currentUser == null || row == -1) return;
            int postId = (int) postModel.getValueAt(row, 0);
            if (postRepo.deletePost(postId, currentUser.getId())) {
                refreshPostTable();
                JOptionPane.showMessageDialog(this, "삭제 완료!");
            } else {
                JOptionPane.showMessageDialog(this, "본인이 작성한 글만 삭제할 수 있습니다.");
            }
        });

        btnList.addActionListener(e -> refreshPostTable());
        return panel;
    }

    private void refreshExerciseTable() {
        if (currentUser == null) return;
        exModel.setRowCount(0);
        List<Exercise> list = exerciseRepo.getExercisesByUser(currentUser.getId());
        for (Exercise ex : list) {
            exModel.addRow(new Object[]{ex.getExercise_id(), ex.getUser_id(), ex.getBody_info(), ex.getWorkout_info(), ex.getExercise_date(), ex.getDiet()});
        }
    }

    private void refreshPostTable() {
        postModel.setRowCount(0);
        List<Post> list = postRepo.getAllPosts();
        for (Post p : list) {
            postModel.addRow(new Object[]{p.getPost_id(), p.getUser_id(), p.getTitle(), p.getContent(), p.getCreated_at()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExerciseAppGUI().setVisible(true));
    }
}