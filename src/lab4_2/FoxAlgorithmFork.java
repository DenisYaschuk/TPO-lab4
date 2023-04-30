package lab4_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FoxAlgorithmFork extends RecursiveTask<Result> {
    private final Matrix aMatrix;
    private final Matrix bMatrix;
    private int threadNum;
    Matrix resultMatrix;
    private int[][] sizesX;
    private int[][] sizesY;
    int stepSize;
    public FoxAlgorithmFork(Matrix a, Matrix b, int threads) {
        this.aMatrix = a;
        this.bMatrix = b;
        this.threadNum = threads;

        resultMatrix = new Matrix(aMatrix.getSizeX(), bMatrix.getSizeY());
        threadNum = threadNum < aMatrix.getSizeX() ? threadNum : aMatrix.getSizeX();
        threadNum = Help.nearestDivisor(threadNum, aMatrix.getSizeX());
        stepSize = aMatrix.getSizeX() / threadNum;

        sizesX = new int[threadNum][threadNum];
        sizesY = new int[threadNum][threadNum];
        for (int x = 0; x < threadNum; x++) {
            for (int y = 0; y < threadNum; y++) {
                sizesX[x][y] = stepSize * x;
                sizesY[x][y] = stepSize * y;
            }
        }
    }

    private Matrix getBlock(Matrix matrix, int indexX, int indexY, int stepSize){
        Matrix block = new Matrix(stepSize, stepSize);
        for (int index = 0; index < stepSize; index++) {
            System.arraycopy(matrix.matrix[index + indexX], indexY, block.matrix[index], 0, stepSize);
        }
        return block;
    }

    @Override
    protected Result compute() {
        long timestamp0 = System.nanoTime();
        List<RecursiveTask<HashMap<String, Object>>> computList = new ArrayList<>();
        for (int move = 0; move < threadNum; move++) {
            for (int x = 0; x < threadNum; x++) {
                for (int y = 0; y < threadNum; y++) {
                    int aShiftIndexX = sizesX[x][(x + move) % threadNum];
                    int aShiftIndexY = sizesY[x][(x + move) % threadNum];

                    int bShiftIndexX = sizesX[(x + move) % threadNum][y];
                    int bShiftIndexY = sizesY[(x + move) % threadNum][y];

                    Matrix aBlock = getBlock(aMatrix, aShiftIndexX, aShiftIndexY, stepSize);
                    Matrix bBlock = getBlock(bMatrix, bShiftIndexX, bShiftIndexY, stepSize);
                    FoxFork foxFork = new FoxFork(
                            aBlock, bBlock, sizesX[x][y], sizesY[x][y]
                    );
                    computList.add(foxFork);
                    foxFork.fork();
                }
            }
        }

        for (RecursiveTask<HashMap<String, Object>> computMap : computList) {
            HashMap<String, Object> map = computMap.join();

            Matrix block = (Matrix) map.get("block");
            int stepX = (int) map.get("stepX");
            int stepY = (int) map.get("stepY");

            for (int x = 0; x < block.getSizeX(); x++) {
                for (int y = 0; y < block.getSizeY(); y++) {
                    resultMatrix.matrix[x + stepX][y + stepY] += block.matrix[x][y];
                }
            }
        }

        long timestamp1 = System.nanoTime();
        return new Result(resultMatrix, ((timestamp1-timestamp0)/1000000));
    }
}

class FoxFork extends RecursiveTask<HashMap<String, Object>> {
    private Matrix aMatrix;
    private Matrix bMatrix;
    private final int stepX;
    private final int stepY;

    public FoxFork(Matrix a, Matrix b, int x, int y) {
        this.aMatrix = a;
        this.bMatrix = b;
        this.stepX = x;
        this.stepY = y;
    }

    @Override
    protected HashMap<String, Object> compute() {
        Matrix block = new Matrix(aMatrix.getSizeY(), bMatrix.getSizeX());
        for (int x = 0; x < aMatrix.getSizeX(); x++) {
            for (int y = 0; y < bMatrix.getSizeY(); y++) {
                for (int i = 0; i < bMatrix.getSizeX(); i++) {
                    block.matrix[x][y] += aMatrix.matrix[x][i] * bMatrix.matrix[i][y];
                }
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("block", block);
        map.put("stepX", stepX);
        map.put("stepY", stepY);
        return map;
    }
}