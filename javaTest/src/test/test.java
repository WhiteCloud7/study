package test;

public class test {
	int result = 0;
	public static void main(String[] args) {
		test test = new test();
		System.out.println(test.test(2));
	}
	
	public int test(int max){
		int current = 1;
		int cu = max;
		for(int i =0 ;i<cu;i++) {
			for(int j=1;j<=max;j++) {
				current =j*current;
			}
			max--;
			result = current + result;
		}
		return result;
	}
}
