import java.io.PrintStream;
import java.util.Scanner;

public class Main {
	
	private static int countNonZeros(int matrix[][], int r, int c) {
		int total = 0;
		
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				if (matrix[i][j] == 0) {
					total++;
				}
			}
		}
		
		return r*c - total;
	}
		
	public static void fillMatrix(int [][] matrix, int r, int c, Scanner in) {
		for (int i = 0; i < r; i++) {
        	for (int j = 0; j < c; j++) {
        		matrix[i][j] = in.nextInt();
        	}
        }
	}
	
	public static void processCase(Scanner in, PrintStream out) {
        
		int r = in.nextInt();
        int c = in.nextInt();        
		
        int [][] matrix = new int[r][c];
        
        fillMatrix(matrix, r, c, in);
        		
		while (true) {
			for (int j = 0; j < c; j++) {
				for (int i = 0; i < r; i++) {
										
					if (i + 1 < r && matrix[i][j] != 0) {
						/*
						 * Lower pair
						 */
						
						if (matrix[i][j] < 0) {
							matrix[i][j] += 1;
							matrix[i+1][j] += 1;
							
						} else {
							matrix[i][j] -= 1;
							matrix[i+1][j] -= 1;
							
						}
					} 
					
					if (j + 1 < c && matrix[i][j] != 0) {
						/*
						 * Right pair
						 */
						
						if (matrix[i][j] < 0) {
							matrix[i][j] += 1;
							matrix[i][j+1] += 1;
							
						} else {
							matrix[i][j] -= 1;
							matrix[i][j+1] -= 1;
						}
					}
				}
			}
			
			int numNonZeroEntries = countNonZeros(matrix, r, c); 
			
			if (numNonZeroEntries == 1) {
				out.println("No");
				break;
			} else if (numNonZeroEntries == 0) {
				out.println("Yes");
				break;
			} 		
		}
    }
	
	public static void process(Scanner in, PrintStream out) {
        int N = in.nextInt();
        for (int i = 1; i <= N; i++) {
            out.print("" + i + ": ");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) {			
		process(new Scanner(System.in), System.out);		
	}	

}
