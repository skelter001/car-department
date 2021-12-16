import java.util.Scanner;

/**
 * Each line of the output contains the result n^p, if both n and p are positive.
 * If either n or p is negative, the output contains "n and p should be non-negative".
 * If both n and p are zero, the output contains "n and p should not be zero.".
 */
class MyCalculator {
    public int power(int n, int p) throws Exception {
        if(n == 0 && p == 0)
            throw new Exception("n and p should not be zero.");
        if(n < 0 || p < 0)
            throw new Exception("n or p should not be negative.");
        return (int) Math.pow(n, p);
    }
}

public class JavaExceptionHandling2 {
    public static final MyCalculator my_calculator = new MyCalculator();
    public static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int p = in.nextInt();

            try {
                System.out.println(my_calculator.power(n, p));
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                in.close();
            }
        }
    }
}
