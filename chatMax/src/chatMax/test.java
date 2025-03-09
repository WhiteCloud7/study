package chatMax;

class A{
	public void a(){
		System.out.println("我是父类");
	}
	
	public void b(){
		System.out.println("我也是子类");
	}
}

class B extends A{
	public void a(){

		System.out.println("我是子类");
	}
	
	public void b(){
		System.out.println("我也是子类");
	}
}

public class test {
	public static void main(String[] args) {
		A a = new B();
		B b = new B();
		a.a();
	}
}
