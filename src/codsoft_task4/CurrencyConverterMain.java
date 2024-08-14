package codsoft_task4;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterMain {
     final String apiUrl = "https://api.exchangerate-api.com/v4/latest/";

    // Map for currency details
    private static final Map<String, String> CURRENCY_NAMES = new HashMap<>();
    private static final Map<String, String> CURRENCY_SYMBOLS = new HashMap<>();
    
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
    }

    public double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        URL url = new URL(apiUrl + baseCurrency);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Failed to get exchange rates. HTTP response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract the exchange rate from the raw response
        String responseStr = response.toString();
        String searchStr = "\"" + targetCurrency + "\":";
        int startIndex = responseStr.indexOf(searchStr);
        if (startIndex == -1) {
            throw new Exception("Currency " + targetCurrency + " not found in the response.");
        }

        startIndex += searchStr.length();
        int endIndex = responseStr.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = responseStr.indexOf("}", startIndex);
        }

        String rateStr = responseStr.substring(startIndex, endIndex);
        return Double.parseDouble(rateStr);
    }

    public double convertAmount(double amount, String baseCurrency, String targetCurrency) throws Exception {
        if (!CURRENCY_NAMES.containsKey(baseCurrency) || !CURRENCY_NAMES.containsKey(targetCurrency)) {
            throw new Exception("Currency not supported.");
        }

        double rate = getExchangeRate(baseCurrency, targetCurrency);
        return amount * rate;
    }
    
    public String getCurrencyName(String currencyCode) {
        return CURRENCY_NAMES.getOrDefault(currencyCode, "Unknown Currency");
    }

    public String getCurrencySymbol(String currencyCode) {
        return CURRENCY_SYMBOLS.getOrDefault(currencyCode, "Unknown Symbol");
    }
}
