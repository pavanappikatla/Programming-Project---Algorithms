import java.util.Scanner;
//Main class with main function which is an entry point to the entire project. Here the routing is done based on the algorithm keyword entered by the user
public class MainClass {
    public static void main(String[] args) throws CloneNotSupportedException {
        Scanner sc = new Scanner(System.in);
        String keyword = sc.nextLine();
      //String keyword="";
      Problem2 problem2 = new Problem2();

      //Based on the keyword entered by the user, the corresponding method is called
     switch(keyword)
     {
         case "1":
             Alg1 alg1 = new Alg1();
             alg1.main();
             break;
         case "2":
             Alg2 alg2 = new Alg2();
             alg2.main();
             break;
         case "3a":
         case "3A":
             Task3A task3A = new Task3A();
             task3A.main();
             break;
         case "3b":
         case "3B":
             Task3B task3B = new Task3B();
             task3B.main();
             break;
         case "4":
             problem2.main("4");
             break;
         case "5":
             problem2.main("5");
             break;
         case "6a":
         case "6A":
             problem2.main("6A");
             break;
         case "6b":
         case "6B":
             problem2.main("6B");
             break;
         case "7":
             problem2.main("7");
             break;
         case "8":
             problem2.main("8");
             break;
         case "9a":
         case "9A":
             problem2.main("9a");
             break;
         case "9b":
         case "9B":
             problem2.main("9b");
             break;


     }
    }
}
