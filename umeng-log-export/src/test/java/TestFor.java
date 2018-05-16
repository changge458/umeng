public class TestFor {

    public static int count = 0;

    public static void main(String[] args) {
        //dymaticCyc(3,0,5);
        //System.out.println("count:   "+ count);
        comb(5, 3, new int[60], 0);
        //System.out.println("count:   "+ count1);
    }

    public static void comb(int n, int m, int buff[], int count1) {
        if (m == 0)//递归结束，输出
        {
            for (int i = 0; i < count1; i++) {
                System.out.print(buff[i]);
            }
            System.out.println();
            return;
        }


        for (int i = 0; i <= n - m; i++) {
            buff[count1++] = n - i;
            comb(n - i - 1, m - 1, buff, count1);
            --count1;
        }
    }

    public static void dymaticCyc(int num, int m, int sum) {
        //从sum个数中选num个数
        //dymaticCyc(2,0);此方法调用，m得从0开始
        if (num > 0) {
            for (int i = 1 + m; i <= sum; i++) {
                dymaticCyc(num - 1, i, sum);
                if (num == 1) {
                    System.out.println("  m:   " + m + "   i:   " + i);
                    count++;
                }
            }
        }
    }
}