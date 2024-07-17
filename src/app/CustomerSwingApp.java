package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerSwingApp {
    private JFrame frame;
    private JLabel lblId, lblLastName, lblFirstName, lblPhone;
    private JButton btnPrevious, btnNext;
    private List<Customer> customers;
    private int currentIndex;

    public CustomerSwingApp() {
        customers = fetchCustomersFromDatabase();

        frame = new JFrame("Customer");
        frame.setLayout(new GridLayout(5, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblId = new JLabel();
        lblLastName = new JLabel();
        lblFirstName = new JLabel();
        lblPhone = new JLabel();

        btnPrevious = new JButton("Previous");
        btnNext = new JButton("Next");

        frame.add(new JLabel("ID:"));
        frame.add(lblId);
        frame.add(new JLabel("Last Name:"));
        frame.add(lblLastName);
        frame.add(new JLabel("First Name:"));
        frame.add(lblFirstName);
        frame.add(new JLabel("Phone:"));
        frame.add(lblPhone);
        frame.add(btnPrevious);
        frame.add(btnNext);

        btnPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    updateCustomerInfo();
                }
            }
        });

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < customers.size() - 1) {
                    currentIndex++;
                    updateCustomerInfo();
                }
            }
        });

        currentIndex = 0;
        updateCustomerInfo();

        frame.pack();
        frame.setVisible(true);
    }

    private List<Customer> fetchCustomersFromDatabase() {
        List<Customer> customerList = new ArrayList<>();
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Customer";
        String jdbcUser = "postgres";
        String jdbcPassword = "roza2024";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")) {

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String lastName = rs.getString("customer_last_name");
                String firstName = rs.getString("customer_first_name");
                String phone = rs.getString("customer_phone");
                customerList.add(new Customer(id, lastName, firstName, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    private void updateCustomerInfo() {
        if (customers.isEmpty()) {
            lblId.setText("");
            lblLastName.setText("");
            lblFirstName.setText("");
            lblPhone.setText("");
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        Customer customer = customers.get(currentIndex);
        lblId.setText(String.valueOf(customer.getId()));
        lblLastName.setText(customer.getLastName());
        lblFirstName.setText(customer.getFirstName());
        lblPhone.setText(customer.getPhone());

        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < customers.size() - 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerSwingApp();
            }
        });
    }
}
