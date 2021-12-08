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
        java.util.Arrays.sort(firstStringChars);
        java.util.Arrays.sort(secondStringChars);
        return java.util.Arrays.equals(firstStringChars, secondStringChars);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        String b = scan.next();
        scan.close();
        boolean ret = isAnagram(a, b);
        System.out.println( (ret) ? "Anagrams" : "Not Anagrams" );
    }
}
