import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageEncryptionApp {

    private static JFrame frame;
    private static JTextArea logArea;
    private static SecretKey secretKey; // Store the encryption key

    public static void main(String[] args) {
        // Create the main application frame
        frame = new JFrame("Image Encryption/Decryption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create buttons for selecting image and decrypting
        JButton selectImageButton = new JButton("Select Image");
        JButton decryptButton = new JButton("Decrypt Image");

        // Add action listeners for the buttons
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImageAndEncrypt();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectEncryptedImageAndDecrypt();
            }
        });

        // Create a text area for logging
        logArea = new JTextArea(10, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectImageButton);
        buttonPanel.add(decryptButton);

        // Add components to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Set frame properties and display it
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Generate or retrieve the encryption key
        secretKey = generateOrRetrieveKey();
    }

    private static SecretKey generateOrRetrieveKey() {
        // Check if the key file exists, and if so, retrieve the key
        String keyFilePath = "encryption_key.key";
        if (Files.exists(Paths.get(keyFilePath))) {
            try {
                byte[] encodedKey = Files.readAllBytes(Paths.get(keyFilePath));
                return new SecretKeySpec(encodedKey, "AES");
            } catch (IOException e) {
                e.printStackTrace();
                log("Error reading the encryption key file: " + e.getMessage());
            }
        }

        // If the key file doesn't exist, generate a new key and save it
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // 128-bit key size
            SecretKey key = keyGen.generateKey();

            // Save the key to a file for future use
            byte[] encodedKey = key.getEncoded();
            Files.write(Paths.get(keyFilePath), encodedKey);

            return key;
        } catch (Exception e) {
            e.printStackTrace();
            log("Error generating or saving the encryption key: " + e.getMessage());
            return null;
        }
    }

    private static void selectImageAndEncrypt() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            log("Selected
