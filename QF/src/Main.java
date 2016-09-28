import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {

	public static void processCase(BufferedReader in, PrintStream out) throws IOException {
		
		String line = in.readLine(); // to read multiple integers line
		String[] splitted = line.split(" ");
		int nums [] = new int[5];		

		for (int i = 0; i < splitted.length; i++) {
			nums[i] = Integer.parseInt(splitted[i]);
		}

		int command = nums[0];
		int K = nums[1];
							
		int sumCells [][] = new int[K+1][K+1];
		
		while (true) {
			
			line = in.readLine();
			splitted = line.split(" ");
			
			for (int i = 0; i < splitted.length; i++) {
				nums[i] = Integer.parseInt(splitted[i]);
			}
			
			command = nums[0];
			
			if (command == 3) {
				break;
			} else if (command == 1) {
				
				int x = nums[1];
				int y = nums[2];
				int d = nums[3];
															
				for (int i = y+1; i < sumCells.length; i++) {
					for (int j = x+1; j < sumCells[0].length; j++) {						
							sumCells[i][j] += d;
					}
				}				
			} else if (command == 2) {
				int l = nums[1];
				int b = nums[2];
				int r = nums[3];
				int t = nums[4];
				
				int sum = 0;															
				
				if (l == 0 && b == 0) {
					sum = sumCells[t+1][r+1];
				} else {
					sum = sumCells[t+1][r+1] + sumCells[b][l] - sumCells[b][r+1] - sumCells[t+1][l];
				}
				
				System.out.println(sum);								
			}			
		}		
	}
		
	public static void process(BufferedReader in, PrintStream out) throws NumberFormatException, IOException {
        int N = Integer.parseInt(in.readLine());
		
        for (int i = 1; i <= N; i++) {
            out.println("" + i + ": ");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		process(new BufferedReader(new InputStreamReader(System.in)), System.out);
	}

}
