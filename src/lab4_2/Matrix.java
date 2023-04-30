package lab4_2;

public class Matrix {
    public double[][] matrix;
    private final int sizeX;
    public int getSizeX(){
        return this.sizeX;
    }
    private final int sizeY;
    public int getSizeY(){
        return this.sizeY;
    }
    public Matrix(int X,int Y){
        this.sizeX = X;
        this.sizeY = Y;
        this.matrix = new double[X][Y];
    }

    public void randomMatrixFilling(int maxValue) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                matrix[x][y] = Math.random() * maxValue;
            }
        }
    }
}