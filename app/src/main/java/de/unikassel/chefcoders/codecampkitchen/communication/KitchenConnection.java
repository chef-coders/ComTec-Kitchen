package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.HashMap;
import java.util.Map;

public class KitchenConnection {
    public static final String URL = "http://srv8.comtec.eecs.uni-kassel.de";
    public static final int PORT = 10800;
    public static final String USER_KEY = "QXrnDvfLLy0RdS";
    public static final String ADMIN_KEY = "UkQ4wtmOoWU9Ws";

    private static final String BASE_URL = URL + ":" + PORT + "/api";
    private HttpConnection connection;

    public KitchenConnection(HttpConnection connection) {
        this.connection = connection;
    }

    // Server info
    public String getServerInfo() {
        return connection.get(BASE_URL);
    }

    // Users
    public String getUser(String userToken) {
        return connection.get(BASE_URL + "/users/" + userToken);
    }

    public String getAllUsers() {
        return connection.get(BASE_URL + "/users");
    }

    public String createRegularUser(String userJson) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("key", USER_KEY);
        return connection.post(BASE_URL + "/users", userJson, headers);
    }

    public String createAdminUser(String userJson) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("key", ADMIN_KEY);
        headers.put("Content-Type", "application/json");
        return connection.post(BASE_URL + "/users", userJson, headers);
    }

    public String deleteUser(String userToken) {
        connection.delete(BASE_URL + "/users/" + userToken);
    }

    public String updateUser(String userToken, String userJson) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return connection.put(BASE_URL + "/users/" + userToken, userJson, headers);
    }

    // Purchases
    public String getPurchase(String purchaseToken) {
        return connection.get(BASE_URL + "/purchases/" + purchaseToken);
    }

    public String getAllPurchases() {
        return connection.get(BASE_URL + "/purchases");
    }

    public String getPurchasesForUser(String userToken) {
        return connection.get(BASE_URL + "/purchases/" + userToken);
    }

    public String buyItem(String buyingDataJson) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return connection.post(BASE_URL + "/purchases", buyingDataJson, headers);
    }

    public String deletePurchase(String purchaseToken) {
        connection.delete(BASE_URL + "/purchases/" + purchaseToken);
    }

    public String updatePurchase(String purchaseToken, String purchaseJson) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return connection.put(BASE_URL + "/purchases/" + purchaseToken, purchaseJson, headers);
    }

    // Items
    public String getItem(String itemToken) {
        return connection.get(BASE_URL + "/items/" + itemToken);
    }

    public String getAllItems() {
        return connection.get(BASE_URL + "/items");
    }

    public String createItem(String itemJson) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return connection.post(BASE_URL + "/items", itemJson, headers);
    }

    public String deleteItem(String itemToken) {
        return connection.delete(BASE_URL + "/items/" + itemToken);
    }

    public String updateItem(String itemToken, String itemJson) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return connection.put(BASE_URL + "/items/" + itemToken, itemJson, headers);
    }
}
