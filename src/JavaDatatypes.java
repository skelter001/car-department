import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * For each input variable and appropriate primitive,
 * you must determine if the given primitives are capable of storing it.
 */
class JavaDatatypes {
    public static void main(String[] argh) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            try {
                long x = sc.nextLong();
                System.out.println(x + " can be fitted in:");
                if (x >= Byte.MIN_VALUE && x <= Byte.MAX_VALUE)
                    System.out.println("* byte");
                if (x >= Short.MIN_VALUE && x <= Short.MAX_VALUE)
                    System.out.println("* short");
                if (x >= Integer.MIN_VALUE && x <= Integer.MAX_VALUE)
                    System.out.println("* int");
                System.out.println("* long");
            } catch (InputMismatchException ime) {
                System.out.println(sc.next() + " can't be fitted anywhere.");
            }
        }
        sc.close();
    }
}



