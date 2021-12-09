import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;


public class JavaBigDecimal {
    public static void main(String[] args) {
        //Input
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] s = new String[n + 2];
        for (int i = 0; i < n; i++) {
            s[i] = sc.next();
        }
        sc.close();

        List<String> values = new ArrayList<>(Arrays.asList(s));
        values.removeIf(Objects::isNull);
        values.sort(Comparator.comparingDouble(Double::parseDouble).reversed());

        s = values.toArray(new String[0]);

        //Output
        for (int i = 0; i < n; i++) {
            System.out.println(s[i]);
        }
    }
}
