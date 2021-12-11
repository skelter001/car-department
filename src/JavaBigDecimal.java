import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class JavaBigDecimal {
    public static void main(String[] args) {
        //Input
        Scanner sc = new Scanner(System.in);
        int digitsAmount = sc.nextInt();
        List<String> digits = new ArrayList<>();
        for (int i = 0; i < digitsAmount; i++) {
            digits.add(sc.next());
        }
        sc.close();
        Comparator<String> comparator = Comparator.comparing(BigDecimal::new);
        digits.sort(comparator.reversed());

        //Output
        String ans = String.join("\n", digits);
        System.out.println(ans);
    }
}
