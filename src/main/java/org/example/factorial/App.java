package org.example.factorial;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the thread pool: ");
        int poolSize = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                poolSize = Integer.parseInt(scanner.nextLine());
                if (poolSize <= 0) {
                    System.out.print("Please enter a positive integer: ");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a positive integer: ");
            }
        }

        FactorialCalculator calculator = new FactorialCalculator(poolSize);
        calculator.start();

        scanner.close();
    }
}

