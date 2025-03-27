package test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class test {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        // 创建并启动多个线程
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resource.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (resource) {
                    if (resource.value <= 3) {
                        resource.notify(); 
                    }
                    resource.increment();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

class SharedResource {
    int value = 0;

    public void increment() {
        // 尝试获取锁
        synchronized (this) {
            try {
                // 获取锁成功，访问和修改资源
                value++;
                if (value == 3) {
                    this.wait(); // 当前线程在 resource 对象上等待
                }
                System.out.println(Thread.currentThread().getName() + " 增加后的值: " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class mytheard extends Thread{
	@Override
	public void run() {
		try {
			sleep(10);
			System.out.println("theard"+Thread.currentThread());
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}

class mytheard2 extends Thread{
	@Override
	public void run() {
		try {
			wait();
			System.out.println("theard"+Thread.currentThread());
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}