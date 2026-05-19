import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class HotelReservationSystem {
    static final String FILE_NAME = "bookings.txt";
    JFrame mainFrame;
    public HotelReservationSystem() {
        mainFrame = new JFrame("Hotel Reservation System");
        mainFrame.setSize(500, 400);
        mainFrame.setLayout(null);
        mainFrame.getContentPane().setBackground(Color.BLACK);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel title = new JLabel("HOTEL RESERVATION SYSTEM");
        title.setBounds(90, 30, 350, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        mainFrame.add(title);
        JButton searchBtn = createButton("Search Rooms", 140, 100);
        JButton bookBtn = createButton("Book Room", 140, 160);
        JButton viewBtn = createButton("View / Cancel Booking", 140, 220);
        JButton exitBtn = createButton("Exit", 140, 280);
        mainFrame.add(searchBtn);
        mainFrame.add(bookBtn);
        mainFrame.add(viewBtn);
        mainFrame.add(exitBtn);
        searchBtn.addActionListener(e -> new SearchWindow());
        bookBtn.addActionListener(e -> new BookingWindow());
        viewBtn.addActionListener(e -> new ViewBookingWindow());
        exitBtn.addActionListener(e -> System.exit(0));
        mainFrame.setVisible(true);
    }
    JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 200, 40);
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }
    class SearchWindow {
        JFrame frame;
        SearchWindow() {
            frame = new JFrame("Available Rooms");
            frame.setSize(500, 350);
            frame.getContentPane().setBackground(Color.BLACK);
            frame.setLayout(null);
            JLabel title = new JLabel("ROOM DETAILS");
            title.setBounds(170, 20, 200, 30);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 20));
            frame.add(title);
            String[] columns = {"Room No", "Type", "Price", "Status"};
            String[][] data = {
                    {"101", "Standard", "1000", "Available"},
                    {"102", "Deluxe", "2500", "Available"},
                    {"103", "Suite", "5000", "Available"}
            };
            JTable table = new JTable(data, columns);
            table.setBackground(Color.BLACK);
            table.setForeground(Color.WHITE);
            table.setGridColor(Color.WHITE);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(25);
            JScrollPane pane = new JScrollPane(table);
            pane.setBounds(40, 80, 400, 120);
            frame.add(pane);
            frame.setVisible(true);
        }
    }
    class BookingWindow {
        JFrame frame;
        JTextField nameField, phoneField, daysField;
        JComboBox<String> roomBox;
        BookingWindow() {
            frame = new JFrame("Book Room");
            frame.setSize(500, 450);
            frame.getContentPane().setBackground(Color.BLACK);
            frame.setLayout(null);

            JLabel title = new JLabel("ROOM BOOKING");
            title.setBounds(160, 20, 250, 30);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 22));
            frame.add(title);

            JLabel name = createLabel("Customer Name:", 50, 90);
            JLabel phone = createLabel("Phone Number:", 50, 140);
            JLabel room = createLabel("Room Type:", 50, 190);
            JLabel days = createLabel("Days:", 50, 240);

            frame.add(name);
            frame.add(phone);
            frame.add(room);
            frame.add(days);

            nameField = createTextField(220, 90);
            phoneField = createTextField(220, 140);
            daysField = createTextField(220, 240);

            frame.add(nameField);
            frame.add(phoneField);
            frame.add(daysField);

            String[] rooms = {"Standard", "Deluxe", "Suite"};
            roomBox = new JComboBox<>(rooms);
            roomBox.setBounds(220, 190, 180, 30);
            frame.add(roomBox);
            JButton bookBtn = createButton("Confirm Booking", 140, 320);
            frame.add(bookBtn);
            bookBtn.addActionListener(e -> saveBooking());
            frame.setVisible(true);
        }
        void saveBooking() {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String room = (String) roomBox.getSelectedItem();
            String days = daysField.getText();
            int price = 0;
            if (room.equals("Standard"))
                price = 1000;
            else if (room.equals("Deluxe"))
                price = 2500;
            else
                price = 5000;
            int total = price * Integer.parseInt(days);
            try {
                FileWriter fw = new FileWriter(FILE_NAME, true);
                fw.write(name + "," + phone + "," + room + "," + days + "," + total + "\n");
                fw.close();
                JOptionPane.showMessageDialog(frame,
                        "Payment Successful\nBooking Confirmed!\nTotal Amount: ₹" + total);
                nameField.setText("");
                phoneField.setText("");
                daysField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error Saving Booking");
            }
        }
    }
    class ViewBookingWindow {
        JFrame frame;
        JTable table;
        DefaultTableModel model;
        JTextField cancelField;
        ViewBookingWindow() {
            frame = new JFrame("View Bookings");
            frame.setSize(700, 450);
            frame.getContentPane().setBackground(Color.BLACK);
            frame.setLayout(null);

            JLabel title = new JLabel("BOOKING DETAILS");
            title.setBounds(240, 20, 250, 30);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 22));
            frame.add(title);

            String[] columns = {"Name", "Phone", "Room", "Days", "Amount"};
            model = new DefaultTableModel(columns, 0);

            table = new JTable(model);
            table.setBackground(Color.BLACK);
            table.setForeground(Color.WHITE);
            table.setGridColor(Color.WHITE);
            table.setRowHeight(25);

            JScrollPane pane = new JScrollPane(table);
            pane.setBounds(30, 80, 620, 200);
            frame.add(pane);
            loadBookings();
            JLabel cancelLabel = createLabel("Enter Row Number to Cancel:", 120, 320);
            frame.add(cancelLabel);
            cancelField = createTextField(380, 320);
            frame.add(cancelField);
            JButton cancelBtn = createButton("Cancel Booking", 230, 360);
            frame.add(cancelBtn);
            cancelBtn.addActionListener(e -> cancelBooking());
            frame.setVisible(true);
        }
        void loadBookings() {
            try {
                BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    model.addRow(data);
                }
                br.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "No Bookings Found");
            }
        }
        void cancelBooking() {
            try {
                int row = Integer.parseInt(cancelField.getText());
                if (row >= 0 && row < model.getRowCount()) {
                    model.removeRow(row);
                    FileWriter fw = new FileWriter(FILE_NAME);
                    for (int i = 0; i < model.getRowCount(); i++) {
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            fw.write(model.getValueAt(i, j).toString());
                            if (j != model.getColumnCount() - 1)
                                fw.write(",");
                        }
                        fw.write("\n");
                    }
                    fw.close();
                    JOptionPane.showMessageDialog(frame, "Booking Cancelled Successfully");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid Row Number");
            }
        }
    }
    JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        return label;
    }
    JTextField createTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 180, 30);
        field.setBackground(Color.DARK_GRAY);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        return field;
    }
    public static void main(String[] args) {
        new HotelReservationSystem();
    }
}
