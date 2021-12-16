import java.util.Arrays;
import java.util.Scanner;


public class JavaAnagrams {
    /**
     *
     * @param a the first string
     * @param b the second string
     * @return If and are case-insensitive anagrams, return true. Otherwise, return false.
     */
    static boolean isAnagram(String a, String b) {
        char[] firstStringChars = a.toLowerCase().toCharArray();
        char[] secondStringChars = b.toLowerCase().toCharArray();
        Arrays.sort(firstStringChars);
        Arrays.sort(secondStringChars);
        return Arrays.equals(firstStringChars, secondStringChars);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String firstString = scan.next();
        String secondString = scan.next();
        scan.close();
        System.out.println( isAnagram(firstString, secondString) ? "Anagrams" : "Not Anagrams" );
    }
}
