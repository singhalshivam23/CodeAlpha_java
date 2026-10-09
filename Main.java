import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static class Stock {
        private final String symbol;
        private final String company;
        private double price;

        Stock(String symbol, String company, double price) {
            this.symbol = symbol;
            this.company = company;
            this.price = price;
        }
        String getSymbol() { return symbol; }
        String getCompany() { return company; }
        double getPrice() { return price; }
        void setPrice(double price) { this.price = price; }
    }

    static class Transaction {
        final String action;
        final String symbol;
        final int quantity;
        final double price;

        Transaction(String action, String symbol, int quantity, double price) {
            this.action = action;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
        }
        public String toString() {
            return action + " " + quantity + " " + symbol + " @ Rs. "
                    + String.format("%.2f", price) + " | Total: Rs. "
                    + String.format("%.2f", price * quantity);
        }
    }

    static final Scanner sc = new Scanner(System.in);
    static final Map<String, Stock> market = new LinkedHashMap<>();
    static final Map<String, Integer> portfolio = new LinkedHashMap<>();
    static final ArrayList<Transaction> history = new ArrayList<>();
    static double cash = 100000.00;

    public static void main(String[] args) {
        market.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3850));
        market.put("INFY", new Stock("INFY", "Infosys", 1750));
        market.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2920));
        market.put("HDFCBANK", new Stock("HDFCBANK", "HDFC Bank", 1680));

        while (true) {
            System.out.println("\\n=== STOCK TRADING SIMULATOR (NOT REAL TRADING) ===");
            System.out.println("Available cash: Rs. " + money(cash));
            System.out.println("1. View market");
            System.out.println("2. Buy shares");
            System.out.println("3. Sell shares");
            System.out.println("4. View portfolio");
            System.out.println("5. View transaction history");
            System.out.println("6. Simulate market price update");
            System.out.println("7. Exit");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> showMarket();
                case 2 -> trade(true);
                case 3 -> trade(false);
                case 4 -> showPortfolio();
                case 5 -> showHistory();
                case 6 -> updatePrices();
                case 7 -> { System.out.println("Goodbye."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void showMarket() {
        System.out.println("\\n--- MARKET ---");
        for (Stock s : market.values())
            System.out.println(s.getSymbol() + " | " + s.getCompany() + " | Rs. " + money(s.getPrice()));
    }

    static void trade(boolean buy) {
        showMarket();
        System.out.print((buy ? "Buy" : "Sell") + " which symbol? ");
        String symbol = sc.nextLine().trim().toUpperCase();
        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Unknown stock symbol.");
            return;
        }
        System.out.print("Quantity: ");
        int quantity = readInt();
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        double total = stock.getPrice() * quantity;
        int owned = portfolio.getOrDefault(symbol, 0);
        if (buy) {
            if (total > cash) {
                System.out.println("Insufficient cash. Required: Rs. " + money(total));
                return;
            }
            cash -= total;
            portfolio.put(symbol, owned + quantity);
            history.add(new Transaction("BUY", symbol, quantity, stock.getPrice()));
            System.out.println("Purchase completed. Remaining cash: Rs. " + money(cash));
        } else {
            if (quantity > owned) {
                System.out.println("You own only " + owned + " shares of " + symbol + ".");
                return;
            }
            cash += total;
            int remaining = owned - quantity;
            if (remaining == 0) portfolio.remove(symbol);
            else portfolio.put(symbol, remaining);
            history.add(new Transaction("SELL", symbol, quantity, stock.getPrice()));
            System.out.println("Sale completed. Cash: Rs. " + money(cash));
        }
    }

    static void showPortfolio() {
        System.out.println("\\n--- PORTFOLIO ---");
        if (portfolio.isEmpty()) {
            System.out.println("No shares owned yet.");
        } else {
            double value = 0;
            for (Map.Entry<String, Integer> item : portfolio.entrySet()) {
                Stock s = market.get(item.getKey());
                double holding = s.getPrice() * item.getValue();
                value += holding;
                System.out.println(item.getKey() + ": " + item.getValue()
                        + " shares | Current value: Rs. " + money(holding));
            }
            System.out.println("Total stock value: Rs. " + money(value));
        }
        System.out.println("Cash balance: Rs. " + money(cash));
        System.out.println("Portfolio value (cash + shares): Rs. "
                + money(cash + currentStockValue()));
    }

    static double currentStockValue() {
        double total = 0;
        for (Map.Entry<String, Integer> item : portfolio.entrySet())
            total += market.get(item.getKey()).getPrice() * item.getValue();
        return total;
    }

    static void showHistory() {
        System.out.println("\\n--- TRANSACTION HISTORY ---");
        if (history.isEmpty()) System.out.println("No transactions yet.");
        else for (Transaction t : history) System.out.println(t);
    }

    static void updatePrices() {
        System.out.println("Enter new simulated prices (Rs.); this is not live market data.");
        for (Stock s : market.values()) {
            System.out.print(s.getSymbol() + " current Rs. " + money(s.getPrice()) + ", new price: ");
            double p = readDouble();
            if (p > 0) s.setPrice(p);
            else System.out.println("Invalid price; keeping previous price.");
        }
        System.out.println("Simulated prices updated.");
    }

    static String money(double amount) { return String.format("%.2f", amount); }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a whole number: ");
            sc.next();
        }
        int v = sc.nextInt(); sc.nextLine(); return v;
    }

    static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        double v = sc.nextDouble(); sc.nextLine(); return v;
    }
}
