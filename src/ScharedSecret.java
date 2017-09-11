import java.sql.Time;
import java.util.Random;

public class ScharedSecret {
	static Random rand=new Random();
	
	public static int r10(){
		return rand.nextInt(10);
	}
	
	public static int[] encodeBit(boolean bit){
		int threshold=22;
		int dist=3;
		
		int low = threshold-dist;
		int high = threshold+dist;
		
		int d1,d2,d3,d4,d5;
		//System.out.println("\nbit: "+(bit?1:0));		
			if (bit){				
				//System.out.println("bounds: ["+threshold+"..."+high+"]");
				d1=r10();
				while(d1+18<threshold) d1=r10();
				//System.out.println("d1 = "+d1);
				
				d2=r10();
				while (d1+d2+9<threshold||d1+d2>high) d2=r10();
				//System.out.println("d2 = "+d2);
				
				d3=r10();
				while (d1+d2+d3<threshold||d1+d2+d3>high)d3=r10();
				//System.out.println("d3 = "+d3);
				
				d4=r10();				
				while (d1+d2+d3+d4 > high) d4=r10();
				//System.out.println("d4 = "+d4);
				
				d5=r10();				
				while (d1+d2+d3+d4+d5 > high) d5=r10();
				//System.out.println("d5 = "+d5);
				
				
			} else {
				//System.out.println("bounds: ["+low+"..."+threshold+"]");
				d1=r10();
				while(d1+18<low) d1=r10();
				//System.out.println("d1 = "+d1);
				
				d2=r10();
				while (d1+d2+9<low) d2=r10();
				//System.out.println("d2 = "+d2);
				
				d3=r10();
				while (d1+d2+d3>threshold||d1+d2+d3<low)d3=r10();
				//System.out.println("d3 = "+d3);
				
				d4=r10();				
				while (d1+d2+d3+d4>threshold||d1+d2+d3+d4<low) d4=r10();
				//System.out.println("d4 = "+d4);
				
				d5=r10();				
				while (d1+d2+d3+d4+d5 > threshold||d1+d2+d3+d4+d5<low) d5=r10();
				//System.out.println("d5 = "+d5);
			}			
		
			//System.out.println(""+d1+d2+d3+d4+d5+" ("+(d1+d2+d3+d4+d5)+")");
		int[] result = {d1,d2,d3,d4,d5};
		return result;
	}
	
	public static String[] encodeChar(char c){
		String binary = Integer.toBinaryString(c & 0xFF);
		
		while (binary.length()<8) binary='0'+binary;
		System.out.print(binary+" ");
		String code1="";
		String code2="";
		String code3="";
		String code4="";
		String code5="";
		for (int i=0; i<8;i++){
			int[] digits = encodeBit(binary.charAt(i)=='1');
			int index = rand.nextInt(4);			
			code1+=digits[index];
			if (index!=4)digits[index]=digits[4]; // move last digit to position of used digit
			
			index = rand.nextInt(3);
			code2+=digits[index];
			if (index!=3)digits[index]=digits[3]; // move last digit to position of used digit
			
			index = rand.nextInt(2);			
			code3+=digits[index];
			if (index!=2)digits[index]=digits[2]; // move last digit to position of used digit
			
			index = rand.nextInt(1);
			code4+=digits[index];
			code5+=digits[1-index];
		}
		String[] result = {code1,code2,code3,code4,code5};
		return result;
	}
	
	public static void main(String[] args) {
		String secret="gnome-keyring";
		String code1="";
		String code2="";
		String code3="";
		String code4="";
		String code5="";
		System.out.print("  Binary: ");
		for (int i=0; i<secret.length();i++){
			String[] codes = encodeChar(secret.charAt(i));
			code1+=codes[0]+" ";
			code2+=codes[1]+" ";
			code3+=codes[2]+" ";
			code4+=codes[3]+" ";
			code5+=codes[4]+" ";
		}
		System.out.println();
		System.out.println("\nSecret 1: "+code1);
		System.out.println("\nSecret 2: "+code2);
		System.out.println("\nSecret 3: "+code3);
		System.out.println("\nSecret 4: "+code4);
		System.out.println("\nSecret 5: "+code5);
		System.out.print("\nChecksum: ");
		for (int i=0; i<code1.length(); i++){
			
			if (i%9==8) {
				System.out.print(" ");
			} else {
				System.out.print((code1.charAt(i)+code2.charAt(i)+code3.charAt(i)+code4.charAt(i)+code5.charAt(i))>(240+22)?1:0);
			}
		}
	}
}
