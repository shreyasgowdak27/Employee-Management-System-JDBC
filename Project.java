import java.sql.*;
import java.util.Scanner;

public class Project {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/company_db?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASS = "password"; // Update this with your actual MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Load Driver and Establish Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            // 2. Initialize Table (Based on Project Requirements)
            Statement stmt = con.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE, " +
                    "department VARCHAR(50), " +
                    "salary DOUBLE)";
            stmt.executeUpdate(createTableSQL);

            while (true) {
                System.out.println("\n==== Employee Management System ====");
                System.out.println("1. Add Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Search Employee");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1: // CREATE
                        System.out.print("Enter Name: "); String name = sc.nextLine();
                        System.out.print("Enter Email: "); String email = sc.nextLine();
                        System.out.print("Enter Department: "); String dept = sc.nextLine();
                        System.out.print("Enter Salary: "); double salary = sc.nextDouble();

                        String insertSQL = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";
                        PreparedStatement psInsert = con.prepareStatement(insertSQL);
                        psInsert.setString(1, name);
                        psInsert.setString(2, email);
                        psInsert.setString(3, dept);
                        psInsert.setDouble(4, salary);
                        psInsert.executeUpdate();
                        System.out.println("✔ Employee Added Successfully!");
                        break;

                    case 2: // READ ALL
                        String selectAll = "SELECT * FROM employees";
                        ResultSet rsAll = stmt.executeQuery(selectAll);
                        System.out.println("\nID | Name | Email | Department | Salary");
                        System.out.println("---------------------------------------");
                        while (rsAll.next()) {
                            System.out.println(rsAll.getInt("id") + " | " + rsAll.getString("name") + " | " +
                                    rsAll.getString("email") + " | " + rsAll.getString("department") + " | " + rsAll.getDouble("salary"));
                        }
                        break;

                    case 3: // UPDATE
                        System.out.print("Enter Employee ID to Update: "); int upId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter New Department: "); String newDept = sc.nextLine();
                        System.out.print("Enter New Salary: "); double newSalary = sc.nextDouble();

                        String updateSQL = "UPDATE employees SET department = ?, salary = ? WHERE id = ?";
                        PreparedStatement psUpdate = con.prepareStatement(updateSQL);
                        psUpdate.setString(1, newDept);
                        psUpdate.setDouble(2, newSalary);
                        psUpdate.setInt(3, upId);
                        int rowsUpdated = psUpdate.executeUpdate();
                        System.out.println(rowsUpdated > 0 ? "✔ Update Successful!" : "✖ Employee ID not found.");
                        break;

                    case 4: // DELETE
                        System.out.print("Enter Employee ID to Delete: "); int delId = sc.nextInt();
                        String deleteSQL = "DELETE FROM employees WHERE id = ?";
                        PreparedStatement psDelete = con.prepareStatement(deleteSQL);
                        psDelete.setInt(1, delId);
                        int rowsDeleted = psDelete.executeUpdate();
                        System.out.println(rowsDeleted > 0 ? "✔ Employee Deleted!" : "✖ ID not found.");
                        break;

                    case 5: // SEARCH
                        System.out.print("Enter Employee ID to Search: "); int searchId = sc.nextInt();
                        String searchSQL = "SELECT * FROM employees WHERE id = ?";
                        PreparedStatement psSearch = con.prepareStatement(searchSQL);
                        psSearch.setInt(1, searchId);
                        ResultSet rsSearch = psSearch.executeQuery();
                        if (rsSearch.next()) {
                            System.out.println("Found: " + rsSearch.getString("name") + " (" + rsSearch.getString("department") + ")");
                        } else {
                            System.out.println("✖ No employee found with ID: " + searchId);
                        }
                        break;

                    case 6: // EXIT
                        System.out.println("Exiting System... Goodbye!");
                        con.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid Choice! Try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}








