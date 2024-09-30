import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Create the input map (Simulating the JSON structure)
        Map<String, Object> caseData = new HashMap<>();
        Map<String, String> keys = new HashMap<>();
        keys.put("n", "4");  // Total number of shares provided
        keys.put("k", "3");  // Minimum number of shares required to reconstruct the secret
        caseData.put("keys", keys);

        // Add share objects to the map
        caseData.put("1", new Share("10", "4"));
        caseData.put("2", new Share("2", "111"));
        caseData.put("3", new Share("10", "12"));
        caseData.put("6", new Share("4", "213"));

        // Extract the necessary number of shares (k = 3)
        int k = Integer.parseInt(keys.get("k"));
        int[][] shares = new int[k][2];  // To store (x, y) pairs for interpolation

        // Populate the shares array with the (x, y) pairs
        shares[0][0] = 1; // x-value
        shares[0][1] = convertToDecimal("10", "4"); // y-value, convert base 10 to decimal

        shares[1][0] = 2; // x-value
        shares[1][1] = convertToDecimal("2", "111"); // y-value, convert base 2 to decimal

        shares[2][0] = 3; // x-value
        shares[2][1] = convertToDecimal("10", "12"); // y-value, convert base 10 to decimal

        // Perform Lagrange Interpolation to reconstruct the secret (constant term c)
        int secret = lagrangeInterpolation(shares);
        System.out.println("Reconstructed Secret: " + secret);
    }

    // Convert the given value in any base to decimal
    private static int convertToDecimal(String base, String value) {
        int baseInt = Integer.parseInt(base);  // Convert the base to an integer
        return Integer.parseInt(value, baseInt);  // Convert value from the given base to decimal
    }

    // Lagrange interpolation to calculate f(0), which is the secret
    private static int lagrangeInterpolation(int[][] shares) {
        double secret = 0;  // To hold the final value of f(0)

        // Loop through each share to calculate the secret using Lagrange's formula
        for (int i = 0; i < shares.length; i++) {
            int xi = shares[i][0];  // x_i (share index)
            int yi = shares[i][1];  // y_i (share value)
            double li = 1.0;  // Initialize Lagrange coefficient for this share

            // Calculate the Lagrange coefficient for this share
            for (int j = 0; j < shares.length; j++) {
                if (i != j) {
                    int xj = shares[j][0];  // x_j (another share index)
                    li *= (0.0 - xj) / (xi - xj);  // Lagrange formula component
                }
            }

            secret += yi * li;  // Sum up the terms for f(0)
        }

        // Return the secret as an integer (since it's the constant term)
        return (int) Math.round(secret);
    }
}

class Share {
    String base;
    String value;

    Share(String base, String value) {
        this.base = base;
        this.value = value;
    }
}
