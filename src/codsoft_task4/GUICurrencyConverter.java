package codsoft_task4;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GUICurrencyConverter {
     JFrame frame;
     JTextField amountField;
    JTextArea resultArea;
     JComboBox<String> baseCurrencyBox;
    JComboBox<String> targetCurrencyBox;
     CurrencyConverterMain converter;

    // Maps for currency details
    private static final Map<String, String> CURRENCY_NAMES = new HashMap<>();
    private static final Map<String, String> CURRENCY_SYMBOLS = new HashMap<>();
    private static final Map<String, String> CURRENCY_CODES = new HashMap<>();

    static {
        CURRENCY_NAMES.put("USD", "United States Dollar");
        CURRENCY_NAMES.put("INR", "Indian Rupee");
        CURRENCY_NAMES.put("GBP", "British Pound");
        CURRENCY_NAMES.put("EUR", "Euro");
        CURRENCY_NAMES.put("AUD", "Australian Dollar");
        CURRENCY_NAMES.put("CAD", "Canadian Dollar");
        CURRENCY_NAMES.put("CNY", "Chinese Yuan");
        CURRENCY_NAMES.put("JPY", "Japanese Yen");

        CURRENCY_SYMBOLS.put("USD", "$");
        CURRENCY_SYMBOLS.put("INR", "₹");
        CURRENCY_SYMBOLS.put("GBP", "£");
        CURRENCY_SYMBOLS.put("EUR", "€");
        CURRENCY_SYMBOLS.put("AUD", "A$");
        CURRENCY_SYMBOLS.put("CAD", "C$");
        CURRENCY_SYMBOLS.put("CNY", "¥");
        CURRENCY_SYMBOLS.put("JPY", "¥");

        // Reverse map for lookup
        for (Map.Entry<String, String> entry : CURRENCY_NAMES.entrySet()) {
            CURRENCY_CODES.put(entry.getValue(), entry.getKey());
        }
    }

    public GUICurrencyConverter() {
        converter = new CurrencyConverterMain();
        createAndShowGUI();
    }

     void createAndShowGUI() {
    frame = new JFrame("Currency Converter");
    frame.setSize(1500, 1000); // Increased size for better layout
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null); // Use null layout for absolute positioning

    // Heading setup
    JLabel heading = new JLabel("CURRENCY CONVERTER");
    heading.setBounds(100, 5, 1500, 80); // Positioning the heading at the top
    heading.setFont(new Font("Verdana", Font.BOLD, 56)); // Large font size
    heading.setForeground(new Color(0, 0, 204));
    frame.add(heading);

    // Panel setup
    JPanel panel = new JPanel();
    panel.setLayout(null); // Allow manual positioning
    panel.setBounds(0, 100, 1500, 800); // Adjust position and size
    frame.add(panel);

    JLabel baseCurrencyLabel = new JLabel("Base Currency:");
    baseCurrencyLabel.setBounds(300, 40, 500, 30);
    baseCurrencyLabel.setFont(new Font("Verdana", Font.BOLD, 26)); // Large font size
    baseCurrencyLabel.setForeground(Color.RED);
    panel.add(baseCurrencyLabel);

    baseCurrencyBox = new JComboBox<>(CURRENCY_NAMES.values().toArray(new String[0]));
    baseCurrencyBox.setBounds(600, 40, 200, 30);
    baseCurrencyBox.setFont(new Font("Verdana", Font.BOLD, 16)); // Large font size
    baseCurrencyBox.setForeground(Color.BLACK);
    panel.add(baseCurrencyBox);

    JLabel targetCurrencyLabel = new JLabel("Target Currency:");
    targetCurrencyLabel.setBounds(300, 150, 500, 32);
    targetCurrencyLabel.setFont(new Font("Verdana", Font.BOLD, 26)); // Large font size
    targetCurrencyLabel.setForeground(Color.RED);
    panel.add(targetCurrencyLabel);

    targetCurrencyBox = new JComboBox<>(CURRENCY_NAMES.values().toArray(new String[0]));
    targetCurrencyBox.setBounds(600, 150, 200, 30);
    targetCurrencyBox.setFont(new Font("Verdana", Font.BOLD, 16)); // Large font size
    targetCurrencyBox.setForeground(Color.BLACK);
    panel.add(targetCurrencyBox);

    JLabel amountLabel = new JLabel("Amount:");
    amountLabel.setBounds(300, 260, 150, 30);
    amountLabel.setFont(new Font("Verdana", Font.BOLD, 26)); // Large font size
    amountLabel.setForeground(Color.RED);
    panel.add(amountLabel);

    amountField = new JTextField();
    amountField.setBounds(600, 260, 200, 30);
    amountField.setFont(new Font("Verdana", Font.BOLD, 16)); // Large font size
    amountField.setForeground(Color.BLACK);
    panel.add(amountField);

    JButton convertButton = new JButton("Convert");
    convertButton.setBounds(500, 400, 200, 50);
    convertButton.setBackground(Color.BLUE);
    convertButton.setForeground(Color.WHITE);
    convertButton.setFont(new Font("Tahoma", Font.BOLD, 30));
    panel.add(convertButton);

    convertButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            convertCurrency();
        }
    });

    resultArea = new JTextArea();
    resultArea.setBounds(1100, 180, 1000, 150);// Adjust size and position
    resultArea.setFont(new Font("Verdana", Font.BOLD, 26));
    resultArea.setForeground(Color.GREEN);
    resultArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(resultArea);
    scrollPane.setBounds(850, 40, 500, 300); 
   
    panel.add(scrollPane);

    // Image setup
    File imgFile1 = new File("C:\\Users\\Administrator\\Downloads\\icons\\cc4.png");
    if (!imgFile1.exists()) {
        System.err.println("File not found: " + imgFile1.getAbsolutePath());
    } else {
        ImageIcon i1 = new ImageIcon(imgFile1.getAbsolutePath());
        Image i2 = i1.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH); // Scale image to fit
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(40, 250, 400, 400); // Adjust bounds to fit within the frame
        panel.add(image);
    }

    frame.setVisible(true);
}

    private void convertCurrency() {
        String baseCurrencyName = (String) baseCurrencyBox.getSelectedItem();
        String targetCurrencyName = (String) targetCurrencyBox.getSelectedItem();
        String baseCurrencyCode = CURRENCY_CODES.get(baseCurrencyName);
        String targetCurrencyCode = CURRENCY_CODES.get(targetCurrencyName);
        double amount = getAmount();

        if (amount <= 0) {
            resultArea.setText("Invalid amount.");
            return;
        }

        try {
            double convertedAmount = converter.convertAmount(amount, baseCurrencyCode, targetCurrencyCode);
            String targetSymbol = CURRENCY_SYMBOLS.get(targetCurrencyCode);
            resultArea.setText(String.format("Converted Amount: %.2f %s", convertedAmount, targetSymbol));
        } catch (Exception e) {
            resultArea.setText("Error fetching exchange rate.");
            e.printStackTrace();
        }
    }

    private double getAmount() {
        try {
            return Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUICurrencyConverter());
    }
}
