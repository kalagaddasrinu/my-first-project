import java.io.*;
import java.util.*;

class BankAccount implements Serializable {
    private String accountHolder;
    private String accountNumber;
    private double balance;

    // Constructor
    public BankAccount(String accountHolder, String accountNumber, double initialBalance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    // Deposit
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    // Withdraw
    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }

    // Check balance
    public void checkBalance() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
    }
}

public class BankManagementSystem {
    private static final String FILE_NAME = "accounts.dat";
    private static ArrayList<BankAccount> accounts = new ArrayList<>();

    // Save accounts to file
    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Load accounts from file
    private static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (ArrayList<BankAccount>) ois.readObject();
        } catch (Exception e) {
            accounts = new ArrayList<>(); // If file not found or empty
        }
    }

    // Find account by number
    private static BankAccount findAccount(String accNo) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) {
                return acc;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadAccounts(); // Load accounts at program start
        int choice;

        do {
            System.out.println("\n--- BANK MENU ---");
            System.out.println("1. Create New Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Create account
                    System.out.print("Enter Account Holder Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();

                    if (findAccount(accNo) != null) {
                        System.out.println("Account already exists!");
                        break;
                    }

                    System.out.print("Enter Initial Balance: ");
                    double balance = sc.nextDouble();
                    accounts.add(new BankAccount(name, accNo, balance));
                    System.out.println("Account created successfully!");
                    saveAccounts();
                    break;

                case 2: // Deposit
                    System.out.print("Enter Account Number: ");
                    accNo = sc.nextLine();
                    BankAccount depositAcc = findAccount(accNo);
                    if (depositAcc != null) {
                        System.out.print("Enter deposit amount: ");
                        double amount = sc.nextDouble();
                        depositAcc.deposit(amount);
                        saveAccounts();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 3: // Withdraw
                    System.out.print("Enter Account Number: ");
                    accNo = sc.nextLine();
                    BankAccount withdrawAcc = findAccount(accNo);
                    if (withdrawAcc != null) {
                        System.out.print("Enter withdraw amount: ");
                        double amount = sc.nextDouble();
                        withdrawAcc.withdraw(amount);
                        saveAccounts();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 4: // Check balance
                    System.out.print("Enter Account Number: ");
                    accNo = sc.nextLine();
                    BankAccount checkAcc = findAccount(accNo);
                    if (checkAcc != null) {
                        checkAcc.checkBalance();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 5:
                    System.out.println("Exiting... Accounts saved.");
                    saveAccounts();
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }
}
