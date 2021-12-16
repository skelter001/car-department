import java.util.Scanner;

/**
 * Given two strings of lowercase English letters, A and B, perform the following operations:
 *
 * Sum the lengths of A and B
 * Determine if A is lexicographically larger than B
 * Capitalize the first letter in A and B and print them on a single line, separated by a space.
 */

public class JavaStringIntroduction {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String A = sc.next();
        String B = sc.next();
        sc.close();
        System.out.println(A.length() + B.length());
        System.out.println(A.compareTo(B) <= 0 ? "No" : "Yes");
        String finalString =
                A.substring(0, 1).toUpperCase() + A.substring(1) + " " +
                        B.substring(0, 1).toUpperCase() + B.substring(1);
        System.out.println(finalString);
    }
}
