//Alg2 Θ(m ∗ n) time greedy algorithm
import java.util.Scanner;

public class Alg2 {
    public void main() {
        int m, n;
        Scanner sc = new Scanner(System.in);
        m = sc.nextInt();  // Number of stocks
        n = sc.nextInt();  // Number of days in each stock
        int[][] shareMatrix = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                shareMatrix[i][j] = sc.nextInt();   // Reading the m stock values for N days from the user
            }
        ComputeMaxShareValue(shareMatrix, m, n);  // The main method where the maximum share value is computed is being called here
    }

    private static void ComputeMaxShareValue(int[][] shareMatrix, int m, int n) {
        int maxSum=0; // maximum profit calculated on the previous stock
        int finalBuyDayIndex=0; // Finally captured buy day index
        int finalSellDayIndex=0; // Finally captured sell day index
        int finalStockIndex=0; // Finally captured stock Index where maximum profit is obtained

        // Same logic in the inner loop is being continued for M different stocks
        for(int i=0;i<m;i++) {
            int tempMinTillNow=shareMatrix[i][0];
            int tempMaxForEachShare=0;
            int buyDay=0;
            int sellDay=0;
            int stock=0;
            for (int j = 1; j < n; j++) {          // Computation of maximum share value in linear time for each share
                    if(tempMinTillNow > shareMatrix[i][j])
                    {

                        // In each share while we are traversing the values per each day,
                        // minimum stock value is being updated whenever we find a further smaller value.
                        tempMinTillNow = shareMatrix[i][j];
                        buyDay = j;
                    }
                    else if(tempMaxForEachShare < shareMatrix[i][j] - tempMinTillNow)
                    {
                        //Similarly for while traversing each day share value,
                        // maximum value is being calculated based on the minimum value found so far
                       tempMaxForEachShare = shareMatrix[i][j] - tempMinTillNow;
                       sellDay = j;
                       stock = i;
                    }
                }
            // Crucial condition to check whether the maximum profit found in the previous share is greater than the current share
         if(maxSum < tempMaxForEachShare) {
             maxSum = tempMaxForEachShare;
             finalBuyDayIndex = buyDay;
             finalSellDayIndex = sellDay;
             finalStockIndex = stock;
         }
        }
        System.out.println(+finalStockIndex+" "+finalBuyDayIndex+" "+finalSellDayIndex);  //finally printing the values as per the requirement

    }

}
