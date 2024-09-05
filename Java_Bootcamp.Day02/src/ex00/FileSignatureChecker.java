import java.io.*;
import java.util.*;

public class FileSignatureChecker {

    private final Map<String, String> signatures = new HashMap<>();

    public FileSignatureChecker(String signatureFilePath) throws IOException {
        loadSignatures(signatureFilePath);
    }

    private void loadSignatures(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    signatures.put(parts[1], parts[0]);
                }
            }
        }
    }

    public String checkSignature(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileHeader = new byte[8];
            int read = fis.read(fileHeader);
            String hexHeader = bytesToHex(fileHeader);
            for (String signature : signatures.keySet()) {
                if (hexHeader.startsWith(signature)) {
                    return signatures.get(signature);
                }
            }
        }
        return "UNDEFINED";
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
