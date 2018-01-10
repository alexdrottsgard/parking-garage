package modul5;

/**
 * Object class which represents a car for the parkinghouse with its parkingtime 
 * @author drottsgard
 *
 */
public class CarInPark {
	private int timeParked;
	private int index;
	private Car car;
	private ParkingHouse pHouse;
	
	public CarInPark(int timeParked, int index, Car car, ParkingHouse parkinghouse) {
		this.timeParked = timeParked;
		this.index = index;
	}
	
	/**
	 * @return - time the car is going to be parked
	 */
	public int getTimeParked() {
		return timeParked;
	}
 	
	/**
	 * @return - number of the car
	 */
	public int getIndex() {
		return index;
	}

}
