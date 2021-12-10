import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class JavaBigDecimal {
    public static void main(String[] args) {
        //Input
        Scanner sc = new Scanner(System.in);
        int digitsAmount = sc.nextInt();
        String[] digits = new String[digitsAmount + 2];
        for (int i = 0; i < digitsAmount; i++) {
            digits[i] = sc.next();
        }
        sc.close();

        List<String> values = Arrays.stream(digits)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Comparator<String> comparator = Comparator.comparing(BigDecimal::new);
        values.sort(comparator.reversed());

        digits = values.toArray(new String[0]);

        //Output
        for (int i = 0; i < digitsAmount; i++) {
            System.out.println(digits[i]);
        }
    }
}
