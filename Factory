import java.util.concurrent.Semaphore;

public class Factory implements Runnable{
	
	public String name = null;
	public int count = 0;
	public int head = 0, torso = 0, hand = 0, leg = 0, broom = 0, robotCount = 0;

	//truckLine initialized to 1, only one truck can access Factory at a given time, rest is blocked
	public static Semaphore truckLine = new Semaphore(1, true); //number of trucks on line
	
	//factory hold truck, release when truck need to do go back
	//initialized to 0, Truck thread increments this when a truck is loaded into Factory
	public static Semaphore holdTruck = new Semaphore(0, true);
	
	//permit to access truckAtFactory
	public static Semaphore truckAtFactoryAccess = new Semaphore(1, true);
	public static Truck truckAtFactory = null; //the current truck at factory
	
	public Factory(String name){
		this.name = name;
	}
	
	public void run(){
		Operate.msg("is opened for business", name);
		
		while (factoryIsOpen()) { //while factory is open
			if (truckLineHasTruck()){ //if truck line is not empty
				try {
					holdTruck.acquire();
					if (truckAtFactory.getDeliveryAttemptNumber()==1){
						
						if (truckIsSurplus()){
							Operate.msg(truckAtFactory.getPart() + " has surplus", name);
							truckAtFactory.setDeliveryAttemptNumber(2);
							holdTruck.release(); //let Truck take action
							removeTruck(); //remove truck from front of line
						} else {
							acceptPartsFromTruck();
							Operate.msg("accepted " + truckAtFactory.getPart(), name);
							holdTruck.release(); //let Truck take action 
							removeTruck(); //remove truck from front of line
						}//end inner if block
					
					} else if (truckAtFactory.getDeliveryAttemptNumber()==2){
						acceptPartsFromTruck();
						Operate.msg("auto accepted " + truckAtFactory.getPart(), name);
						removeTruck(); //truck can die
					}//end block
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}//truckLineHasTruck
			
			if (canBuildRobot()){
				buildRobot();
			}
			
		}//factoryIsOpen
		
		Operate.msg("has " + head + "head, " + torso + "torso, " + hand + "hand, "
				+ leg + "leg, " + broom + "broom and built " + robotCount + " robot", name);
		
		Operate.msg("is now closed", name);
	
	}//end run
	
	public void removeTruck(){
		try {
			truckAtFactoryAccess.acquire();
			truckAtFactory = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			truckAtFactoryAccess.release();
		}
	}
	
	public void buildRobot(){
		head--;
		torso--;
		hand-=2;
		leg-=2;
		broom--;
		robotCount++;
		Operate.msg("built a robot WOOOOOOOOOOHOOOOOOOOOOOOOOOOOOOOOOOO " + robotCount, name);
	}
	
	public boolean canBuildRobot(){
		if (head >= 1 && torso >= 1 && hand >= 2 && leg >= 2 && broom >= 1){
			return true;
		}
		return false;
	}
	
	public void acceptPartsFromTruck() {
		if (truckAtFactory.getPart().equals("head")){
			head++;
		}
		if (truckAtFactory.getPart().equals("torso")){
			torso++;
		}
		if (truckAtFactory.getPart().equals("hand")){
			hand++;
		}
		if (truckAtFactory.getPart().equals("leg")){
			leg++;
		}
		if (truckAtFactory.getPart().equals("broom")){
			broom++;
		}
	}
	
	public boolean truckIsSurplus(){
		String currentSurplusPart = findCurrentSurplusPart();
		
		if (truckAtFactory.getPart().equals(currentSurplusPart)){
			return true;
		}

		return false;
	}
	
	public String findCurrentSurplusPart(){
		int max = head;
		String currentSurplusPart = "head";
		
		if (torso > max){
			max = torso;
			currentSurplusPart = "torso";
		}
		if ((hand/2) > max){
			max = hand/2;
			currentSurplusPart = "hand";
		}
		if ((leg/2) > max){
			max = leg/2;
			currentSurplusPart = "leg";
		}
		if (broom > max){
			max = broom;
			currentSurplusPart = "broom";
		}
		if (max < 2){
			currentSurplusPart = "";
		}
		return currentSurplusPart;
	}
	
	public boolean truckLineHasTruck(){
		try {
			truckAtFactoryAccess.acquire();
			if(truckAtFactory!=null){
				return true;
			} else {
				return false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			truckAtFactoryAccess.release();
		}
		System.out.println("Error at truckLineHasTruck()");
		return true;
	}

	public boolean factoryIsOpen(){
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
	}

}
