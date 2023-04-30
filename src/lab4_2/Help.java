package lab4_2;

public class Help {
    public static int nearestDivisor(int smallest, int dividend) {
        int divisor = 0;
        if(smallest <= dividend) {
            divisor = smallest;
            while (dividend % divisor != 0) {
                divisor++;
            }
        } else {
            divisor = dividend;
        }
        return divisor;
    }
}