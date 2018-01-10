package modul5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Class which also is a buffer, contatins the cars waiting to enter the parkinghouse.
 * Contains semaphore for the parkinghouse and also for it's own queue.
 * @author drottsgard
 *
 */
public class EntranceEast implements Runnable {
	private LinkedList<CarInPark> carsInQueue = new LinkedList<CarInPark>();
	private ParkingHouse parking;
	private Semaphore parkSem;
	private Semaphore eastSem;

	/**
	 * Constructor
	 * @param parking - the parkinghouse object
	 * @param eastSem - semaphore for this class and it's queue
	 */
	public EntranceEast(ParkingHouse parking, Semaphore eastSem) {
		this.parking = parking;
		this.eastSem = eastSem;
		parkSem = parking.getSem();
	}

	/**
	 * return method for car, when car is returned the semaphore is released to give slot for a new car in waiting queue
	 * @return - the car
	 * @throws InterruptedException
	 */
	public synchronized CarInPark get() throws InterruptedException {
		while (carsInQueue.isEmpty()) {
			wait();
		}
		CarInPark car = carsInQueue.removeFirst();
		eastSem.release();
		return car;
	}

	/**
	 * Method for putting car in queue. Everytime a car is put in queue it notifys other threads so they can read the queue.
	 * @param car - to put in queue
	 */
	public synchronized void put(CarInPark car) {
		carsInQueue.add(car);
		notifyAll();
		System.out.println("EASTKÖ ANTAL BILAR: " + carsInQueue.size());
	}

	/**
	 * Run-method which puts car in the parkinghouse if the parkinghouse is not full (using the parkinghouse semaphore)
	 */
	@Override
	public void run() {
		CarInPark car;
		while (true) {
			try {
				car = get();
				parkSem.acquire();
				parking.put(car);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
