import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	private final static long[] FACT = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800,
			479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L};
	
	public static void processCase(BufferedReader in, PrintStream out) throws IOException {
		
		String line = in.readLine();
		String data[] = line.split(" ");
		
		long K = Long.parseLong(data[1]);
		char [] original = data[0].toCharArray();
		
		Arrays.sort(original);
				
		ArrayList<String> stringArr = new ArrayList<String>();
		
		for (int i = 0; i < original.length; i++) {
			stringArr.add(original[i]+"");
		}		
		
		System.out.println(permute(stringArr, K-1));
		
	}
	
	public static String permute(ArrayList<String> arr, long K) {
		
		if (K == 0) {
			StringBuilder x = new StringBuilder();
			for (int i = 0; i < arr.size(); i++) {
				x.append(arr.get(i));
			}
			
			return x.toString();
		}
		
		if (arr.size() == 1) {
			return arr.get(0);
		}
		
		String theWord = arr.toString();
		
		int N = arr.size();
				
		long duplicates = 1L;
		for (int i = 0; i < N; i++) {
			int numDuplicatesI =  theWord.length() - theWord.replace(arr.get(i), "").length();
			if (numDuplicatesI > 1) {
				duplicates *= FACT[numDuplicatesI];
				theWord = theWord.replace(arr.get(i), "");
			}
		}
		
		long totalCount = FACT[N]/duplicates; 

		long acc = 0L;
		
		for (int i = 0; i < N; i++) {
			
			
			int temp = i-1;
			if (temp == -1) {
				temp = N-1;
			}
			
			if (i > 0 && arr.get(temp).equals(arr.get(i))) {
				continue;
			}
			
			long count = totalCount * countOccurences(arr, i)/N;
			
			if (acc + count > K) {
				
				String returnVal = arr.remove(i);
				
				return returnVal + permute(arr, K-acc);
			}
			
			acc += count;
		}
		
		return "";
		
	}
	
	private static int countOccurences(ArrayList<String> arr, int index) {
		String theWord = arr.toString();
		return theWord.length() - theWord.replace(arr.get(index), "").length();
	}
	
	public static void process(BufferedReader in, PrintStream out) throws NumberFormatException, IOException {
        int N = Integer.parseInt(in.readLine());
        
        for (int i = 1; i <= N; i++) {
            out.print("" + i + ": ");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		process(new BufferedReader(new InputStreamReader(System.in)), System.out);
	}

}
