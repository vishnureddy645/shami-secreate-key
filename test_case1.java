import java.util.HashMap;
import java.util.Map;

public class test_case1 {

    public static void main(String[] args) {
        // Input JSON-like structure
        Map<String, Object> caseData = new HashMap<>();
        Map<String, String> keys = new HashMap<>();
        keys.put("n", "4");
        keys.put("k", "3");
        
        caseData.put("keys", keys);
        caseData.put("1", new Share("10", "4"));
        caseData.put("2", new Share("2", "111"));
        caseData.put("3", new Share("10", "12"));
        caseData.put("6", new Share("4", "213"));
        
        // Parse the shares (k = 3)
        int[][] shares = new int[3][2]; // We need 3 shares to reconstruct (x, y) pairs
        
        shares[0][0] = 1; // x-value
        shares[0][1] = convertToDecimal("10", "4"); // y-value (base 10 -> 4)
        
        shares[1][0] = 2; // x-value
        shares[1][1] = convertToDecimal("2", "111"); // y-value (base 2 -> 7)
        
        shares[2][0] = 3; // x-value
        shares[2][1] = convertToDecimal("10", "12"); // y-value (base 10 -> 12)
        
        // Perform Lagrange Interpolation to find the secret (f(0))
        int secret = lagrangeInterpolation(shares);
        System.out.println("Reconstructed Secret: " + secret);
    }

    // Convert the given value in any base to decimal
    private static int convertToDecimal(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return Integer.parseInt(value, baseInt); // Convert value from the given base to decimal
    }

    // Lagrange interpolation to calculate f(0), which is the secret
    private static int lagrangeInterpolation(int[][] shares) {
        double secret = 0;
        
        for (int i = 0; i < shares.length; i++) {
            int xi = shares[i][0]; // x_i
            int yi = shares[i][1]; // y_i
            double li = 1.0; // Lagrange coefficient

            // Calculate the Lagrange coefficient for each share
            for (int j = 0; j < shares.length; j++) {
                if (i != j) {
                    int xj = shares[j][0]; // x_j
                    li *= (0.0 - xj) / (xi - xj); // Compute product for the Lagrange coefficient
                }
            }

            secret += yi * li; // Sum the terms for the secret (f(0))
        }

        // Return the secret as an integer (as it's the constant term)
        return (int) Math.round(secret);
    }
}

// Class to store each share's base and value
class Share {
    String base;
    String value;

    Share(String base, String value) {
        this.base = base;
        this.value = value;
    }
}