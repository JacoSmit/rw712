import java.io.PrintStream;
import java.util.Scanner;

public class Main {

	public static void processCase(Scanner in, PrintStream out) {
		
		int n = in.nextInt();
		int votes [] = new int[n+1];
		int w = 0;
		
		/*
		 * Sum total votes
		 */
		for (int i = 1; i < votes.length; i++) {
			votes[i] = in.nextInt();
			w += votes[i];
		}
		
		/*
		 * Compute Q. Q is minimum number of votes to have the majority
		 */
		int q = (int) Math.ceil(w / 2.0);
		
		long a[][] = new long[w+1][n+1];		
		fillFirstRow(a, 1L);
				
		for (int j = 1; j <= w; j++) {
			for (int r = 1; r <= n; r++) {				
				if (j-votes[r] >= 0) {				
					a[j][r] = a[j][r-1] + a[j-votes[r]][r-1];
				} else {
					a[j][r] = a[j][r-1];
				}
			}
		}
		
		long lastCol[] = new long[w+1];
		
		for (int i = 0; i < w+1; i++) {
			lastCol[i] = a[i][a[0].length-1];
		}	
		
		long c[][] = new long[w+1][n+1];		
		fillFirstRow(c, 1L);
		
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= w; j++) {
			
				if (j-votes[i] >= 0) {
					c[j][i] = lastCol[j] - c[j-votes[i]][i];
				} else {
					c[j][i] = lastCol[j];
				}
			}
		}
						
		long total = 0L;
		for (int i = 1; i <= n; i++) {
			total = 0;
			for (int j = q-votes[i]; j <= q-1; j++) {
				if (j < 0) {
					continue;
				}
				total += c[j][i];
			}
			out.println(i + " ~ " + total);
		}		
	}	
	
	private static void fillFirstRow(long [][] arr, long val) {
		for (int i = 0; i < arr[0].length; i++) {
			arr[0][i] = val;
		}
	}
	
	public static void process(Scanner in, PrintStream out) {
        int N = in.nextInt();
        for (int i = 1; i <= N; i++) {
            out.println("" + i + ": ");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) {			
		process(new Scanner(System.in), System.out);		
	}

}
