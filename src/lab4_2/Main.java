package lab4_2;

import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final int X_SIZE = 3000;
    private static final int Y_SIZE = 3000;
    private static final int MAX_RANDOM_VALUE = 1;
    private static final String STRING_FORMAT = "%7.2f";
    public static void main(String[] args) {
        testAllAlgorithms();
    }

    static void testAllAlgorithms (){
        Matrix m1 = new Matrix(X_SIZE,Y_SIZE);
        m1.randomMatrixFilling(MAX_RANDOM_VALUE);
        Matrix m2 = new Matrix(X_SIZE,Y_SIZE);
        m2.randomMatrixFilling(MAX_RANDOM_VALUE);

        System.out.println(X_SIZE+"x"+Y_SIZE+":");

        int threadNum = Runtime.getRuntime().availableProcessors();
        FoxAlgorithm FoxAlg = new FoxAlgorithm(m1,m2,threadNum);
        Result resFoxOld = FoxAlg.multiplyMatrix();
//        resFoxOld.printfMatrix(STRING_FORMAT);
        resFoxOld.printfTime("Fox with ThreadPools (Old)");

        threadNum = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadNum);
        Result resFoxNew = forkJoinPool.invoke(new FoxAlgorithmFork(m1,m2,threadNum));
//        resFoxNew.printfMatrix(STRING_FORMAT);
        resFoxNew.printfTime("Fox with ForkJoin (New)");

        System.out.printf(
                "Algorithms difference: %.2f\n",
                ((double) resFoxOld.getCalculationTime() / resFoxNew.getCalculationTime())
        );
    }
}