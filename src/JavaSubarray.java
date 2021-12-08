import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Print the number of subarrays of given array having negative sums.
 */
public class JavaSubarray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int amount = sc.nextInt();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < amount; ++i)
            values.add(sc.nextInt());

        int sum;
        int count = 0;
        for (int i = 0; i < values.size(); ++i) {
            sum = 0;
            for (int j = i; j < values.size(); ++j) {
                sum += values.get(j);
                if (sum < 0)
                    ++count;
            }
        }


        // another solution
        /**
         * 5
         * 1 -2 4 -5 1
         */
        long count2 = IntStream.range(0, values.size())
                .mapToLong(start -> IntStream.range(start, values.size())
                        .boxed()
                        .map(end -> values.subList(start, end + 1))
                        .filter(list -> list.stream().mapToInt(i -> i).sum() < 0)
                        .count()
                ).sum();
        System.out.println(count2);
        //

        System.out.println(count);
    }
}
