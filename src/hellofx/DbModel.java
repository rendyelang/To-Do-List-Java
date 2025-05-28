package hellofx;

import java.sql.*;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class DbModel {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/todolist_app";
            String user = "root";
            String password = "root";

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<TodoItem> readData(Connection conn) {
        ObservableList<TodoItem> items = FXCollections.observableArrayList();
        String query = "SELECT * FROM tasks ORDER BY id DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // System.out.println(rs.getInt("id"));
                int id = rs.getInt("id");
                // System.out.println(id);
                String task = rs.getString("title");
                String status = rs.getString("status");
                items.add(new TodoItem(id, task, status));
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return items;
    }
    
    public static void insertData(Connection conn, String title, String status) {
        String query = "INSERT INTO tasks (title, status) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(Connection conn, int id) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateData(Connection conn, int id, String title, String status) {
        String query = "UPDATE tasks SET title = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, status);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
