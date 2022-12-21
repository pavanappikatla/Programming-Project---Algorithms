public class Max {
    public static TDR compare(TDR a, TDR b){
        if(a.profit >= b.profit)
            return a;
        else
            return b;
    }
}
