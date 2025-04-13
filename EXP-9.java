// Easy Level – Fetch All Employees
// FetchEmployees.java

import java.sql.*;

public class FetchEmployees {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "password";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");

            while (rs.next()) {
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.println(id + " | " + name + " | " + salary);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// OUTPUT
101 | John Doe | 50000.0  
102 | Alice    | 60000.0  
103 | Bob      | 55000.0  

// Medium Level – CRUD for Product Table
//ProductCRUD.java

  import java.sql.*;
import java.util.*;

public class ProductCRUD {
    static Scanner sc = new Scanner(System.in);
    static Connection conn;

    public static void main(String[] args) throws Exception {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

        while (true) {
            System.out.println("\n1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product\n5. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: add(); break;
                case 2: view(); break;
                case 3: update(); break;
                case 4: delete(); break;
                case 5: conn.close(); return;
            }
        }
    }

    static void add() throws SQLException {
        System.out.print("Enter ProductID, Name, Price, Quantity: ");
        int id = sc.nextInt();
        String name = sc.next();
        double price = sc.nextDouble();
        int qty = sc.nextInt();

        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?)");
        ps.setInt(1, id); ps.setString(2, name); ps.setDouble(3, price); ps.setInt(4, qty);
        ps.executeUpdate();
        conn.commit();
        System.out.println("Product added.");
    }

    static void view() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Product");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getDouble(3) + " | " + rs.getInt(4));
        }
    }

    static void update() throws SQLException {
        System.out.print("Enter ProductID to update price: ");
        int id = sc.nextInt();
        System.out.print("Enter new Price: ");
        double price = sc.nextDouble();

        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("UPDATE Product SET Price = ? WHERE ProductID = ?");
        ps.setDouble(1, price); ps.setInt(2, id);
        ps.executeUpdate();
        conn.commit();
        System.out.println("Product updated.");
    }

    static void delete() throws SQLException {
        System.out.print("Enter ProductID to delete: ");
        int id = sc.nextInt();

        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Product WHERE ProductID = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        conn.commit();
        System.out.println("Product deleted.");
    }
}

// OUPUT
1. Add Product
Enter ProductID, Name, Price, Quantity: 201 Phone 799.99 50
Product added.

2. View Products
201 | Phone | 799.99 | 50

3. Update Product
Enter ProductID to update price: 201
Enter new Price: 749.99
Product updated.

4. Delete Product
Enter ProductID to delete: 201
Product deleted.

//Hard Level – Student MVC App
//Student.java (Model)

public class Student {
    private int studentID;
    private String name;
    private String department;
    private int marks;

    public Student(int studentID, String name, String department, int marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getMarks() { return marks; }
}

// StudentController.java (Controller)
import java.sql.*;
import java.util.*;

public class StudentController {
    Connection conn;

    public StudentController() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");
    }

    public void addStudent(Student s) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?)");
        ps.setInt(1, s.getStudentID());
        ps.setString(2, s.getName());
        ps.setString(3, s.getDepartment());
        ps.setInt(4, s.getMarks());
        ps.executeUpdate();
    }

    public void viewStudents() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Student");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " +
                               rs.getString(3) + " | " + rs.getInt(4));
        }
    }

    public void updateMarks(int id, int marks) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE Student SET Marks = ? WHERE StudentID = ?");
        ps.setInt(1, marks);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    public void deleteStudent(int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Student WHERE StudentID = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public void close() throws SQLException {
        conn.close();
    }
}

//StudentView.java (View)
import java.util.*;

public class StudentView {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        StudentController controller = new StudentController();

        while (true) {
            System.out.println("\n1. Add\n2. View\n3. Update Marks\n4. Delete\n5. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID, Name, Dept, Marks: ");
                    Student s = new Student(sc.nextInt(), sc.next(), sc.next(), sc.nextInt());
                    controller.addStudent(s);
                    System.out.println("Added.");
                    break;

                case 2:
                    controller.viewStudents();
                    break;

                case 3:
                    System.out.print("Enter ID and new marks: ");
                    controller.updateMarks(sc.nextInt(), sc.nextInt());
                    System.out.println("Updated.");
                    break;

                case 4:
                    System.out.print("Enter ID to delete: ");
                    controller.deleteStudent(sc.nextInt());
                    System.out.println("Deleted.");
                    break;

                case 5:
                    controller.close();
                    return;
            }
        }
    }
}

// OUTPUT
1. Add
Enter ID, Name, Dept, Marks: 101 Alice CSE 88
Added.

2. View
101 | Alice | CSE | 88

3. Update Marks
Enter ID and new marks: 101 90
Updated.

4. Delete
Enter ID to delete: 101
Deleted.
