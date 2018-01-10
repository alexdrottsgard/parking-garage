package modul5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * Class that represents a parkinghouse, is also a buffer which use semaphores so it does not get full.
 * @author drottsgard
 *
 */
public class ParkingHouse {
	private LinkedList<CarInPark> carsParked = new LinkedList<CarInPark>();
	private Semaphore parkSem = new Semaphore(50, true);
	private ThreadPoolExecutor executor;

	/**
	 * Constructor
	 * @param executor - to be able to give threads tasks.
	 */
	public ParkingHouse(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	/**
	 * Put method which puts cars in the parkinghouse. When a car is put in the house, a thread is given a task in exit to remove that car
	 * when its parking time is up.
	 * @param car - to put in the parkinghouse and to be removed after X time
	 */
	public synchronized void put(CarInPark car) {
		carsParked.add(car);
		System.out.println("P-HUS ANTAL BILAR: " + carsParked.size());
		System.out.println("Bil nummer: " + car.getIndex() + " ska vara i garaget " + car.getTimeParked() + " sekunder");
		executor.execute(new Exit(car, this));
	}

	/**
	 * Get method which removes car from queue if it is in the queue. When the car is removed it also releases a semaphore so that new cars
	 * can get into the parkinghouse
	 * @param car - to get from the parkinghouse
	 * @return - car from parkinghouse
	 * @throws InterruptedException
	 */
	public synchronized CarInPark get(CarInPark car) throws InterruptedException {
		boolean b = carsParked.remove(car);
		if (b) {
			parkSem.release();
			System.err.println("Bil nummer: " + car.getIndex() + " lämnade garaget");
			System.out.println("P-HUS PLATSER KVAR: " + parkSem.availablePermits());
		} else {
			System.out.println("not good");
		}
		return car;
	}

	/**
	 * 
	 * @return - parkinghouse semaphore
	 */
	public Semaphore getSem() {
		return parkSem;
	}

}
