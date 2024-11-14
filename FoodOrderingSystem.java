
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class FoodOrderingSystem extends JFrame {

    private Map<String, MenuItem> menuItems = new HashMap<>();
    private Map<String, Integer> cart = new HashMap<>();
    private JPanel cartPanel;
    private JLabel totalLabel;
    private JDialog confirmationDialog;
    private JTextField addressField;

    // Define color scheme
    private static final Color DARK_PURPLE = new Color(59, 30, 84);
    private static final Color MEDIUM_PURPLE = new Color(155, 126, 189);
    private static final Color LIGHT_PURPLE = new Color(212, 190, 228);
    private static final Color WHITE = new Color(255, 255, 255);

    private static class MenuItem {

        String name;
        double price;
        String description;
        String imagePath;

        MenuItem(String name, double price, String description, String imagePath) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.imagePath = imagePath;
        }
    }

    public FoodOrderingSystem() {
        setTitle("Food Ordering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(LIGHT_PURPLE);

        initializeMenu();

        JScrollPane menuScrollPane = createMenuPanel();
        cartPanel = createCartPanel();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_PURPLE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(menuScrollPane, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);

        add(mainPanel);
        createConfirmationDialog();
    }

    private void initializeMenu() {
        menuItems.put("Butter Chicken", new MenuItem("Butter Chicken", 350.0, "Creamy tomato-based chicken curry", "butterchicken.jpg"));
        menuItems.put("Paneer Tikka", new MenuItem("Paneer Tikka", 250.0, "Grilled paneer with spices", "paneertikka.jpg"));
        menuItems.put("Biryani", new MenuItem("Biryani", 300.0, "Aromatic basmati rice with spices and meat", "biriyani.jpg"));
        menuItems.put("Samosa", new MenuItem("Samosa", 50.0, "Crispy pastry stuffed with spiced potatoes", "samosa.jpg"));
        menuItems.put("Chole Bhature", new MenuItem("Chole Bhature", 150.0, "Spicy chickpeas with fried bread", "cholebhature.jpg"));
        menuItems.put("Masala Dosa", new MenuItem("Masala Dosa", 120.0, "South Indian crepe with potato filling", "dosa.jpg"));
        menuItems.put("Tandoori Chicken", new MenuItem("Tandoori Chicken", 350.0, "Grilled chicken marinated in spices", "chickentikka.jpg"));
        menuItems.put("Aloo Paratha", new MenuItem("Aloo Paratha", 100.0, "Flatbread stuffed with spiced potatoes", "allooparatha.jpg"));
        menuItems.put("Pav Bhaji", new MenuItem("Pav Bhaji", 150.0, "Mixed vegetable curry with buttered bread", "paavbhaji.jpg"));
        menuItems.put("Dal Makhani", new MenuItem("Dal Makhani", 200.0, "Rich lentil curry with butter and cream", "dalmakhani.jpg"));
        menuItems.put("Vada Pav", new MenuItem("Vada Pav", 50.0, "Mumbai-style potato fritter in a bun", "vadapaav.jpg"));
        menuItems.put("Pani Puri", new MenuItem("Pani Puri", 40.0, "Crispy balls filled with tangy water", "paanipoori.jpg"));
        menuItems.put("Rajma Chawal", new MenuItem("Rajma Chawal", 150.0, "Red kidney beans with rice", "rajmachawal.jpg"));
        menuItems.put("Malai Kofta", new MenuItem("Malai Kofta", 250.0, "Creamy curry with fried paneer balls", "malaikofta.jpg"));
        menuItems.put("Aloo Gobi", new MenuItem("Aloo Gobi", 180.0, "Spiced potatoes and cauliflower", "gobi allo.jpg"));
        menuItems.put("Bhindi Masala", new MenuItem("Bhindi Masala", 200.0, "Spicy stir-fried okra", "bhindi.jpg"));
        menuItems.put("Rogan Josh", new MenuItem("Rogan Josh", 400.0, "Kashmiri-style red lamb curry", "Mutton Rogan Josh.jpg"));
        menuItems.put("Idli Sambar", new MenuItem("Idli Sambar", 80.0, "Steamed rice cakes with lentil soup", "idli sambar.jpg"));
        menuItems.put("Kadhi Pakora", new MenuItem("Kadhi Pakora", 150.0, "Gram flour fritters in yogurt curry", "kadi pakoda.jpg"));


    }

    private JScrollPane createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        for (MenuItem item : menuItems.values()) {
            JPanel itemPanel = new JPanel(new BorderLayout(5, 5));
            itemPanel.setBackground(WHITE);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MEDIUM_PURPLE, 1),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));

            JLabel nameLabel = new JLabel(item.name);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setForeground(DARK_PURPLE);

            JLabel priceLabel = new JLabel("₹" + item.price);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setForeground(MEDIUM_PURPLE);

            JLabel descLabel = new JLabel(item.description);
            descLabel.setForeground(Color.GRAY);

            // Adding image for each menu item
            ImageIcon itemImageIcon = new ImageIcon(item.imagePath);
            JLabel itemImageLabel = new JLabel(new ImageIcon(itemImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

            JButton addButton = createStyledButton("Add to Cart");
            addButton.addActionListener(e -> addToCart(item.name));

            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
            infoPanel.setBackground(WHITE);
            infoPanel.add(nameLabel);
            infoPanel.add(priceLabel);
            infoPanel.add(descLabel);

            itemPanel.add(itemImageLabel, BorderLayout.WEST);  // Display image on the left
            itemPanel.add(infoPanel, BorderLayout.CENTER);
            itemPanel.add(addButton, BorderLayout.EAST);

            panel.add(itemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(300, getHeight()));

        JPanel cartItemsPanel = new JPanel();
        cartItemsPanel.setBackground(WHITE);
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));

        totalLabel = new JLabel("Total: ₹0.0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(DARK_PURPLE);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JButton orderButton = createStyledButton("Place Order");
        orderButton.setFont(new Font("Arial", Font.BOLD, 14));
        orderButton.addActionListener(e -> showConfirmation());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        JLabel cartTitle = new JLabel("Your Cart");
        cartTitle.setFont(new Font("Arial", Font.BOLD, 20));
        cartTitle.setForeground(DARK_PURPLE);
        cartTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        headerPanel.add(cartTitle, BorderLayout.NORTH);
        headerPanel.add(totalLabel, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(cartItemsPanel), BorderLayout.CENTER);
        panel.add(orderButton, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(MEDIUM_PURPLE);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(DARK_PURPLE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(MEDIUM_PURPLE);
            }
        });

        return button;
    }

    private void createConfirmationDialog() {
        confirmationDialog = new JDialog(this, "Order Confirmation", true);
        confirmationDialog.setSize(400, 450);
        confirmationDialog.setLayout(new BorderLayout(10, 10));
        confirmationDialog.getContentPane().setBackground(WHITE);

        // Confirmation Image at the top
        ImageIcon confirmImageIcon = new ImageIcon("tick.gif");
        JLabel confirmImageLabel = new JLabel(new ImageIcon(confirmImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        confirmImageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Order Summary Panel
        JPanel orderSummaryPanel = new JPanel();
        orderSummaryPanel.setBackground(WHITE);
        orderSummaryPanel.setLayout(new BoxLayout(orderSummaryPanel, BoxLayout.Y_AXIS));
        orderSummaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Address Panel
        JPanel addressPanel = new JPanel(new BorderLayout(5, 5));
        addressPanel.setBackground(WHITE);
        addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel addressLabel = new JLabel("Delivery Address:");
        addressLabel.setForeground(DARK_PURPLE);
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addressField = new JTextField(20);
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(addressField, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);

        JButton confirmButton = createStyledButton("Confirm Order");
        JButton cancelButton = createStyledButton("Cancel");

        confirmButton.addActionListener(e -> confirmOrder());
        cancelButton.addActionListener(e -> confirmationDialog.setVisible(false));

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        confirmationDialog.add(new JScrollPane(orderSummaryPanel), BorderLayout.CENTER);
        confirmationDialog.add(addressPanel, BorderLayout.NORTH);
        confirmationDialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmationDialog.setLocationRelativeTo(this);
    }

    private void updateCart() {
        JPanel cartItemsPanel = (JPanel) ((JScrollPane) cartPanel.getComponent(1)).getViewport().getView();
        cartItemsPanel.removeAll();

        double total = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = menuItems.get(itemName).price;

            JLabel itemLabel = new JLabel(itemName + " x " + quantity + " - ₹" + (price * quantity));
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemLabel.setForeground(DARK_PURPLE);

            cartItemsPanel.add(itemLabel);
            total += price * quantity;
        }

        totalLabel.setText("Total: ₹" + total);
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void addToCart(String itemName) {
        cart.put(itemName, cart.getOrDefault(itemName, 0) + 1);
        updateCart();
    }

    private void showConfirmation() {
        // Update the order summary
        JPanel orderSummaryPanel = (JPanel) ((JScrollPane) confirmationDialog.getContentPane().getComponent(0)).getViewport().getView();
        orderSummaryPanel.removeAll();

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = menuItems.get(itemName).price;

            JLabel itemLabel = new JLabel(itemName + " x " + quantity + " - ₹" + (price * quantity));
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemLabel.setForeground(DARK_PURPLE);

            orderSummaryPanel.add(itemLabel);
        }

        orderSummaryPanel.revalidate();
        orderSummaryPanel.repaint();

        confirmationDialog.setVisible(true);
    }

    private void confirmOrder() {
        String address = addressField.getText();
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a delivery address.");
            return;
        }

        double total = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = menuItems.get(itemName).price;
            total += price * quantity;

            // Save each item in the order to the database
            saveOrderToDatabase(itemName, quantity, price * quantity, address);
        }

        JOptionPane.showMessageDialog(this, "Order Confirmed!\nDelivery Address: " + address);
        cart.clear();
        updateCart();
        confirmationDialog.setVisible(false);
    }

    // Save the order to the database
    private void saveOrderToDatabase(String itemName, int quantity, double totalPrice, String address) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Orders (item_name, quantity, total_price, delivery_address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, itemName);
                statement.setInt(2, quantity);
                statement.setDouble(3, totalPrice);
                statement.setString(4, address);
                statement.executeUpdate();
                System.out.println("Order saved to database successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FoodOrderingSystem frame = new FoodOrderingSystem();
            frame.setVisible(true);
        });
    }
}
