import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Print the number of subarrays of given array having negative sums.
 */
public class JavaSubarray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int amount = sc.nextInt();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            values.add(sc.nextInt());
        }

        int sum;
        int count = 0;
        for (int i = 0; i < values.size(); ++i) {
            sum = 0;
            for (int j = i; j < values.size(); ++j) {
                sum += values.get(j);
                if (sum < 0) {
                    ++count;
                }
            }
        }

        System.out.println(count);
    }
}