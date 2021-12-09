import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * You will be given two integers x and y as input, you have to compute x/y.
 * If x and y are not 32-bit signed integers or if y is zero,
 * exception will occur, and you have to report it.
 */
public class JavaExceptionHandling {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int firstDig = sc.nextInt();
            int secondDig = sc.nextInt();
            System.out.println(firstDig / secondDig);
        } catch (InputMismatchException ime) {
            System.out.println(ime.getClass().getCanonicalName());
        } catch (ArithmeticException ae) {
            System.out.println(ae);
        }
    }
}