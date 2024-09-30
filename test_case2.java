import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class test_case2 {

    public static void main(String[] args) {
        // Input: New JSON-like structure with 9 shares
        Map<String, Object> caseData = buildCaseData();

        // Parse the number of shares (k) and shares themselves
        int k = Integer.parseInt(((Map<String, String>) caseData.get("keys")).get("k"));
        BigInteger[][] shares = parseShares(caseData, k);

        // Perform Lagrange Interpolation to find the secret (f(0))
        BigInteger secret = lagrangeInterpolation(shares);
        System.out.println("Reconstructed Secret: " + secret);
    }

    // Build the input case data for the new test case
    private static Map<String, Object> buildCaseData() {
        Map<String, Object> caseData = new HashMap<>();
        Map<String, String> keys = new HashMap<>();
        keys.put("n", "9");
        keys.put("k", "6");

        caseData.put("keys", keys);
        caseData.put("1", new Share("10", "28735619723837"));
        caseData.put("2", new Share("16", "1A228867F0CA"));
        caseData.put("3", new Share("12", "32811A4AA0B7B"));
        caseData.put("4", new Share("11", "917978721331A"));
        caseData.put("5", new Share("16", "1A22886782E1"));
        caseData.put("6", new Share("10", "28735619654702"));
        caseData.put("7", new Share("14", "71AB5070CC4B"));
        caseData.put("8", new Share("9", "122662581541670"));
        caseData.put("9", new Share("8", "642121030037605"));

        return caseData;
    }

    // Parse the shares from the case data into a 2D array
    private static BigInteger[][] parseShares(Map<String, Object> caseData, int k) {
        BigInteger[][] shares = new BigInteger[k][2]; // Store (x, y) pairs
        
        int index = 0;
        for (String key : caseData.keySet()) {
            if (!key.equals("keys")) {
                Share share = (Share) caseData.get(key);
                shares[index][0] = new BigInteger(key); // x-value
                shares[index][1] = convertToDecimal(share.base, share.value); // y-value
                index++;
                if (index == k) {
                    break; // Only take 'k' shares
                }
            }
        }
        return shares;
    }

    // Convert the given value in any base to decimal using BigInteger
    private static BigInteger convertToDecimal(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return new BigInteger(value, baseInt); // Convert value from the given base to decimal
    }

    // Lagrange interpolation to calculate f(0), which is the secret
    private static BigInteger lagrangeInterpolation(BigInteger[][] shares) {
        BigInteger secret = BigInteger.ZERO;
        
        for (int i = 0; i < shares.length; i++) {
            BigInteger xi = shares[i][0]; // x_i
            BigInteger yi = shares[i][1]; // y_i
            BigInteger li = BigInteger.ONE; // Lagrange coefficient

            // Calculate the Lagrange coefficient for each share
            for (int j = 0; j < shares.length; j++) {
                if (i != j) {
                    BigInteger xj = shares[j][0]; // x_j
                    BigInteger numerator = BigInteger.ZERO.subtract(xj);
                    BigInteger denominator = xi.subtract(xj);
                    li = li.multiply(numerator).divide(denominator); // Compute product for the Lagrange coefficient
                }
            }

            secret = secret.add(yi.multiply(li)); // Sum the terms for the secret (f(0))
        }

        return secret;
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

