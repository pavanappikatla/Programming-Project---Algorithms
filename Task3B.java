//
//
//
//Task3b Iterative BottomUp implementation of Alg3

import java.util.Scanner;

public class Task3B {
    public void main() {   //Task3b Iterative BottomUp implementation of Alg3

        int m, n;
        int totalShareValue = 0;  // The maximum profit is recorded in this
        Scanner sc = new Scanner(System.in);

        m = sc.nextInt();  // Number of stocks
        n = sc.nextInt();  // Number of days the stocks are recorded

        int[][] shareMatrix = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                shareMatrix[i][j] = sc.nextInt();   // Reading the values of entire stocks from the user
            }

        int[][] profit = new int[m][n];   // Two-dimensional array to record the profits at various buy days and sell days through bottom up
        int maxProfitTillNow = 0;
        int maxStockIndex=0;
        int[] minvalue=new int[m];     // The minimum day obtained in each stock is shared in this array to help us easily identify while backtracking
        int overAllMin=0;

        for (int i = 0; i < m; i++){ // The inner for loop is being iterated for M different stocks
            minvalue[i] = shareMatrix[i][0];  // Minimum value is being started with first day value
            profit[i][0] = 0; // profit is initialised as zero

            for(int j=1;j<n;j++){
                // profit is being updated based on the minimum value found so far and current day value
                profit[i][j] = Math.max(shareMatrix[i][j]-minvalue[i],profit[i][j-1]);
                minvalue[i] = Math.min(minvalue[i],shareMatrix[i][j]); // minimum value is updated for the current stock after comparison with previous min value noted
            }

        }

        // Capturing the maximum profit obtained in the last column of the two-dimensional profit array
        for(int i=0;i<m;i++)
        {
            if(maxProfitTillNow < profit[i][n-1])
            {
                maxProfitTillNow = profit[i][n-1];
                maxStockIndex = i;  // The stock index where the maximum profit is obtained is captured here
                overAllMin = minvalue[maxStockIndex];  // Also the minimum value where profit is obtained is updated
            }
        }

        int maxValueIndex=0;
        int minValueIndex=0;

        // Using the values captured above, now the indexes of maximum and minimum stock values which led to the profit is captured here.
        for(int i=n-1,j=0,firstMaxIndFound=0;i>=0 && j<n;i--,j++)
        {
            if(firstMaxIndFound == 0)
            if(shareMatrix[maxStockIndex][i] - overAllMin == maxProfitTillNow)
            {
                maxValueIndex = i;
                firstMaxIndFound = 1;
            }
            if(shareMatrix[maxStockIndex][j] == overAllMin)
            {
                minValueIndex = j;
            }
        }

        System.out.println(maxStockIndex+" "+minValueIndex+" "+maxValueIndex); // Finally the values are being printed.

    }
}
