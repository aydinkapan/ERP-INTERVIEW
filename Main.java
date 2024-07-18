import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class Main {

    public static void main(String[] args) {
        List<Order> orders = Arrays.asList(
                new Order(1000, 2000, 12, 100.51),
                new Order(1000, 2001, 31, 200),
                new Order(1000, 2002, 22, 150.86),
                new Order(1000, 2003, 41, 250),
                new Order(1000, 2004, 55, 244),
                new Order(1001, 2001, 88, 44.531),
                new Order(1001, 2002, 121, 88.11),
                new Order(1001, 2004, 74, 211),
                new Order(1001, 2002, 14, 88.11),
                new Order(1002, 2003, 2, 12.1),
                new Order(1002, 2004, 3, 22.3),
                new Order(1002, 2003, 8, 12.1),
                new Order(1002, 2002, 16, 94),
                new Order(1002, 2005, 9, 44.1),
                new Order(1002, 2006, 19, 90)
        );

        printTotalAmount(orders);
        printAveragePrice(orders);
        printAveragePricePerItem(orders);
        printItemOrderQuantities(orders);
        performGetRequest();
        performPostRequest();
    }

    public static void printTotalAmount(List<Order> orders) {
        double totalAmount = orders.stream().mapToDouble(Order::getTotalPrice).sum();
        System.out.println();
        System.out.println("Answer A -->");
        System.out.println();
        System.out.println("Total amount for all orders: " + totalAmount);
    }

    public static void printAveragePrice(List<Order> orders) {
        double averagePrice = orders.stream().mapToDouble(Order::getTotalPrice).average().orElse(0.0);
        System.out.println();
        System.out.println("Answer B -->");
        System.out.println();

        System.out.println("Average price for all items: " + averagePrice);
    }

    public static void printAveragePricePerItem(List<Order> orders) {
        Map<Integer, Double> averagePricePerItem = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.itemId,
                        Collectors.averagingDouble(o -> o.unitPrice)
                ));
        System.out.println();
        System.out.println("Answer C -->");
        System.out.println();
        averagePricePerItem.forEach((itemId, avgPrice) ->
                System.out.println("Item ID: " + itemId + ", Average Price: " + avgPrice));
    }

    public static void printItemOrderQuantities(List<Order> orders) {
        Map<Integer, Map<Integer, Integer>> itemOrderQuantities = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.itemId,
                        Collectors.groupingBy(
                                o -> o.orderId,
                                Collectors.summingInt(o -> o.quantity)
                        )
                ));
        System.out.println();
        System.out.println("Answer D -->");
        System.out.println();
        itemOrderQuantities.forEach((itemId, ordersMap) -> {
            System.out.println("Item ID: " + itemId);
            ordersMap.forEach((orderId, quantity) ->
                    System.out.println("  Order ID: " + orderId + ", Quantity: " + quantity));
        });
    }

    public static void performGetRequest() {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            System.out.println();
            System.out.println("GET Response Code : " + responseCode);
            System.out.println();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("GET Response: " + response);
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void performPostRequest() {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println();
            System.out.println("POST Response Code : " + responseCode);
            System.out.println();

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine.trim());
                }
                in.close();

                System.out.println("POST Response: " + response);
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
