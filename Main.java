import java.sql.*;
import java.util.*;
public class Main {

    //Retrieves and displays all records from the students table.
    public static void getAllStudents(Statement statement) {
        try {
            //query to get all students from the table and ordering it by student ID
            statement.executeQuery("SELECT * FROM students ORDER BY student_id");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                //prints out every row
               System.out.print(resultSet.getInt("student_id") + "\t");
               System.out.print(resultSet.getString("first_name") + " ");
               System.out.print(resultSet.getString("last_name") + "\t");
               System.out.print(resultSet.getString("email") + "\t");
               System.out.println(resultSet.getDate("enrollment_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Inserts a new student record into the students table.
    public static void addStudent(Statement statement, String fName, String lName, String email, String date) {
        try {
            int rowInserted  = statement.executeUpdate(String.format("INSERT INTO students (first_name, last_name, email, enrollment_date) " + "VALUES ('%s', '%s', '%s', '%s');", fName, lName, email, date));
            //this checks if the student was added correctly
            if (rowInserted > 0) System.out.println("A new student was successfully added");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Updates the email address for a student with the specified student_id.
    public static void updateStudentEmail(Statement statement, int id, String email) {
        try {
            statement.executeUpdate(String.format("UPDATE students SET email='%s' WHERE student_id=%d;", email, id));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Deletes the record of the student with the specified student_id.
    public static void deleteStudent(Statement statement, int id){
        try {
            statement.executeUpdate(String.format("DELETE FROM students WHERE student_id=%d", id));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String url = "jdbc:postgresql://localhost:5432/3005A3Q1";
        String user = "postgres";
        String password = "C6d84dqt1@@";
        String createSQL = "DROP TABLE IF EXISTS students;\n" +
                "\n" +
                "CREATE TABLE students (\n" +
                "    student_id SERIAL PRIMARY KEY,\n" +
                "    first_name TEXT NOT NULL,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    email TEXT NOT NULL UNIQUE,\n" +
                "    enrollment_date DATE\n" +
                ");";
        String fillSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES\n" +
                "('John', 'Doe', 'john.doe@example.com', '2023-09-01'),\n" +
                "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),\n" +
                "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');";
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();
            if (conn != null){
                //creating table and filling it with data
                statement.executeUpdate(createSQL);
                statement.executeUpdate(fillSQL);

                while (true){
                    //showcasing menu for user to pick
                    System.out.println("Menu: \n1. getAllStudents()\n2. addStudent(first_name, last_name, email, enrollment_date)" +
                            "\n3. updateStudentEmail(student_id, new_email)\n4. deleteStudent(student_id)\n5. Exit\nEnter choice: ");
                    int choice = input.nextInt();
                    // calls functions based on user's choice
                    if (choice == 1) {
                        getAllStudents(statement);
                    } else if (choice == 2) {
                        //prompts user for values to add into the table
                        System.out.println("Enter first name: ");
                        String fname = input.next();
                        System.out.println("Enter last name: ");
                        String lname = input.next();
                        System.out.println("Enter email: ");
                        String email = input.next();
                        System.out.println("Enter date in this format YYYY-MM-DD: ");
                        String date = input.next();
                        addStudent(statement, fname, lname, email, date);
                    } else if (choice == 3) {
                        //prompts user for values to update a student's email
                        System.out.println("Enter student id: ");
                        int id = input.nextInt();
                        System.out.println("Enter new email: ");
                        String newEmail = input.next();
                        updateStudentEmail(statement, id, newEmail);
                    } else if (choice == 4) {
                        //prompts user for student id to delete from table
                        System.out.println("Enter student id: ");
                        int deleteID = input.nextInt();
                        deleteStudent(statement, deleteID);
                    } else break;
                }
            } else {
                System.out.println("Failed to establish connection.");
            }
            input.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
