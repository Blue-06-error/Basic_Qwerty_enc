import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class QwertyEncryption {
    
    // Function to generate QWERTY-based mapping
    public static HashMap<Character, Character> getQwertyMapping(char startLetter) {
        String qwertyOrder = "QWERTYUIOPASDFGHJKLZXCVBNM";
        int startIndex = qwertyOrder.indexOf(Character.toUpperCase(startLetter));
        
        // Rotating the QWERTY mapping
        String mappedOrder = qwertyOrder.substring(startIndex) + qwertyOrder.substring(0, startIndex);
        
        HashMap<Character, Character> mapping = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            mapping.put((char) ('A' + i), mappedOrder.charAt(i));
        }
        return mapping;
    }

    // Function to encrypt message
    public static String encryptMessage(String message, char startLetter1, Character startLetter2) {
        message = message.toUpperCase();
        HashMap<Character, Character> mapping1 = getQwertyMapping(startLetter1);
        HashMap<Character, Character> mapping2 = (startLetter2 != null) ? getQwertyMapping(startLetter2) : mapping1;
        
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (Character.isLetter(c)) {
                if (i < 8 || startLetter2 == null) {
                    encryptedText.append(mapping1.get(c));
                } else {
                    encryptedText.append(mapping2.get(c));
                }
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    // Function to decrypt message
    public static String decryptMessage(String encryptedMessage, char startLetter1, Character startLetter2) {
        encryptedMessage = encryptedMessage.toUpperCase();
        HashMap<Character, Character> reverseMapping1 = getReverseMapping(startLetter1);
        HashMap<Character, Character> reverseMapping2 = (startLetter2 != null) ? getReverseMapping(startLetter2) : reverseMapping1;
        
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length(); i++) {
            char c = encryptedMessage.charAt(i);
            if (Character.isLetter(c)) {
                if (i < 8 || startLetter2 == null) {
                    decryptedText.append(reverseMapping1.get(c));
                } else {
                    decryptedText.append(reverseMapping2.get(c));
                }
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    // Function to generate reverse QWERTY mapping
    public static HashMap<Character, Character> getReverseMapping(char startLetter) {
        HashMap<Character, Character> mapping = getQwertyMapping(startLetter);
        HashMap<Character, Character> reverseMapping = new HashMap<>();
        
        for (char key : mapping.keySet()) {
            reverseMapping.put(mapping.get(key), key);
        }
        return reverseMapping;
    }

    // Function to hash the encrypted message
    public static String hashMessage(String encryptedMessage) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(encryptedMessage.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error computing SHA-256 hash", e);
        }
    }

    // Main function for user interaction
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to (E)ncrypt or (D)ecrypt a message? ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("E")) {
            System.out.print("Enter the message to encrypt: ");
            String message = scanner.nextLine();
            System.out.print("Enter the first starting letter: ");
            char startLetter1 = scanner.next().charAt(0);

            // Only ask for second letter if the message has 8 or more characters
            Character startLetter2 = null;
            if (message.replaceAll("[^A-Za-z]", "").length() >= 8) {
                System.out.print("Enter the second starting letter for characters after the 8th: ");
                startLetter2 = scanner.next().charAt(0);
            }

            String encryptedMessage = encryptMessage(message, startLetter1, startLetter2);
            System.out.println("Encrypted Message: " + encryptedMessage);
            
            String hashedMessage = hashMessage(encryptedMessage);
            System.out.println("SHA-256 Hashed Encrypted Message: " + hashedMessage);
        } 
        else if (choice.equals("D")) {
            System.out.print("Enter the encrypted message to decrypt: ");
            String encryptedMessage = scanner.nextLine();
            System.out.print("Enter the first starting letter: ");
            char startLetter1 = scanner.next().charAt(0);

            // Only ask for second letter if the message has 8 or more characters
            Character startLetter2 = null;
            if (encryptedMessage.replaceAll("[^A-Za-z]", "").length() >= 8) {
                System.out.print("Enter the second starting letter for characters after the 8th: ");
                startLetter2 = scanner.next().charAt(0);
            }

            String decryptedMessage = decryptMessage(encryptedMessage, startLetter1, startLetter2);
            System.out.println("Decrypted Message: " + decryptedMessage);
        } 
        else {
            System.out.println("Invalid choice. Please enter 'E' to encrypt or 'D' to decrypt.");
        }
        
        scanner.close();
    }
}
