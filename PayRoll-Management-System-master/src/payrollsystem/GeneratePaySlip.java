package payrollsystem;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.time.Month;

public class GeneratePaySlip extends JFrame implements ActionListener, ItemListener {
    private Choice empIdChoice;
    private JTextArea paySlipText;
    private JButton generatePay, print;

    GeneratePaySlip() {
        setTitle("Pay Slip Generator");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        empIdChoice = new Choice();
        try {
            Conn connect = new Conn();
            ResultSet rs = connect.s.executeQuery("SELECT * FROM employee");
            empIdChoice.add("");
            while (rs.next()) {
                empIdChoice.add(rs.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Employee ID"));
        panel.add(empIdChoice);
        add(panel, BorderLayout.NORTH);

        paySlipText = new JTextArea(30, 80);
        paySlipText.setFont(new Font("Arial", Font.BOLD, 20));
        JScrollPane scroll = new JScrollPane(paySlipText);
        add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel();
        generatePay = new JButton("Generate Pay Slip");
        print = new JButton("Print");

        south.add(print);
        south.add(generatePay);
        add(south, BorderLayout.SOUTH);

        generatePay.addActionListener(this);
        print.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (empIdChoice.getSelectedItem().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an Employee ID");
            return;
        }

        if (ae.getSource() == print) {
            try {
                paySlipText.print();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Printing Error, Please Try Again");
                e.printStackTrace();
            }
        } else {
            try {
                Conn connect = new Conn();
                String query = "SELECT * FROM employee WHERE id='" + empIdChoice.getSelectedItem() + "'";
                ResultSet rs = connect.s.executeQuery(query);

                if (rs.next()) {
                    String name = rs.getString("name");

                    rs = connect.s.executeQuery("SELECT * FROM salary WHERE id='" + empIdChoice.getSelectedItem() + "'");
                    double gross = 0;
                    double net = 0;

                    Date date = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int m = cal.get(Calendar.MONTH);
                    Month mth = Month.of(m);
                    String month = mth.toString();

                    paySlipText.setText(" ---------------- PAY SLIP FOR THE MONTH OF " + month + ", 2019 ------------------------\n");
                    paySlipText.append("     Employee ID " + empIdChoice.getSelectedItem() + "\n");
                    paySlipText.append("     Employee Name " + name + "\n");
                    paySlipText.append("----------------------------------------------------------------\n");

                    rs.next();
                    double hra = rs.getDouble("hra");
                    paySlipText.append("                  HRA         : " + hra + "\n");
                    double da = rs.getDouble("da");
                    paySlipText.append("                  DA          : " + da + "\n");
                    double med = rs.getDouble("med");
                    paySlipText.append("                  MED         : " + med + "\n");
                    double pf = rs.getDouble("pf");
                    paySlipText.append("                  PF          : " + pf + "\n");
                    double basic = rs.getDouble("basic_salary");
                    gross = hra + da + med + pf + basic;
                    net = gross - pf;
                    paySlipText.append("                  BASIC SALARY : " + basic + "\n");
                    paySlipText.append("-------------------------------------------------------\n");
                    paySlipText.append("\n");
                    paySlipText.append("       GROSS SALARY :" + gross + "    \n");
                    paySlipText.append("       NET SALARY : " + net + "\n");
                    paySlipText.append("       Tax   :   2.1% of gross " + (gross * 2.1 / 100) + "\n");
                    paySlipText.append(" -------------------------------------------------\n\n\n\n");
                    paySlipText.append("   (  Signature  )      \n");
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

  //  public static void main(String[] args) {
  //      SwingUtilities.invokeLater(() -> new GeneratePaySlip());
  //  }

    @Override
   public void itemStateChanged(ItemEvent e) {
    throw new UnsupportedOperationException("Unimplemented method 'itemStateChanged'");
    }
}

