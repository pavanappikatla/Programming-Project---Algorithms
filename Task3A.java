

// Implementation of Task3a Give a recursive implementation of Alg3 using Memoization.

import java.util.Scanner;

public class Task3A {
     int counter=0;
     int buyDay=0; // To store the index of buyDay (lowest day)
     int sellDay=0; // To store the index for sellDay (highest day)
     int stock=0;  // To store the stock index where maximum profit is achieved
    public void main() {
        int m, n;  // To read number of stocks and values of such stocks for n days
        int totalShareValue = 0; // To store maximum profit
        Scanner sc = new Scanner(System.in);
        m = sc.nextInt();   // Number of stocks
        n = sc.nextInt();   // No of days per each stock


        int[][] shareMatrix = new int[m][n]; // To store the M stocks values on N days

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                shareMatrix[i][j] = sc.nextInt();
            }


        int[][][] cache = new int[m][n][2];

        for (int i = 0; i < m; i++)    // Calling the function to compute maximum share for each stock
        {
            int val = ComputeMaxShareValue(shareMatrix, i, 1, 0, cache);
            if(totalShareValue < val) {  // If previously computed profit is less than current profit from another stock
                stock = i;     // update the maximum stock index
                totalShareValue = val; // update the maximum profit
            }
        }
        System.out.println("Maximum stock achieved in :"+stock);
        int minValue=0;
        int minValueIndex=0;
        int maxValueIndex=0;


        // Back tracking the store cache values to capture the minimum share value
        for(int i=n-1;i>=0;i--)
        {
            if(cache[stock][i][1] == totalShareValue)
            {
                minValue = shareMatrix[stock][i];
                minValueIndex = i;
                break;
            }
        }

        // Back tracking the stored cache values to capture maximum share value
        for(int i=n-1;i>=0;i--) {
            if (cache[stock][i][0] - minValue == totalShareValue) {
                maxValueIndex = i;
                break;
            }
        }

        System.out.println(stock+" "+minValueIndex+" "+maxValueIndex);


    }


    //Main function that computes the maximum share value in a stock for N days through recursive implementation of dynamic programming

    private int ComputeMaxShareValue(int[][] shareMatrix, int mthShare, int buyableFlag, int dayIndex, int[][][] cache) {
        counter++;
        if (dayIndex == shareMatrix[mthShare].length) { // Base case. When the last element in the stock array is reached
            return 0;
        }
        else
        {
            // Looking in the cache (Memoization) whether the value is previously calculated thereby avoiding overlapping subproblems
            if(cache[mthShare][dayIndex][buyableFlag] != 0)
            {
                return cache[mthShare][dayIndex][buyableFlag];
            }
            else
            {
                if (buyableFlag == 1) {  // Each day has two options whether to buy a stock or to not buy that stock. This condition takes care when there is an option to buy.

                    // Maximum value of(Bought(expensed out) on this day + Selling on next day || Simple buy flag transferred to next day)
                    return cache[mthShare][dayIndex][buyableFlag] = Math.max(-shareMatrix[mthShare][dayIndex] + ComputeMaxShareValue(shareMatrix, mthShare, 0, dayIndex + 1,cache), ComputeMaxShareValue(shareMatrix, mthShare, 1, dayIndex + 1,cache));
                } else {   // This else part takes care of the case where a stock is already bought and is looking for an ideal day to sell.

                    // Maximum value of(Sold on this day || Transfer the selling option for next day)
                    return cache[mthShare][dayIndex][buyableFlag] = Math.max(+shareMatrix[mthShare][dayIndex], ComputeMaxShareValue(shareMatrix, mthShare, 0, dayIndex + 1, cache));
                }
            }
        }
    }
}


