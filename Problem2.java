import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Problem2 {

    int subProblems = 0;
    //code with o(m * n^2*k)

    //code with o(m * n^2*k)
    public int algo5(int[][] arr, int n, int k, int stocks) {
        // array to store the maximum profit achievable with k transactions across m stocks for n days
        int[][] dp = new int[k + 1][n + 1];
        // array to store the transaction combination
        String[][] store = new String[k+1][n+1];

        //initialising the arrays
        for (int j = 0; j <= n; j++) {
            store[0][j] = "";
            dp[0][j] = 0;
        }

        for (int i = 0; i <= k; i++) {
            store[i][0] = "";
            dp[i][0] = 0;
        }

        for (int i = 1; i <= k; i++) {

            for(int v = 0; v < stocks; v++) {
                for (int j = 1; j < n; j++) {
                    // maintain the maximum profit possible for the day across v stocks
                    int maxFactor = 0;
                    // maintain the transaction combination associated with the profit
                    String comb = "";

                    for (int m = 0; m < j; m++) {
                        int t = arr[v][j] - arr[v][m] + dp[i - 1][m];
                        if(t > maxFactor)
                            comb = String.format("%s %s %s", v, m, j) + ((store[i-1][m] != "")?","+store[i-1][m]:"" );
                        // update the max profit if you find a transaction greater than it
                        maxFactor = Math.max(maxFactor, t);
                    }

                    if(dp[i][j-1] > maxFactor)
                        comb = store[i][j-1];
                    // if the transaction is less than the max profit obtained by the previous day
                    int temp = Math.max(dp[i][j - 1], maxFactor);
                    if(temp < dp[i][j])
                        comb = store[i][j];
                    // if the existing max profit and current max profit will be compared and the max value will only be maintained
                    dp[i][j] = Math.max(temp, dp[i][j]);
                    store[i][j] = comb;
                }

            }
        }
        System.out.println(dp[k][n-1] + "  " + String.join("\n", store[k][n-1].split(",")));
        return dp[k][n - 1];
    }

    //code with o(m * n * k) runtime complexity same solution as above instead of running the inner loop which computes the max_factor everytime

    public int algo6BottomUp(int[][] arr, int n, int k, int stocks) {
        // array to store the transaction combination
        String store[][] = new String[k+1][n+1];
        // array to store the maximum profit achievable with k transactions across m stocks for n days
        int dp[][] = new int[k + 1][n + 1];
        //initialising the arrays
        for (int j = 0; j <= n; j++) {
            store[0][j] = "";
            dp[0][j] = 0;
        }
        for (int i = 0; i <= k; i++) {
            dp[i][0] = 0;
            store[i][0] = "";
        }
        for (int i = 1; i <= k; i++) {
            for (int v = 0; v < stocks; v++) {
                int maxFactor = Integer.MIN_VALUE;

                String comb = "";
                //maintains index of buy day which bore maximum profit
                int maxIndex = -1;
                //maintains transaction details for associated max profit
                String maxComb = "";
                for (int j = 1; j < n; j++) {
                    int t = dp[i - 1][j - 1] - arr[v][j - 1];
                    if(t > maxFactor) {
                        //j index which yields the maximum profit is maintained
                        maxIndex = j-1;
                        // transactions corresponding to the j is maintained
                        maxComb = store[i-1][j-1];
                    }

                    comb = String.format("%s %s %s", v, maxIndex, j) + ((maxComb!="")?(","+maxComb):"") ;

                    maxFactor = Math.max(maxFactor, t);
                    if(arr[v][j] + maxFactor < dp[i][j-1]){
                        comb = store[i][j-1];
                    }
                    // comparison with the previous day with i transaction
                    int temp = Math.max(dp[i][j - 1], arr[v][j] + maxFactor);
                    if(dp[i][j] > temp){
                        comb = store[i][j];
                    }
                    //comparison with the existing profit for i transaction and at the jth day
                    dp[i][j] = Math.max(dp[i][j], temp);
                    store[i][j] = comb;
                }
            }
        }
        System.out.println(dp[k][n-1] + "  " + String.join("\n", store[k][n-1].split(",")));
        return dp[k][n - 1];
    }



    // TDR is a custom datatype which contains the integer value as well the String which maintains the combinations of transactions which yielded the profit
    public TDR findMaxProfitAlgo4(int[][] prices, int[][] max, int[][] mToS, int n, int k, int stock) throws CloneNotSupportedException {
        // if k is 0 then return 0 profit
        if(k == 0)
            return new TDR();
        // initialising empty profit variable
        TDR profit = new TDR();
        // iterating through n days keeping each day as sell day
        for(int i = 1; i <= n; i++) {
            // this variable holds the buy and sell day and the stock information for the two pair i and j
            String comb = "";
            for (int j = i - 1; j >= 0; j--) {

                TDR temp = new TDR();
                // profit achieved by having i as sell day and j as buy day
                int t = prices[stock][i] - prices[stock][j];
                // if the profit is greater than the max maintained between i and j update
                if (t > max[j][i]) {
                    max[j][i] = t;
                    mToS[j][i] = stock;
                }
                if(max[j][i] > 0)
                    comb = String.format("%s %s %s", mToS[j][i], j, i);
                // find the profit achieved from j day with k - 1 transactions
                TDR sub = (TDR) findMaxProfitAlgo4(prices, max, mToS, j, k - 1, stock).clone();
                // add those profit
                temp.profit += sub.profit + max[j][i];
                //combinations is also maintained in parallel w.r.t to the profit
                temp.comb = comb + ((sub.comb != "")?( ((comb == "")?"":",") + sub.comb):"");
                // return the max of it
                profit = (TDR)Max.compare(temp, profit).clone();
            }
        }
        return profit;
    }

    public void algo4(int[][] prices, int k) throws CloneNotSupportedException {
        TDR profit = new TDR();
        //variable mToS contains the stock which has a maximum value between any two days i and j
        int[][] mToS = new int[prices[0].length][prices[0].length];
        // variable max that contains the maximum transaction value between two days i and j
        int[][] max = new int[prices[0].length][prices[0].length];
        for(int i = 0; i < prices.length; i++){
            // this function will return the maximum profit possible till the stock i
            profit = Max.compare(profit, findMaxProfitAlgo4(prices, max, mToS, prices[0].length-1, k, i));
        }

        System.out.println(profit.profit + " " + String.join("\n", profit.comb.split(",")));

    }

    public TDR maximizeProfitAlgo6TopDown(int[][] arr, int i, int k, boolean buy, HashMap<String, TDR> memo, int stock, int buyIndex, int prevStock) throws CloneNotSupportedException {
        //base condition
        if (i >= arr[0].length || k == 0 || stock >= arr.length || stock < 0) return new TDR();
        String key = String.format("%s-%s-%s-%s", i, k, buy, stock);
        // if the key is present in the DP table don't process
        if(!memo.containsKey(key)) {

            subProblems++;
            //skip the day
            TDR profit = this.maximizeProfitAlgo6TopDown(arr, i + 1, k, buy, memo, stock, buyIndex,-1);
            if (buy) {
                //sell at the ith day
                TDR ps = this.maximizeProfitAlgo6TopDown(arr, i, k - 1, false, memo, stock, -1,-1);
                //check if we can maximize the profit by buying the stock at the ith day of stock+1 stock
                TDR pu = this.maximizeProfitAlgo6TopDown(arr, i, k - 1, false, memo, stock + 1, -1,-1);
                //check if we can maximize the profit by buying the stock at the ith day of stock-1 stock
                TDR pd = this.maximizeProfitAlgo6TopDown(arr, i, k - 1, false, memo, stock - 1, -1,-1);
                //update the max value accordingly out of the three possible values
                TDR temp  = Max.compare(Max.compare(ps, pu), pd);
                //update the profit
                temp.profit = temp.profit + arr[stock][i];
                //update the sell day to backtrack
                temp.sellDay = i ;
                //compare it with global profit for that subproblem
                profit = (TDR)Max.compare(profit, temp).clone();

            } else {
                // find the profit when buying the stock at the ith day for stock
                TDR ps = this.maximizeProfitAlgo6TopDown(arr, i + 1, k, true, memo, stock, i,-1);
                TDR pu = new TDR();
                TDR pd = new TDR();

                //prevStock is used to prevent stackoverflow error, find the profit on buying the stock at stock+1th day
                if(prevStock != stock + 1)
                    pu = this.maximizeProfitAlgo6TopDown(arr, i, k, false, memo, stock + 1, -1,-1);
                //prevStock is used to prevent stackoverflow error, find the profit on buying the stock at stock-1th day
                if(prevStock != stock - 1)
                    pd = this.maximizeProfitAlgo6TopDown(arr, i, k, false, memo, stock-1, -1, stock);

                //find the max among the profit
                ps.profit = ps.profit - arr[stock][i];
                //append the transaction details to the string comb maintained in the class for printing the transaction values finally
                ps.comb = String.format("%s %s %s", stock, i, ps.sellDay) + ((ps.comb != "")?(","+ps.comb):"");
                profit = Max.compare(profit, ps);
                profit = Max.compare(profit, pu);
                profit = Max.compare(profit, pd);
            }
            memo.put(key, profit);
        }

        return (TDR)memo.get(key).clone();
    }


    public void main(String algCode) throws CloneNotSupportedException {

        int m, n,k; // To read number of stocks and values of such stocks for n days
        Scanner sc = new Scanner(System.in);
        k = sc.nextInt();
        System.out.print("Enter the size (Number of shares and No of stock values in each share separated by a space):");
        m = sc.nextInt();
        n = sc.nextInt();
        System.out.println("M value: " + m + ", N value:" + n);

        System.out.println("Enter the stock values " + n + " values in each of " + m + " lines:");
        int[][] arr = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                arr[i][j] = sc.nextInt();   // Reading m * n values from the user
            }

        int[][] max = new int[n][n];
        String[][] store = new String[n][n];

        long startTime = System.currentTimeMillis();

        switch (algCode)
        {
            case "4":
                algo4(arr,k);
                break;
            case "5":
                algo5(arr,n,k,m);
                break;
            case "6a":
            case "6A":
                TDR output = maximizeProfitAlgo6TopDown(arr,0,k,false,new HashMap<String, TDR>(),0, 0, -1);
                System.out.println(String.join("\n",  output.comb.split(",")));
                break;
            case "6b":
            case "6B":
                algo6BottomUp(arr,n,k,m);
                break;
            case "7":
                algo7(arr, k);
                break;
            case "8":
                Arrays.stream(store).forEach(a -> Arrays.fill(a, ""));
                algo8(arr,k,max,store);
                break;
            case "9a":
            case "9A":
                TDR output1 = algo9TopDown(arr,0,k,false,new HashMap<String,TDR>(),0,-1,-1);
                System.out.println(String.join("\n",  output1.comb.split(",")));
                break;
            case "9b":
            case "9B":
                algo9BottomUp(arr,k);
                break;

        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Total execution time to in Milli seconds: "
                + elapsedTime);



        //int arr[][] = {{10,4,6,0,11,4,14,11,15,3,13,14,6,2,7,5,5,13,0,0,8,1,15,8,12,9,12,11,15,1,11,0,14,8,0,6,10,11,4,6,12,4,6,12,4,4,3,3,6,1,15,11,8,11,2,10,1,7,8,7,13,13,8,4,13,10,3,3,11,7,10,5,15,15,10,10,2,13,9,9,9,14,12,3,0,0,9,14,15,0,4,8,14,1,1,13,0,10,12,5},{10,6,6,8,2,9,15,13,8,9,1,7,12,15,11,4,7,3,10,3,2,9,1,1,15,13,3,10,15,2,2,4,9,7,10,11,14,7,2,9,15,2,12,11,10,10,11,15,7,6,5,7,13,10,10,1,9,10,8,15,1,15,6,14,12,6,0,8,3,4,15,1,10,12,11,4,9,14,0,10,9,15,7,10,7,12,7,4,9,2,12,13,4,1,9,6,10,15,14,11},{8,13,1,8,13,1,15,14,2,3,9,3,5,14,3,13,12,10,8,14,12,5,4,10,7,8,3,7,9,15,8,12,9,12,2,7,3,8,6,1,5,4,8,14,8,9,8,13,9,3,11,9,2,14,4,9,5,2,0,10,13,12,4,0,12,8,7,0,2,14,4,3,3,10,15,7,0,1,8,6,10,5,7,0,7,11,3,7,8,0,11,0,12,1,10,14,14,13,8,3},{8,15,3,5,6,8,0,0,2,11,0,9,3,8,6,7,4,14,4,15,15,2,9,7,10,9,1,2,15,9,3,9,8,13,13,10,13,13,15,11,15,15,0,6,14,15,12,10,3,10,0,10,12,14,9,8,3,3,0,9,15,8,14,0,12,2,14,7,11,7,2,1,12,13,7,14,8,13,7,1,9,5,8,14,8,14,0,7,12,6,6,9,15,2,7,9,15,12,6,7},{9,4,9,0,9,3,0,10,3,14,11,10,11,6,7,0,9,11,11,4,2,15,3,3,2,14,11,2,2,1,9,14,6,4,7,2,2,0,3,5,15,2,1,4,15,7,12,7,4,0,10,14,11,6,11,1,0,11,14,14,0,5,9,11,15,7,9,1,1,5,13,1,0,15,3,14,6,4,14,6,6,2,3,8,1,10,1,0,3,2,2,4,14,9,3,10,6,3,6,5},{6,5,9,7,10,12,8,9,5,10,14,6,0,0,14,0,3,10,11,14,6,1,10,9,8,5,9,14,8,10,11,15,14,5,12,15,1,12,2,13,9,3,1,12,9,10,11,4,3,4,14,2,1,11,10,4,2,1,2,6,9,14,0,6,10,10,11,12,7,10,15,0,4,12,7,0,2,5,1,12,1,4,11,12,9,14,12,6,13,7,7,4,2,5,1,7,15,14,7,15},{7,10,10,2,14,5,12,6,8,6,2,3,5,9,9,0,2,13,11,8,14,5,4,12,1,12,12,15,5,13,4,9,4,11,8,6,9,6,3,1,13,15,15,2,2,14,12,1,6,2,7,11,4,15,6,6,3,0,2,13,7,12,1,1,3,15,7,3,12,7,0,9,8,5,9,9,14,11,13,2,6,11,10,13,5,2,9,7,6,13,3,13,0,5,9,5,10,7,13,2},{15,7,9,14,7,1,8,0,8,2,13,3,13,8,10,4,5,10,0,5,3,14,5,12,12,2,13,12,13,11,12,14,13,14,2,5,5,14,3,11,14,8,0,3,4,10,14,1,2,14,2,11,3,8,1,12,13,4,11,7,11,0,0,1,0,4,7,10,2,9,14,3,6,12,4,2,14,1,3,1,5,1,9,9,9,10,4,5,13,10,4,4,6,0,0,6,5,6,10,6},{0,1,13,3,13,3,14,3,2,12,1,13,15,14,1,9,7,7,15,15,10,9,10,12,5,11,14,5,5,7,4,10,6,5,4,9,1,13,2,0,1,15,15,5,3,14,15,4,1,0,14,15,9,5,10,8,11,5,7,8,3,4,11,10,10,9,15,9,6,2,5,14,11,5,14,12,15,14,1,5,0,3,3,5,5,9,12,1,1,3,6,8,8,3,2,7,13,10,8,4},{11,8,1,10,4,4,10,0,15,4,9,0,8,8,11,14,4,4,3,0,5,14,5,8,0,5,8,10,9,1,2,3,14,11,13,12,1,1,5,10,8,9,15,14,15,11,9,12,0,4,5,3,1,5,3,11,5,7,15,0,5,5,5,11,1,14,11,3,2,11,11,14,15,5,11,8,5,5,6,10,12,0,10,3,0,12,12,6,15,7,10,5,4,11,11,1,15,15,2,9},{2,10,13,15,14,15,3,14,6,12,6,13,2,3,13,9,12,11,11,7,10,13,4,6,11,10,3,15,11,15,11,0,5,13,8,8,2,3,7,5,2,11,14,10,1,0,12,12,11,2,9,12,4,9,5,7,8,14,14,12,7,5,1,9,11,5,4,5,12,11,4,7,6,13,1,12,10,11,3,13,1,5,12,1,9,6,7,8,2,8,6,4,2,10,15,13,7,15,2,1},{11,1,9,12,11,12,5,4,13,8,8,2,12,14,3,3,11,14,5,8,5,13,4,8,3,0,12,2,0,7,6,9,9,8,0,3,13,6,12,10,14,2,13,2,11,6,9,9,5,0,5,7,5,3,14,15,9,2,6,0,12,8,14,0,10,2,15,3,8,4,0,13,5,6,12,2,8,3,15,9,15,5,9,1,1,1,11,13,8,6,11,1,13,1,3,4,9,5,14,0},{15,1,0,9,15,12,15,13,14,15,3,2,6,6,6,10,15,4,10,6,5,14,6,10,10,12,2,0,6,15,6,13,4,6,3,9,8,13,12,7,11,1,8,12,12,9,1,13,5,11,8,12,3,12,12,13,15,11,12,0,1,10,1,0,11,5,3,8,13,2,10,1,10,9,5,0,5,4,10,12,6,1,13,8,9,7,10,13,7,11,6,6,12,7,6,0,12,13,6,15},{3,4,14,10,11,8,5,6,12,8,14,7,4,15,1,9,1,4,11,14,12,5,3,6,3,2,1,9,2,1,4,15,8,10,6,10,10,11,14,14,8,12,7,1,13,2,10,9,4,10,15,7,13,2,11,9,14,0,10,1,6,1,10,9,4,8,13,15,15,13,14,11,10,3,6,3,11,1,6,14,2,9,11,13,11,7,8,0,2,12,9,15,0,2,3,7,7,9,11,8},{15,15,15,12,3,6,2,13,2,13,5,9,13,0,0,2,8,7,14,5,13,5,7,0,12,7,3,13,13,1,1,6,7,7,6,6,6,12,5,1,14,10,0,11,10,3,14,14,9,12,0,10,13,0,3,14,9,2,13,10,15,7,7,11,13,1,10,1,13,1,15,2,14,7,10,0,12,8,0,2,9,11,8,9,6,13,2,12,11,10,4,8,14,3,2,15,7,4,14,2},{0,0,11,13,10,12,11,15,5,12,11,7,8,0,7,9,5,7,15,11,1,11,1,11,8,10,12,2,3,15,5,15,11,14,11,8,14,14,15,6,14,10,3,0,15,11,12,7,0,2,13,6,11,15,7,7,4,14,5,3,7,10,3,12,7,9,3,5,15,1,12,13,8,3,3,11,12,7,6,15,2,1,8,4,9,5,15,12,15,0,5,0,1,1,10,3,11,14,12,0},{7,4,5,0,13,5,6,13,13,5,9,11,4,10,15,9,9,12,2,5,10,7,13,11,15,3,11,11,11,15,15,4,12,13,11,15,12,11,0,4,7,0,1,9,9,8,3,1,4,1,0,7,12,6,3,7,3,3,1,0,7,13,0,4,5,0,3,5,3,9,13,8,8,12,1,1,12,9,3,11,1,9,5,5,12,12,2,9,15,14,3,12,10,5,4,6,10,10,6,9},{8,2,0,15,1,0,5,15,6,6,2,2,5,8,14,12,15,8,1,0,7,9,3,1,12,1,5,15,14,14,5,12,1,8,11,5,10,14,8,10,3,13,7,11,8,11,7,6,7,3,4,14,12,12,7,12,0,13,13,5,5,0,5,5,1,6,14,1,5,14,0,3,6,15,7,15,9,6,11,14,8,15,0,8,3,3,12,0,4,6,3,8,0,15,11,2,7,1,11,7},{9,5,15,15,14,12,0,8,6,3,15,1,7,1,0,10,8,6,3,2,6,10,2,2,14,11,7,10,2,15,2,14,1,5,8,1,12,12,3,7,7,12,11,3,8,3,8,3,7,14,2,10,10,3,14,8,15,15,3,4,15,2,10,15,15,2,8,2,13,2,15,3,2,15,9,2,0,7,10,12,9,5,5,8,2,11,11,3,15,15,9,3,15,2,7,15,4,14,15,10},{1,14,14,7,8,5,12,5,4,4,2,8,0,11,4,15,12,10,2,9,6,2,7,2,0,15,9,8,14,5,8,9,7,5,11,9,15,8,11,9,5,7,3,14,10,6,15,14,9,6,1,9,4,13,2,15,5,6,12,2,4,1,14,13,11,11,15,14,7,6,6,13,10,8,9,9,5,1,11,11,9,12,4,0,1,9,15,3,3,12,8,1,8,2,7,0,12,1,10,5},{10,13,8,9,8,2,7,8,9,9,12,11,6,1,9,14,15,8,7,0,9,2,7,12,8,15,12,15,11,8,1,1,4,6,14,9,4,3,1,7,6,8,5,6,6,7,0,12,10,1,3,15,6,11,8,4,4,10,12,6,13,10,9,6,15,8,10,6,14,4,14,8,0,3,11,5,7,6,12,8,13,4,3,5,15,2,3,11,15,10,11,2,13,0,15,2,9,8,15,7},{14,5,6,5,6,10,13,10,8,13,6,9,5,13,4,13,13,9,8,12,14,14,8,0,15,3,5,9,1,10,14,8,7,12,9,13,11,3,0,13,5,0,15,2,6,5,14,14,15,8,9,0,12,7,13,11,7,8,11,4,1,8,6,1,4,0,9,5,12,12,1,2,0,14,7,10,8,11,12,14,9,0,6,10,7,10,6,13,10,7,2,11,3,11,2,12,8,12,9,14},{10,5,15,7,5,6,3,12,12,4,8,8,10,11,12,13,6,2,8,0,0,5,9,3,3,2,12,13,0,5,8,5,12,2,2,2,1,13,15,6,10,15,15,14,13,0,13,7,15,12,13,12,11,10,12,4,6,15,4,9,14,13,6,10,1,2,5,4,2,1,15,1,11,4,5,7,10,7,9,12,15,12,6,12,10,9,5,2,13,11,13,2,13,6,0,5,14,1,2,8},{12,1,10,6,0,12,7,2,11,5,10,13,1,3,9,14,5,6,13,3,9,0,3,14,14,10,14,2,3,8,15,11,0,6,10,5,3,6,13,11,4,15,8,13,3,14,2,10,7,14,9,14,9,8,2,13,4,8,3,13,1,4,11,0,4,4,9,12,8,13,12,11,13,6,5,14,3,9,4,12,13,6,15,9,10,15,1,3,8,15,6,1,9,2,5,8,0,9,4,14},{15,13,10,13,2,5,5,14,14,6,15,3,12,14,3,7,2,15,8,2,15,11,6,0,3,4,5,5,7,8,4,13,3,15,10,14,9,5,0,12,10,13,7,6,6,2,14,14,2,0,2,8,2,10,10,10,13,14,6,9,8,11,14,14,14,11,14,2,5,4,6,4,9,13,12,14,2,11,4,6,10,9,13,8,10,5,13,12,12,3,10,11,7,2,0,14,14,1,12,13},{13,7,0,7,0,1,11,15,11,14,0,2,12,4,3,0,2,0,0,15,7,7,15,5,13,6,8,14,6,7,6,4,1,1,0,12,6,7,4,5,1,7,5,11,0,2,10,8,9,9,4,13,6,10,1,0,12,2,0,9,9,0,15,15,1,0,11,7,7,10,14,0,15,13,0,15,10,2,3,5,7,12,11,3,5,3,7,11,12,7,0,4,15,12,0,14,15,7,11,1},{0,12,9,7,11,6,7,13,13,0,14,7,14,1,3,8,3,15,1,10,11,6,10,9,10,12,3,12,3,9,7,6,4,9,1,1,8,7,4,5,7,15,10,14,14,10,4,3,5,7,0,7,2,8,1,6,15,4,3,8,12,10,2,7,11,15,13,13,13,12,1,9,6,4,10,8,2,4,3,12,10,13,12,1,11,15,13,12,1,14,15,12,11,6,4,10,14,0,5,12},{4,13,7,4,12,1,10,9,3,3,14,9,14,0,2,10,10,14,14,7,4,0,15,10,3,7,10,4,13,0,13,4,6,4,7,15,10,3,7,6,13,3,4,14,6,5,12,15,5,0,10,3,3,0,7,6,5,1,13,5,15,1,2,0,4,10,13,15,0,3,9,2,6,14,0,5,8,10,15,11,0,8,5,4,13,2,1,4,8,13,8,13,1,11,8,5,7,7,4,2},{9,13,12,10,5,5,3,12,6,14,4,12,11,8,8,8,6,0,15,4,5,15,11,3,7,8,3,11,3,13,3,11,11,15,1,15,12,15,11,15,3,12,15,5,13,8,13,6,4,9,6,4,12,1,7,9,2,15,6,0,14,4,11,10,9,3,2,2,14,12,4,2,14,9,4,4,2,10,2,6,10,5,8,12,1,12,0,6,4,15,9,7,4,4,0,8,9,9,1,15},{5,10,1,12,11,10,11,2,3,6,0,3,3,2,2,14,13,14,9,9,12,9,7,7,15,3,8,9,0,9,10,6,13,8,6,13,15,9,14,2,8,6,7,3,3,13,1,8,11,0,4,0,3,7,9,7,11,2,11,6,1,1,8,14,8,2,14,14,1,1,11,3,13,8,6,14,0,15,12,2,15,15,8,8,10,2,14,10,12,6,3,5,14,7,5,10,14,10,8,5},{15,10,4,7,13,1,4,8,6,2,6,12,12,7,10,7,12,5,5,14,4,8,5,4,1,13,6,2,11,6,1,9,9,4,15,5,15,14,7,6,15,11,14,14,10,13,7,2,11,14,2,5,13,1,13,1,1,9,8,8,9,9,5,1,11,10,7,14,5,3,8,12,7,10,7,12,0,0,6,10,9,9,7,11,11,13,8,5,4,2,10,9,1,7,14,1,1,5,14,3},{4,8,7,0,13,4,14,14,13,4,7,14,9,14,8,15,0,11,6,3,2,15,9,7,1,14,8,12,2,6,10,3,12,10,10,14,8,10,0,8,2,9,4,3,15,15,9,5,9,7,4,9,7,5,11,12,6,12,4,12,1,9,2,8,7,2,6,11,3,7,7,3,7,14,13,3,11,5,2,1,7,9,11,15,8,3,9,6,3,8,14,12,4,3,8,1,15,7,7,5},{8,2,8,11,10,15,13,14,9,0,0,11,4,4,14,0,0,2,12,15,7,6,5,10,13,10,0,15,15,14,15,0,2,1,10,10,5,5,6,8,7,4,4,1,14,11,13,3,4,1,1,5,12,15,0,7,5,1,4,6,4,5,0,0,2,1,14,11,0,6,11,5,5,3,13,5,14,15,13,13,6,0,0,2,1,10,9,9,13,12,10,1,5,4,13,1,4,4,12,8},{9,11,9,14,8,14,1,0,7,11,8,15,15,5,13,6,11,7,15,12,1,7,4,7,4,0,9,1,1,1,8,9,0,5,1,10,4,15,6,3,10,11,8,10,5,0,7,3,0,8,5,10,13,14,7,2,10,6,15,14,4,4,0,6,13,15,2,4,13,11,7,13,8,15,5,12,2,3,2,5,14,12,9,12,3,1,9,12,15,10,8,14,0,15,0,3,10,2,13,10},{15,4,11,5,6,9,1,11,6,6,9,11,7,3,7,1,15,14,3,0,1,12,15,0,7,3,15,1,14,12,11,5,13,10,12,7,0,1,1,14,12,3,10,2,4,11,5,8,13,10,14,6,13,15,1,15,1,6,10,8,14,6,6,10,9,12,10,12,8,10,5,15,4,3,14,8,9,5,7,13,5,0,14,13,2,10,2,10,6,2,3,2,6,9,3,15,8,12,8,13},{12,4,10,13,4,15,15,4,10,5,3,12,2,8,7,3,4,2,8,14,6,8,12,0,8,12,4,8,15,5,10,5,2,5,13,4,5,13,4,2,12,13,1,15,2,11,13,15,8,11,14,10,3,1,11,12,5,5,14,7,14,15,11,15,4,12,13,10,2,2,0,1,5,12,5,6,15,6,8,10,13,6,9,6,8,10,11,14,14,15,6,12,11,6,5,3,7,7,11,7},{4,6,3,9,12,1,2,5,2,10,11,4,14,11,11,8,3,2,8,8,9,2,9,1,2,14,6,1,14,14,12,5,7,8,12,4,14,4,14,0,13,10,3,2,1,8,8,6,10,14,5,13,7,2,12,14,3,3,2,11,7,0,11,9,8,7,2,14,14,12,0,0,13,4,6,1,9,1,3,10,2,3,9,9,13,15,13,5,9,15,15,11,12,0,8,15,10,2,13,10},{14,6,0,6,8,15,10,2,7,9,0,1,13,1,3,2,12,11,10,10,3,2,7,5,6,0,5,0,5,15,6,15,3,2,5,14,11,5,11,11,2,9,8,3,11,0,0,7,6,5,5,8,7,8,1,11,0,12,13,6,9,0,10,11,6,4,9,6,1,1,2,9,8,4,0,8,4,4,10,14,13,14,9,10,10,14,1,1,12,9,1,4,13,2,1,13,1,4,9,10},{4,15,7,7,7,2,15,4,10,15,6,13,1,3,5,10,13,13,8,4,9,10,15,7,11,12,5,13,2,4,12,5,15,8,8,6,8,1,9,14,1,6,14,9,4,0,2,0,13,4,7,9,2,7,2,7,10,9,13,15,11,2,5,7,3,11,11,6,2,9,2,6,15,0,14,2,15,5,13,2,11,4,7,15,6,0,1,4,8,14,5,14,7,5,8,3,5,14,7,4},{1,0,6,15,13,10,11,9,10,11,14,10,15,3,10,13,7,10,3,11,14,0,4,10,0,12,9,13,10,14,6,8,11,5,10,5,8,14,10,2,9,12,3,15,8,10,14,10,12,0,10,9,3,10,15,12,7,0,9,15,14,0,1,13,1,10,8,11,0,13,11,7,7,1,13,7,14,7,2,12,2,9,13,15,11,12,1,8,15,0,1,12,2,2,1,0,10,6,2,14},{8,1,3,4,0,10,3,10,8,4,12,15,3,15,8,0,13,12,8,15,10,5,3,12,4,14,2,0,15,9,13,5,15,7,14,12,3,7,2,15,2,14,1,14,6,0,7,12,5,5,1,6,5,8,8,8,10,10,13,14,8,14,10,13,2,14,0,0,10,10,11,1,8,15,10,6,11,9,2,8,12,9,1,5,11,15,2,15,9,0,5,14,0,3,5,9,13,14,13,8},{6,9,3,12,3,9,9,12,13,10,4,9,9,5,3,0,11,14,8,14,9,8,14,3,0,0,9,1,3,12,9,8,0,9,7,13,15,12,1,3,5,7,11,12,0,2,10,2,0,9,7,13,8,3,11,11,12,0,7,13,15,0,6,3,14,0,2,9,3,6,1,1,13,15,11,11,14,7,2,15,4,7,1,15,12,13,1,13,14,2,3,7,12,2,15,11,4,10,0,9},{7,3,4,6,3,12,0,7,9,12,15,12,0,12,10,15,7,6,2,10,4,12,11,14,8,13,12,9,9,5,10,14,5,9,0,8,12,7,7,13,6,7,3,7,15,5,13,2,7,7,8,13,4,15,10,7,7,0,4,5,14,0,1,14,6,13,2,8,8,9,3,5,7,13,8,6,1,3,6,5,2,13,0,13,10,3,1,6,9,8,3,8,12,11,7,1,4,0,3,10},{4,7,4,14,6,12,0,3,1,2,0,12,9,4,8,1,15,0,5,6,14,4,6,4,15,13,9,5,7,1,11,5,6,15,2,12,14,0,2,15,8,13,8,8,10,5,13,10,5,7,0,13,10,2,6,5,0,0,12,6,4,2,6,12,7,10,15,0,5,15,10,11,15,6,10,5,9,3,9,12,7,4,9,9,13,2,3,6,6,10,4,9,4,5,5,6,3,9,10,15},{5,1,3,0,3,3,1,3,13,4,0,0,7,13,5,12,12,2,2,1,0,9,0,2,3,5,14,7,3,9,11,0,1,2,13,15,15,0,5,3,3,5,0,10,8,12,5,7,14,8,15,4,12,12,8,6,3,0,10,0,14,2,7,7,15,5,11,10,2,5,12,7,8,4,2,8,15,14,0,3,3,7,6,5,4,13,14,15,15,15,11,7,6,14,2,4,0,8,8,3},{13,13,3,4,7,6,14,13,15,9,10,15,2,6,15,13,14,3,10,3,5,15,13,7,2,9,1,9,14,8,8,14,4,13,9,13,7,8,14,13,14,1,7,2,15,5,10,13,13,13,12,13,15,11,8,6,13,11,11,15,11,12,8,8,11,7,13,7,5,13,5,12,5,12,8,2,7,3,0,1,14,2,12,14,11,2,0,10,11,14,7,6,13,10,7,7,10,11,5,4},{7,12,7,10,3,15,0,2,3,2,14,5,0,10,15,11,6,13,11,8,14,1,15,0,8,14,9,4,13,6,4,1,2,9,3,2,1,15,1,11,15,6,1,8,6,0,6,10,11,6,8,1,13,2,13,12,0,15,15,7,2,9,8,7,5,2,7,12,10,4,0,0,7,10,7,4,10,2,10,7,0,1,12,0,0,7,0,6,9,6,10,3,2,5,7,12,4,2,12,9},{6,3,8,14,3,1,1,2,8,3,7,11,3,5,6,0,0,15,5,5,11,4,10,1,13,14,4,10,3,7,8,2,12,7,6,10,3,1,1,9,12,12,14,13,9,13,7,5,7,11,0,13,6,14,10,7,12,12,15,13,1,0,2,13,7,12,9,13,15,0,3,8,8,5,6,3,15,4,14,1,3,2,10,15,10,13,0,8,5,3,1,7,15,5,11,11,0,13,0,10},{2,2,7,4,6,13,2,14,7,1,11,5,4,0,12,11,10,1,12,6,11,9,3,5,3,8,5,2,8,13,5,15,10,9,10,2,4,12,12,11,13,14,5,5,8,1,14,12,15,6,7,15,1,8,4,14,3,8,11,12,9,14,13,4,7,8,0,15,1,1,1,1,14,7,8,11,13,4,9,9,0,4,13,7,7,8,9,1,14,0,2,3,11,14,10,1,5,9,10,5},{13,15,10,10,7,14,7,3,1,6,9,9,4,0,5,7,15,11,14,4,10,14,1,7,15,11,10,4,3,0,2,13,6,9,2,7,3,8,2,0,13,9,12,13,5,15,13,0,7,1,9,1,3,0,0,5,7,9,4,4,4,10,3,13,0,13,8,11,6,11,0,15,7,12,8,4,5,6,13,5,7,10,0,5,9,4,11,2,15,10,5,12,11,10,1,8,6,5,12,1}};


    }



    //dp solution for bottom up o(n) solution
    public TDR algo9TopDown(int[][] arr, int i, int k, boolean buy, HashMap<String, TDR> memo, int stock, int buyIndex, int prevStock) throws CloneNotSupportedException {
        if (i >= arr[0].length ||  stock >= arr.length || stock < 0) return new TDR();
        String key = String.format("%s-%s-%s", i, buy, stock);
        if(!memo.containsKey(key)) {
            subProblems++;
            TDR profit = this.algo9TopDown(arr, i + 1, k, buy, memo, stock, buyIndex,-1);
            if (buy) {
                TDR ps = this.algo9TopDown(arr, i + k + 1, k ,false, memo, stock, -1,-1);
                TDR pu = this.algo9TopDown(arr, i + k + 1, k, false, memo, stock + 1, -1,-1);
                TDR pd = this.algo9TopDown(arr, i + k + 1, k, false, memo, stock - 1, -1,-1);
                TDR temp  = Max.compare(Max.compare(ps, pu), pd);
                temp.profit = temp.profit + arr[stock][i];
                temp.sellDay = i ;
                profit = (TDR)Max.compare(profit, temp).clone();

            } else {

                TDR ps = this.algo9TopDown(arr, i + 1, k, true, memo, stock, i,-1);
                TDR pu = new TDR();
                TDR pd = new TDR();
                if(prevStock != stock + 1)
                    pu = this.algo9TopDown(arr, i, k, false, memo, stock + 1, -1,-1);
                if(prevStock != stock - 1)
                    pd = this.algo9TopDown(arr, i, k, false, memo, stock-1, -1, stock);

                ps.profit = ps.profit - arr[stock][i];
                ps.comb = String.format("%s %s %s", stock, i, ps.sellDay) + ((ps.comb != "")?(","+ps.comb):"");
                profit = Max.compare(profit, ps);
                profit = Max.compare(profit, pu);
                profit = Max.compare(profit, pd);
            }
            memo.put(key, profit);
        }

        return (TDR)memo.get(key).clone();
    }


    public TDR algo7(boolean brought, int i, int n, int[] arr, int c, TDR[] max, int stock) throws CloneNotSupportedException {
        if(i >= n)
            return new TDR();

        subProblems++;
        TDR profit = (TDR)algo7(brought, i+1, n, arr, c, max, stock).clone();
        if(brought) {
            TDR temp = (TDR)algo7(false, i + c + 1, n, arr, c, max, stock).clone();

            temp.profit += arr[i];
            temp.sellDay = i;
            profit = Max.compare(profit, temp);
            return profit;
        }
        else {
            TDR ps = (TDR)algo7(true, i + 1, n, arr, c, max, stock).clone();
            ps.profit = ps.profit - arr[i];
            ps.comb = String.format("%s %s %s", stock, i, ps.sellDay) + ((ps.comb != "")?(","+ps.comb):"");
            profit = Max.compare(ps, profit);
        }
        max[i] = Max.compare(profit, max[i]);
        return max[i];

    }

    public TDR algo7(int[][] stockPrices, int c) throws CloneNotSupportedException {
        TDR[] opt = new TDR[stockPrices[0].length];
        for(int i = 0; i < opt.length; i++)
            opt[i] = new TDR();
        TDR profit = new TDR();

        for(int j = 0; j < 2; j++) {
            for (int i = 0; i < stockPrices.length; i++)
                profit = Max.compare(profit, algo7(false, 0, stockPrices[0].length, stockPrices[i], c, opt, i));
        }
        System.out.println(profit.profit + "  " + String.join("\n", profit.comb.split(",")));
        return profit;
    }



    // dp solution for o(n^2) solution
    public int algo8(int[][] stockPrices,int c,  int[][]max, String[][] store) {
        for(int s = 0; s < stockPrices.length; s++){
            max[0][0] = 0;
            int t = stockPrices[s][1] - stockPrices[s][0];
            if(t > max[1][0])
                store[1][0] = String.format("%s %s %s", s, 0, 1);
            max[1][0] = Math.max(max[1][0], t);
            for(int i = 2; i < stockPrices[0].length; i++){
                int temp = 0;
                String comb = "";
                if(max[i-1][0] > max[i][0])
                    store[i][0] = store[i-1][0];
                max[i][0] = Math.max(max[i][0], max[i-1][0]);

                for(int j = i-1; j>=0; j--) {
                    t = stockPrices[s][i] - stockPrices[s][j];
                    if (t > max[i][j]) {
                        comb = String.format("%s %s %s", s, j, i);
                        store[i][j] = comb;
                    }

                    max[i][j] = Math.max(t, max[i][j]);
                    int prevIndex = j - (c + 1);

                    if(max[i][j] + (( prevIndex < 0) ? 0 : max[prevIndex][0]) > max[i][0] ) {
                        store[i][0] = store[i][j] + ((prevIndex < 0) ? "" : ((store[prevIndex][0] != "") ? ("," + store[prevIndex][0]) : ""));
                    }
                    max[i][0] = Math.max(max[i][j] + (( prevIndex < 0) ? 0 : max[prevIndex][0]), max[i][0]);
                }

            }

        }

        System.out.println(String.join("\n", store[stockPrices[0].length-1][0].split(",")));
        return max[stockPrices[0].length-1][0];
    }

    public  void algo9BottomUp(int[][] arr, int c) throws CloneNotSupportedException {
        HashMap<String, TDR> dp = new HashMap<String, TDR>();

        int m = arr.length-1;
        int n = arr[0].length-1;
        for(int i = n; i>=0; i--){
            for(int v = 0; v<=m; v++){
                String bK = String.format("%s %s %s", v, i, true);
                String sK = String.format("%s %s %s", v, i, false);
                TDR skip = dp.getOrDefault(String.format("%s %s %s", v, i+1, true), new TDR());
                TDR pu = (TDR)dp.getOrDefault(String.format("%s %s %s", v+1, i+c+1, false), new TDR()).clone();
                TDR ps = (TDR)dp.getOrDefault(String.format("%s %s %s", v, i+c+1, false), new TDR()).clone();
                TDR pd = (TDR)dp.getOrDefault(String.format("%s %s %s", v-1, i+c+1, false), new TDR()).clone();
                TDR t = Max.compare(Max.compare(ps, pd), pu);
                t.sellDay = i;
                t.profit += arr[v][i];
                dp.put(bK, Max.compare(t, skip));

                skip = (TDR)dp.getOrDefault(String.format("%s %s %s", v, i+1, false), new TDR()).clone();
                ps = (TDR) dp.getOrDefault(String.format("%s %s %s", v, i, true), new TDR()).clone();
                ps.profit += -arr[v][i];
                if(ps.sellDay != i)
                    ps.comb = String.format("%s %s %s", v, i, ps.sellDay) + ((ps.comb != "")?(","+ps.comb):"");

                pd = (TDR) dp.getOrDefault(String.format("%s %s %s", v-1, i, false), new TDR()).clone();
                dp.put(sK, Max.compare(skip, Max.compare(ps, pd)));

            }
        }

        System.out.println(dp.get(String.format("%s %s %s", m, 0, false)).profit  + "  " + String.join("\n", dp.get(String.format("%s %s %s", m, 0, false)).comb.split(",")));
    }
}


