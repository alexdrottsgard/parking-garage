package modul5;

/**
 * Class which removes car from the parkinghouse.
 * @author drottsgard
 *
 */
public class Exit implements Runnable {
	private ParkingHouse parking;
	private CarInPark car;

	/**
	 * Constructor
	 * @param car - to remove
	 * @param parkingHouse - the parkinghouse object
	 */
	public Exit(CarInPark car, ParkingHouse parkingHouse) {
		this.parking = parkingHouse;
		this.car = car;
	}

	/**
	 * Run-method which at starts goes into sleep for the amount of time the car is supposed to be parked, after that removes the car.
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(car.getTimeParked());
			parking.get(car);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
