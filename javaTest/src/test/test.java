package test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.concurrent.locks.StampedLock;

import javax.xml.validation.Validator;

public class test extends Thread {
	a test = new a();
    public static void main(String[] args){
    	Semaphore semaphore = new Semaphore(1);
//    	StampedLock lock = new StampedLock();
//        Thread t1 = new Thread(() -> {
//            long stamp = lock.writeLock();
//            System.out.println("休眠");
//            try {
//                sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("释放");
//            lock.unlockWrite(stamp);
//        });
//        Thread t2 = new Thread(() -> {
//            long optimisticStamp = lock.tryOptimisticRead();
//            try {
//                sleep(50); // 等待 t1 线程获取写锁
//                if (lock.validate(optimisticStamp)) {
//                    System.out.println("没写！");
//                } else {
//                    System.out.println("写了！");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        t1.start();
//        t2.start();
    	new Thread(()->{
    		try {
				semaphore.acquire();
				System.out.println("得到许可并休眠");
				sleep(100);
				semaphore.release();
				System.out.println("释放许可");
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
    	}).start();
    	new Thread(()->{
    		try {
    			semaphore.acquire();
				System.out.println("得到许可并休眠");
				sleep(100);
				semaphore.release();
				System.out.println("释放许可");
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
    	}).start();
//    	t test = new t();
//    	for(int i=0;i<50;i++) {
//    		test.readAndwrite();
//    	}
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
	StampedLock stampedLock = new StampedLock();
	a test = new a();
	public void readData() {
		new Thread(()->{
			long stamp = stampedLock.readLock();
			int d;
//			readlock.lock();
			d=test.getData();
			System.out.println("读取的数据为:"+d+"  "+Thread.currentThread());
//			readlock.unlock();
			stampedLock.unlockRead(stamp);
		}).start();
	}
	
	public void writeData(int data) {
		new Thread(()->{
			long stamp = stampedLock.writeLock();
			int d;
//			writeLock.lock();
			d=test.getData();
			test.setData(data);
//			writeLock.unlock();
			stampedLock.unlockWrite(stamp);
		}).start();
	}
	
	public void readAndwrite() {
		new Thread(()->{
			int d;
			long stamp = stampedLock.writeLock();
//			writeLock.lock();
			d=test.getData();
			System.out.println("读取的数据为:"+d+"  "+Thread.currentThread());
			test.setData(d+1);
			stamp = stampedLock.tryConvertToReadLock(stamp);
			if(stamp != 0L) {
				System.out.println("读操作");
			}
			stampedLock.unlockRead(stamp);
//			readlock.lock();
//			readlock.unlock();
//			writeLock.unlock();
		}).start();
	}
	
	public void readData1() {
		new Thread(()->{
			synchronized(this) {
				int d;
				d=test.getData();
				System.out.println("读取的数据为:"+d+"  "+Thread.currentThread());
			}
		}).start();;
	}
	
	public synchronized void writeData1(int data) {
		new Thread(()->{
			synchronized(this) {
				int d;
				d=test.getData();
				test.setData(data);
			}
		}).start();;
	}
}

