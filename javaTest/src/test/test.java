package test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.xml.validation.Validator;

public class test extends Thread {
	a test = new a();
    public static void main(String[] args){
//        Thread t1 = new Thread(() -> {
//            System.out.println("t1 执行完成");
//        });
//        Thread t2 = new Thread(() -> {
//        	System.out.println("t2 执行完成");
//        });
//        t1.start();
//        t2.start();
    	t test = new t();
    	for(int i=0;i<50;i++) {
    		if(i%10==0&&i!=0) 
    			test.writeData(i);
    		else
    			test.readData();
    	}
    }
}

class a {
	private int data = 3;
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
		System.out.println("我在写"+Thread.currentThread());
	}
}

class t {
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	ReadLock readlock = lock.readLock();
	WriteLock writeLock = lock.writeLock();
	a test = new a();
	public void readData() {
		new Thread(()->{
			int d;
			readlock.lock();
			d=test.getData();
			System.out.println("读取的数据为:"+d+"  "+Thread.currentThread());
			readlock.unlock();
		}).start();
	}
	
	public void writeData(int data) {
		new Thread(()->{
			writeLock.lock();
			test.setData(data);
			writeLock.unlock();
		}).start();
	}
	
	public void readAndwrite() {
		new Thread(()->{
			int d;
			readlock.lock();
			d=test.getData();
			System.out.println("读取的数据为:"+d+"  "+Thread.currentThread());
			readlock.unlock();
			writeLock.lock();
			test.setData(d+1);
			writeLock.unlock();
		}).start();;
	}
}

