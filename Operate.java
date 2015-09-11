import java.util.concurrent.Semaphore;

public class Operate {
	
	public static Thread head, torso, hand, leg, broom, factory;
	public static Manufacturer m0, m1, m2, m3, m4; 
	
	//only 1 thread can access the shared time variables
	public static Semaphore readTime = new Semaphore(1);
	public static final long OPERATION_TIME = 25000;
	public static long time = System.currentTimeMillis();
	
	public static Semaphore canOperate = new Semaphore(5, true);
	
	public static Semaphore operationStatus = new Semaphore(1);
	public static boolean runningStatus = true;
	
	public Operate(){

	}
	
	public void startOperations(){
		initializeThreads();
		startThreads();
		
		while(!timeToClose()){
		}
		
		msg("running out of time", "Operation");
		
		changeRunningStatus(false);
	}
	
	public static boolean getRunningStatus(){
		operationStatus.acquireUninterruptibly();
		boolean status = runningStatus;
		operationStatus.release();
		return status;
	}
	
	public static void changeRunningStatus(boolean status){
		try {
			operationStatus.acquire();
			runningStatus = status;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			operationStatus.release();
		}
	}
	
	public static boolean timeToClose(){
		boolean returnStatus;
		readTime.acquireUninterruptibly();
			if ((OPERATION_TIME/5) <= (OPERATION_TIME-(System.currentTimeMillis()-time))){
				returnStatus = false;
			} else {
				returnStatus = true;
			}
		readTime.release();
		return returnStatus;
	}
	
	public static void msg(String m, String name) {
		try {
			readTime.acquire();
			System.out.println("["+(System.currentTimeMillis()-time)+"] "+name+": "+m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			readTime.release();
		}
	}
	
	public static void setSurplus(String partName){
		if (partName.equals("head")){
			m0IsSurplus();	
		}
		if (partName.equals("torso")){
			m1IsSurplus();	
		}
		if (partName.equals("hand")){
			m2IsSurplus();	
		}
		if (partName.equals("leg")){
			m3IsSurplus();	
		}
		if (partName.equals("broom")){
			m4IsSurplus();	
		}	
	}//setSurplus
	
	public static void initializeThreads(){
		factory = new Thread(new Factory("factory"));
		head = new Thread(m0 = new Manufacturer("head"));
		torso = new Thread(m1 = new Manufacturer("torso"));
		hand =  new Thread(m2 = new Manufacturer("hand"));
		leg =  new Thread(m3 = new Manufacturer("leg"));
		broom =  new Thread(m4 = new Manufacturer("broom"));
	}
	
	public static void startThreads(){
		factory.start();
		head.start();
		torso.start();	
		hand.start();
		leg.start();
		broom.start();
	}
	
	public static void m0IsSurplus(){
		try {
			m0.surplusAccess.acquire();
			m0.currentSurplusStatus=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m0.surplusAccess.release();
		}
	}
	
	public static void m1IsSurplus(){
		try {
			m1.surplusAccess.acquire();
			m1.currentSurplusStatus=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m1.surplusAccess.release();
		}
	}
	
	public static void m2IsSurplus(){
		try {
			m2.surplusAccess.acquire();
			m2.currentSurplusStatus=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m2.surplusAccess.release();
		}
	}
	
	public static void m3IsSurplus(){
		try {
			m3.surplusAccess.acquire();
			m3.currentSurplusStatus=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m3.surplusAccess.release();
		}
	}
	
	public static void m4IsSurplus(){
		try {
			m4.surplusAccess.acquire();
			m4.currentSurplusStatus=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m4.surplusAccess.release();
		}
	}

}
