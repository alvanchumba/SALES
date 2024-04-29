import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Index extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JTextField amountTextField;

    public Index() {
        setTitle("LANDSCAPE KITCHEN");
        setSize(420, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container contentPane = getContentPane();

        JPanel login = new JPanel();
        login.setLayout(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username");
        login.add(usernameLabel);
        usernameTextField = new JTextField();
        login.add(usernameTextField);
        JLabel passwordLabel = new JLabel("Password");
        login.add(passwordLabel);
        passwordField = new JPasswordField();
        login.add(passwordField);
        JButton signInButton = new JButton("Sign In");
        login.add(signInButton);
        JButton cancelButton = new JButton("Cancel");
        login.add(cancelButton);

        JPanel salesPanel = new JPanel();
        salesPanel.setLayout(new GridLayout(4, 2));
        JLabel amountLabel = new JLabel("Amount");
        salesPanel.add(amountLabel);
        amountTextField = new JTextField();
        salesPanel.add(amountTextField);
        JLabel dateLabel = new JLabel("Date");
        salesPanel.add(dateLabel);
        JTextField dateTextField = new JTextField();
        dateTextField.setToolTipText("YYYY-MM-DD");
        salesPanel.add(dateTextField);
        JButton submitButton = new JButton("Submit");
        salesPanel.add(submitButton);
        salesPanel.setVisible(false);

        contentPane.setLayout(new CardLayout());
        contentPane.add(login, "LOGIN");
        contentPane.add(salesPanel, "SALES");

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticate(username, password)) {
                    CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                    cardLayout.show(contentPane, "SALES");
                } else {
                    JOptionPane.showMessageDialog(Index.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int amount = Integer.parseInt(amountTextField.getText());
                String date = dateTextField.getText();
                String username = usernameTextField.getText();
                try {
                    insertProfitData(username, amount, date);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Index.this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/sales", "root", "@Akc15064");
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            boolean authenticated = resultSet.next();
            resultSet.close();
            statement.close();
            connection.close();
            return authenticated;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void insertProfitData(String username, int amount, String date) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/sales", "root", "@Akc15064");
            String sql = "INSERT INTO profit (username, amount, day) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, amount);
            statement.setString(3, date);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data inserted successfully!");
                dispose();
                new Index().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert data", "Error", JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Index();
            }
        });
    }
}






