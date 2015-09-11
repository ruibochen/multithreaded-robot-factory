import java.util.Random;
import java.util.concurrent.Semaphore;

public class Manufacturer implements Runnable{
	
	public final int PRODUCTION_TIME_MIN = 2000; 
	public final int PRODUCTION_TIME_MAX = 5000;
	public final int HALT_TIME = PRODUCTION_TIME_MAX;
	
	public String name = null;
	public Thread truck = null;
	public int count = 0; //track number of trucks created
	
	//Truck via Operate need access to set currentSurplusStatus for particular manufacturer
	public Semaphore surplusAccess = new Semaphore(1);
	public boolean currentSurplusStatus = false;

	public Manufacturer(String name){
		this.name = name;
	}
	
	public void run(){
		
		//while(Operate.getRunningStatus()){
		while (Operate.canOperate.tryAcquire()){
		
			try {
				//Operate.canOperate.acquire();
				if (surplus()){
					goSleep();
					changeSurplusFalse(); //change currentSurplusStatus back to false after sleep
				}
				producePart();
				sendTruck();

			} catch (Exception e) {//InterruptedException e
				e.printStackTrace();
			} finally {
				Operate.canOperate.release();
				if (Operate.getRunningStatus()==false){
					Operate.canOperate.acquireUninterruptibly();
				}
				
			}
			
		}
		Operate.msg("shop is now closed", name);
		
	}//end run
	
	public void sendTruck(){
		Thread truck = new Thread(new Truck(name+"Truck-"+count, name)); //first parameter is truck name, second parameter is part name
		count++;
		truck.start();
	}
	
	public void producePart(){
		int productionTime = (new Random()).nextInt(PRODUCTION_TIME_MAX - PRODUCTION_TIME_MIN + 1) + PRODUCTION_TIME_MIN;
		try {
			Operate.msg("is now being produced", name);
			Thread.sleep(productionTime);
			Operate.msg("finishes production", name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void goSleep(){
		try {
			Operate.msg("has surplus, go sleep", name);
			Thread.sleep(HALT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void changeSurplusFalse(){
		try {
			surplusAccess.acquire();
			currentSurplusStatus=false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			surplusAccess.release();
		}
	}
	
	public boolean surplus(){
		try {
			surplusAccess.acquire();
			if (currentSurplusStatus==true){ //if there is a surplus, return true
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			surplusAccess.release();
		}
		return false;
	}

	/*public boolean factoryIsOpen(){
		try {
			Operate.readTime.acquire();
			if ((System.currentTimeMillis()-Operate.time) < Operate.OPERATION_TIME){
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Operate.readTime.release();
		}
		
		return false;
	}*/

}
