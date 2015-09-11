import java.util.concurrent.Semaphore;

public class Truck implements Runnable{
	
	public String name = null;
	public String partName = null;
	
	//any operation on this truck must acquire permit
	public Semaphore truckAccess = new Semaphore(1);
	public int deliveryAttemptNumber = 1;

	Truck(String name, String partName){
		this.name = name;
		this.partName = partName;
	}
	
	public void run(){
		Operate.msg("is now enroute to factory ", name);
		goToFactory();
		if(getDeliveryAttemptNumber()==2){
			goToFactory(); //going to back of line
		}
	}//end run
	
	public void goToFactory(){
		try {
			Factory.truckLine.acquire();
			getOnFactoryLine();
			Operate.msg("arrives at factory", name);
			if (getDeliveryAttemptNumber()==1){
				waitForFactoryCommand();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Factory.truckLine.release();
		}
	}//goToFactory
	
	public void waitForFactoryCommand(){
		try {
			Factory.holdTruck.acquire();
			if (getDeliveryAttemptNumber()==2){
				Operate.msg("tells manufacturer there is surplus", name);
				Operate.setSurplus(partName);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void getOnFactoryLine(){
		try {
			Factory.truckAtFactoryAccess.acquire();
			Factory.truckAtFactory = this;
			Factory.holdTruck.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Factory.truckAtFactoryAccess.release();
		}
	}
	
	public int getDeliveryAttemptNumber(){
		try {
			truckAccess.acquire();
			return deliveryAttemptNumber;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			truckAccess.release();
		}
		System.out.println("ERROR in getDeliveryAttemptNumber");
		return 0;
	}
	
	public void setDeliveryAttemptNumber(int n){
		try {
			truckAccess.acquire();
			deliveryAttemptNumber = n;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			truckAccess.release();
		}
	}
	
	public String getPart(){
		try {
			truckAccess.acquire();
			return partName;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			truckAccess.release();
		}
		System.out.println("ERROR in getDeliveryAttemptNumber");
		return partName;
	}
	
}
