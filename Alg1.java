//Implementation of "Alg1 Design a Θ(m ∗ n2) time brute force algorithm for solving Problem1"

import java.util.Scanner;

public class Alg1 {
    public void main() {
        int m, n; // To read number of stocks and values of such stocks for n days
        Scanner sc = new Scanner(System.in);
        m = sc.nextInt();
        n = sc.nextInt();
        int[][] shareMatrix = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                shareMatrix[i][j] = sc.nextInt();   // Reading m * n values from the user
            }

        ComputeMaxShareValue(shareMatrix, m, n); // Calling the algorithm for computing maximum profit

    }


    // Bruteforce algorithm for calculating the maximum profit for one transaction among M stocks
    private void ComputeMaxShareValue(int[][] shareMatrix, int m, int n) {
        int maxSum=0; // For storing previous calculated stock value highest profit
        int buyDay=0; // For storing buy day index (lowest)
        int sellDay=0; // For storing Sell day index (highest)
        int stock=0; // For storing the stock index that yields the highest profit among M stocks
        int tempMax=0; // For storing current stock value highest profit
        for(int i=0;i<m;i++) {          // Outer loop to iterate for M number of stocks
            for (int j = 0; j < n-1;j++) {     // 1st inner loop to iterate Buy Day
                for (int k = j+1; k < n;k++) {   // 2nd inner loop to iterate for each sell day
                    if(tempMax < shareMatrix[i][k] - shareMatrix[i][j])  // If already captured profit is less than current profit
                    {
                        //Updating the buy Day index, sell day Index, stock index and present profit
                        tempMax = shareMatrix[i][k] - shareMatrix[i][j];
                        buyDay = j;
                        sellDay = k;
                        stock = i;
                    }
                }
            }
            if(maxSum < tempMax) // If the profit calculated for nth Stock is more than previous stock then update the profit to max value
              maxSum =  tempMax;
        }
        System.out.println(""+stock+" "+buyDay+" "+sellDay); // Printing the final values

    }
}
