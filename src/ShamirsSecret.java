import java.security.InvalidParameterException;
import java.util.Random;
import java.util.Vector;

public class ShamirsSecret {
	private static Random rand = new Random();
	private static int prime = 257;
	
	public static int ggT(int a, int b){
		if (b<0) b=-b;
		if (a==0) return b;
		if (a<0) a=-a;
		while (b != 0) if (a>b) a-=b; else b-=a;
		return a;
	}

	
	public static class Quotient{
		int divident;
		int divisor;

		public Quotient(int i) {
			this.divident=i;
			this.divisor=1;
		}
		
		public Quotient(int divident,int divisor) throws InvalidParameterException{
			if (divisor == 0) throw new InvalidParameterException("Divisor of quotient must not be zero!");
			int ggt = ggT(divident,divisor);
			if (divisor<0) ggt = -ggt;
			this.divident=divident/ggt;
			this.divisor=divisor/ggt;
		}
		
		public Quotient times(Quotient q) throws InvalidParameterException{
			return new Quotient(this.divident * q.divident,this.divisor * q.divisor);
		}
		
		public Quotient times(int m) throws InvalidParameterException{
			return new Quotient(this.divident * m,this.divisor);
		}
		
		@Override
		public String toString() {
			return (divisor == 1) ? ""+divident : "["+divident+"/"+divisor+"]";
		}

		public int val() throws InvalidParameterException {
			if (divisor != 1) throw new InvalidParameterException("Divisor should not be "+divisor);
			return divident;
		}
		
		public Quotient add(int i) throws InvalidParameterException{
			return add(new Quotient(i));
		}

		public Quotient add(Quotient q) throws InvalidParameterException {
			return new Quotient(divident*q.divisor + divisor*q.divident,divisor*q.divisor);			
		}
		
		public int mod(int q) throws InvalidParameterException {
			int inv = 0;
			while ((divisor*inv)%q !=1)inv++; // search the inverse of the divisor			
			return (divident*inv)%q;
		}
	}
	
	public static class Share{
		private int x,y;
		
		public Share(int x, int[] coefficients) {
			this.x=x;
			int sum=0;
			for (int i=0; i<coefficients.length; i++) sum+=coefficients[i]*pow(x,i);
			this.y=sum % prime;
		}
		
		@Override
		public String toString() {
			return "Secret(x = "+x+", y = "+y+")";
		}
	}
	
	private static int pow(int x,int e){
		return (e==0) ? 1 : x*pow(x,e-1);
	}
	
	private static Vector<Share> createSecrets(int secret, int count, int requires) {
		Vector<Share> result = new Vector<Share>();
		
		int [] coefficients = new int[requires];
		coefficients[0] = secret;
		for (int i=1; i<requires; i++) coefficients[i]=rand.nextInt(256);		
		for (int x=1; x<=count; x++) result.add(new Share(x, coefficients));
		return result;
	}
	
	private static int reconstruct(Vector<Share> shares) throws InvalidParameterException{
		Quotient sum = new Quotient(0);
		for (Share share_j:shares){
			Quotient prod = new Quotient(1);
			for (Share share_m:shares){
				if (share_j == share_m) continue;
				prod = prod.times(new Quotient(share_m.x,share_m.x-share_j.x));
			}
			sum = sum.add(prod.times(share_j.y));
		}		
		return sum.mod(prime);		
	}
	
	public static void main(String[] args) throws InvalidParameterException {
		int secret = rand.nextInt(256);
		System.out.println(secret);
		Vector<Share> shares = createSecrets(secret,6,3);		
		System.out.println(shares);
		
		shares.remove(1);
		shares.remove(2);
		shares.remove(3);
		System.out.println(shares);
		
		secret=reconstruct(shares);
		System.out.println(secret);
	}
}
