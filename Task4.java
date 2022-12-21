import java.util.Scanner;

public class Task4 {

    static int counter=0;
    public static void main(String[] args) {

        int m, n;
        int totalShareValue = 0;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the size (Number of shares and No of stock values in each share separated by a space):");
        m = sc.nextInt();
        n = sc.nextInt();
        System.out.println("M value: " + m + ", N value:" + n);
        //int[][] cache = new int[n][2];

        System.out.println("Enter the stock values " + n + " values in each of " + m + " lines:");
        int[][] shareMatrix = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                shareMatrix[i][j] = sc.nextInt();
            }

        /*for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(shareMatrix[i][j] + " ");
            }
            System.out.println("\n");
        }*/
        long startTime = System.nanoTime();
        int[][] startEndDays = new int[m][2];

            for(int j=0;j<n-1;j++)
            {
                for(int k=j+1;k<n;k++)
                {
                    int tempProfit=0;
                    for (int i = 0; i < m; i++)
                    {

                        if((shareMatrix[i][j] < shareMatrix[i][k]) && ((j >= startEndDays[i][1] && k >= startEndDays[i][0]) ||(j <= startEndDays[i][0] && k < startEndDays[i][1])))
                        {
                            if(tempProfit < shareMatrix[i][k] - shareMatrix[i][j])
                            {
                                tempProfit = shareMatrix[i][k] - shareMatrix[i][j];
                                startEndDays[i][0] = j;
                                startEndDays[i][1]=k;
                            }
                        }
                    }
                    totalShareValue += tempProfit;
                }
            }

        System.out.println("Total Computed Share Value: " + totalShareValue);
        System.out.println("Counter: "+counter);
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time to in Milli seconds: "
                + elapsedTime / 1000000);


    }
}
