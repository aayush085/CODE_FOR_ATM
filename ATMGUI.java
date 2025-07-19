import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ATMGUI extends JFrame {
    private final JTextField amountField, pinField;
    private final JTextArea displayArea;
    private final JButton checkBalanceBtn, depositBtn, withdrawBtn, clearBtn, exitBtn;
    private double balance = 1000.00;
    private final ArrayList<String> transactionHistory = new ArrayList<>();
    private final String correctPIN = "1234";

    public ATMGUI() {
        setTitle("ATM Banking System");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("PIN:"));
        pinField = new JPasswordField(4);
        inputPanel.add(pinField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        checkBalanceBtn = new JButton("Check Balance");
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        clearBtn = new JButton("Clear");
        exitBtn = new JButton("Exit");

        buttonPanel.add(checkBalanceBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exitBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        checkBalanceBtn.addActionListener(_ -> authenticateAndExecute(() -> displayArea.setText("Your current balance is: $" + balance)));

        depositBtn.addActionListener(_ -> authenticateAndExecute(() -> handleTransaction("Deposit")));
        withdrawBtn.addActionListener(_ -> authenticateAndExecute(() -> handleTransaction("Withdraw")));
        clearBtn.addActionListener(_ -> {
            amountField.setText("");
            pinField.setText("");
            displayArea.setText("");
        });

        exitBtn.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void authenticateAndExecute(Runnable action) {
        if (pinField.getText().equals(correctPIN)) {
            action.run();
        } else {
            displayArea.setText("Incorrect PIN. Access Denied.");
        }
    }

    private void handleTransaction(String type) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount > 0) {
                if (type.equals("Deposit")) {
                    balance += amount;
                    transactionHistory.add("Deposited $" + amount);
                } else if (type.equals("Withdraw")) {
                    if (amount <= balance) {
                        balance -= amount;
                        transactionHistory.add("Withdrew $" + amount);
                    } else {
                        displayArea.setText("Insufficient balance.");
                        return;
                    }
                }
                displayArea.setText(type + " $" + amount + "\nNew Balance: $" + balance + "\nTransaction History:\n" + String.join("\n", transactionHistory));
            } else {
                displayArea.setText("Enter a valid positive amount.");
            }
        } catch (NumberFormatException ex) {
            displayArea.setText("Invalid amount. Please enter a number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPanel().setVisible(true));
    }
}

class LoginPanel extends JFrame {

    public static String getCorrectUsername() {
        return correctUsername;
    }

    public static String getCorrectPassword() {
        return correctPassword;
    }
    private final JTextField usernameField;
    private final JPasswordField passField;
    private final JButton loginBtn;
    private static final String correctUsername = "admin";  
    private static final String correctPassword = "password";  

    public LoginPanel() {
        setTitle("User Login");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = new String(passField.getPassword());

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                this.dispose();
                new ATMGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials! Try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}