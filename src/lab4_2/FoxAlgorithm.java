package lab4_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FoxAlgorithm {
    private final Matrix aMatrix;
    private final Matrix bMatrix;
    private int threadNum;
    public FoxAlgorithm(Matrix a, Matrix b, int threads){
        this.aMatrix = a;
        this.bMatrix = b;
        this.threadNum = threads;
    }

    public Result multiplyMatrix(){
        Matrix resultMatrix = new Matrix(aMatrix.getSizeX(), bMatrix.getSizeY());
        threadNum = threadNum < aMatrix.getSizeX() ? threadNum : aMatrix.getSizeX();
        threadNum = Help.nearestDivisor(threadNum, aMatrix.getSizeX());
        int stepSize = aMatrix.getSizeX() / threadNum;

        ExecutorService execPool = Executors.newFixedThreadPool(threadNum);
        long timestamp0 = System.nanoTime();

        int[][] sizesX = new int[threadNum][threadNum];
        int[][] sizesY = new int[threadNum][threadNum];
        for (int x = 0; x < threadNum; x++) {
            for (int y = 0; y < threadNum; y++) {
                sizesX[x][y] = stepSize * x;
                sizesY[x][y] = stepSize * y;
            }
        }

        for (int move = 0; move < threadNum; move++) {
            for (int x = 0; x < threadNum; x++) {
                for (int y = 0; y < threadNum; y++) {
                    int aShiftIndexX = sizesX[x][(x + move) % threadNum];
                    int aShiftIndexY = sizesY[x][(x + move) % threadNum];

                    int bShiftIndexX = sizesX[(x + move) % threadNum][y];
                    int bShiftIndexY = sizesY[(x + move) % threadNum][y];

                    Matrix aBlock = getBlock(aMatrix, aShiftIndexX, aShiftIndexY, stepSize);
                    Matrix bBlock = getBlock(bMatrix, bShiftIndexX, bShiftIndexY, stepSize);
                    FoxThread thread = new FoxThread(
                            aBlock, bBlock, sizesX[x][y], sizesY[x][y], resultMatrix
                    );
                    execPool.execute(thread);
                }
            }
        }

        execPool.shutdown();
        try {
            execPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long timestamp1 = System.nanoTime();
        return new Result(resultMatrix, ((timestamp1-timestamp0)/1000000));
    }

    private Matrix getBlock(Matrix matrix, int indexX, int indexY, int stepSize){
        Matrix block = new Matrix(stepSize, stepSize);
        for (int index = 0; index < stepSize; index++) {
            System.arraycopy(matrix.matrix[index + indexX], indexY, block.matrix[index], 0, stepSize);
        }
        return block;
    }
}

class FoxThread extends Thread {
    private Matrix aMatrix;
    private Matrix bMatrix;
    private final int stepX;
    private final int stepY;
    private Matrix resultMatrix;

    public FoxThread(Matrix a, Matrix b, int x, int y, Matrix result) {
        this.aMatrix = a;
        this.bMatrix = b;
        this.stepX = x;
        this.stepY = y;
        this.resultMatrix = result;
    }

    @Override
    public void run(){
        Matrix subMatrix = new Matrix(aMatrix.getSizeY(), bMatrix.getSizeX());
        for (int x = 0; x < aMatrix.getSizeX(); x++) {
            for (int y = 0; y < bMatrix.getSizeY(); y++) {
                for (int i = 0; i < bMatrix.getSizeX(); i++) {
                    subMatrix.matrix[x][y] += aMatrix.matrix[x][i] * bMatrix.matrix[i][y];
                }
            }
        }

        for (int x = 0; x < subMatrix.getSizeX(); x++) {
            for (int y = 0; y < subMatrix.getSizeY(); y++) {
                resultMatrix.matrix[x + stepX][y + stepY] += subMatrix.matrix[x][y];
            }
        }
    }
}
