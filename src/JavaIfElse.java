import java.util.Scanner;

/**
 * Given two strings of lowercase English letters, A and B, perform the following operations:
 *
 * Sum the lengths of A and B
 * Determine if A is lexicographically larger than B
 * Capitalize the first letter in A and B and print them on a single line, separated by a space.
 */
public class JavaIfElse {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int digit = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        if (digit % 2 == 1) {
            System.out.println("Weird");
        }
        if (digit % 2 == 0 && digit >= 2 && digit <= 5) {
            System.out.println("Not Weird");
        }
        if (digit % 2 == 0 && digit >= 6 && digit <= 20) {
            System.out.println("Weird");
        }
        if(digit % 2 == 0 && digit > 20) {
            System.out.println("Not Weird");
        }
        scanner.close();
    }
}
