import java.util.Scanner;

/**
 * Given an integer, n, perform the following conditional actions:
 * If n is odd, print Weird
 * If n is even and in the inclusive range of 6 to 20, print Not Weird
 * If n is even and in the inclusive range of 6 to 20, print Weird
 * If n is even and greater than 20, print Not Weird
 */
public class JavaIfElse {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int digit = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        if (digit % 2 == 1) {
            System.out.println("Weird");
        }
        else if (digit % 2 == 0 && digit >= 2 && digit <= 5) {
            System.out.println("Not Weird");
        }
        else if (digit % 2 == 0 && digit >= 6 && digit <= 20) {
            System.out.println("Weird");
        }
        else if(digit % 2 == 0 && digit > 20) {
            System.out.println("Not Weird");
        }
        scanner.close();
    }
}
