package lab4_2;

public class Result {
    private final Matrix matrixRes;
    public Matrix getMatrix(){
        return this.matrixRes;
    }
    private final long calculationTime;
    public long getCalculationTime(){
        return this.calculationTime;
    }
    public Result(Matrix matrix){
        this.matrixRes = matrix;
        this.calculationTime = 0;
    }
    public Result(Matrix matrix, long time){
        this.matrixRes = matrix;
        this.calculationTime = time;
    }

    public void printfMatrix(String format) {
        for (int x = 0; x < matrixRes.getSizeX(); x++) {
            for (int y = 0; y < matrixRes.getSizeY(); y++) {
                System.out.printf(format, matrixRes.matrix[x][y]);
            }
            System.out.println();
        }
    }
    public void printfTime(String algName) {
        System.out.println(algName+" algorithm calculation speed: "+calculationTime+" ms");
    }
}