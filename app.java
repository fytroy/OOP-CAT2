import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserForm extends JFrame {

    private JTextField nameField, ageField, emailField;
    private JTextArea displayArea;

    public UserForm() {
        super("User Form");

        // Set up UI components
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserDetails();
                displayUserData();
            }
        });
        add(saveButton);

        displayArea = new JTextArea();
        add(displayArea);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void saveUserDetails() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String email = emailField.getText();

        // Save to the database
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            String query = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayUserData() {
        // Fetch data from the database and display in the text area
        StringBuilder result = new StringBuilder("User Details:\n");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.append("Name: ").append(resultSet.getString("name")).append("\n");
                result.append("Age: ").append(resultSet.getInt("age")).append("\n");
                result.append("Email: ").append(resultSet.getString("email")).append("\n\n");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        displayArea.setText(result.toString());
    }

    public static void main(String[] args) {
        // Initialize the database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserForm();
            }
        });
    }
}
