import java.io.PrintStream;
import java.util.Scanner;

public class Main {

	public static void processCase(Scanner in, PrintStream out) {
		int V = in.nextInt();
		
		long xCoords[] = new long[V]; 
		long yCoords[] = new long[V]; 
				
		
		for (int i = 0; i < V; i++) {
			long x = in.nextLong();
			long y = in.nextLong();
					
			xCoords[i] = x;
			yCoords[i] = y;				
		}	
		
		long total = 0L;
		long area = 0L;		
		
		for (int i = 0; i < V; i++) {			
			
			if (i + 1 == V) {
				area += xCoords[i] * yCoords[0] - xCoords[0] * yCoords[i];
			} else {
				area += xCoords[i] * yCoords[i+1] - xCoords[i+1] * yCoords[i];
			}			
		}
		
		long B = computeB(xCoords, yCoords);
		
		area = Math.abs(area)/2;
		total = area - B/2 + 1;
		
		System.out.println(total);		
	}	
	
	private static long computeB(long []xCoords, long []yCoords) {
		
		long B = 0;
		
		for (int i = 1; i <= xCoords.length; i++) {
			if (i == xCoords.length) {
				B += gcd(Math.abs(xCoords[0] - xCoords[i-1]), Math.abs(yCoords[0] - yCoords[i-1]));
			} else {
				B += gcd(Math.abs(xCoords[i] - xCoords[i-1]), Math.abs(yCoords[i] - yCoords[i-1]));
			}
		}
		
		return B;
	}
	
	
	private static long gcd(long a, long b)
	{
	    while (b > 0)
	    {
	        long temp = b;
	        b = a % b;
	        a = temp;
	    }
	    return a;
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
