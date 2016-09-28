import java.io.PrintStream;
import java.util.Scanner;

public class Main {

	public static void processCase(Scanner in, PrintStream out) {
		String startValue = in.nextLine();
		String moves = in.nextLine();
		
		int startValues[] = new int[startValue.length()];
		int r = 0;
		int s = 0;
		
		int m = (int)Math.pow(2, startValues.length);
		int p = m/2;		
				
		boolean outBounds = false;
		
		for (int i = 0; i < startValues.length; i++) {
			startValues[i] = Integer.parseInt(startValue.charAt(i) + "");
		}
		
		for (int i = 0; i < startValues.length; i++) {
			switch (startValues[i]) {
			case 1:
				break;
				
			case 2:
				s += p;
				break;
			
			case 3:
				r += p;
				s += p;
				break;
				
			case 4:
				r += p;
				break;
			
			default:
				break;
			}			
			p /= 2;
		}
		
		
		int i = 0;
		String current = startValue;
		int numInversions = 0;
		
		while (i < moves.length()) {
			switch (moves.charAt(i)) {
			case 'U':
				r--;
				if (!testBounds(m, r)) {
					outBounds = true;
					break;
				}
				
				numInversions = getNumInversions(r);				
				current = movedString(numInversions, current, 'U');
				
				break;
				
			case 'D':
				r++;
				if (!testBounds(m, r)) {
					outBounds = true;
					break;
				}
				
				numInversions = getNumInversions(r-1);
				current = movedString(numInversions, current, 'D');
				
				break;
				
			case 'L':
				s--;
				if (!testBounds(m, s)) {
					outBounds = true;
					break;
				}
				
				numInversions = getNumInversions(s);
				current = movedString(numInversions, current, 'L');
				
				break;
				
			case 'R':
				s++;
				if (!testBounds(m, s)) {
					outBounds = true;
					break;
				}
				
				numInversions = getNumInversions(s-1);
				current = movedString(numInversions, current, 'R');
				
				break;

			default:
				break;
			}
			
			if (outBounds) {
				break;
			}
			i++;
		}
		
		if (outBounds) {
			System.out.println("OUT");
		} else {
			System.out.println(current);
		}
		
	}
	
	private static String movedString(int numInversions, String current, char direction) {
		
		int length = current.length();
		
		String newCurrent = current.substring(0, length - numInversions);
		
		for (int i = 0; i < numInversions; i++) {
			newCurrent += invertNum(Integer.parseInt(current.charAt(length - numInversions + i) + ""), direction);
		}
		
		return newCurrent;		
	}
	
	private static boolean testBounds(int m, int i) {
		if (i < 0 || i >= m) {
			return false;
		}
		return true;		
	}
		
	private static int invertNum(int x, char move) {
		
		if (move == 'U' || move == 'D') {
			switch (x) {
			case 1:
				return 4;
				
			case 2:
				return 3;
				
			case 3:
				return 2;
				
			case 4:
				return 1;

			default:
				return 0;
			}
		} else {
			switch (x) {
			case 1:
				return 2;
				
			case 2:
				return 1;
				
			case 3:
				return 4;
				
			case 4:
				return 3;

			default:
				return 0;
			}
		}		
	}
	
	private static int getNumInversions(int i) {
		String binary = Integer.toBinaryString(i+1);
		
		int total = 1;
		for (int j = binary.length()-1; j >=0; j--) {
			if (binary.charAt(j) == '1') {
				break;
			}
			total++;
		}
		
		return total;
	}
	
	public static void process(Scanner in, PrintStream out) {
        int N = Integer.parseInt(in.nextLine());
        
        for (int i = 1; i <= N; i++) {
            out.print("" + i + ": ");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) {
		process(new Scanner(System.in), System.out);
	}

}
