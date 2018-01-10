package modul5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class EntranceWest implements Runnable {
	private LinkedList<CarInPark> carsInQueue = new LinkedList<CarInPark>();
	private ParkingHouse parking;
	private Semaphore parkSem;
	private Semaphore westSem;

	public EntranceWest(ParkingHouse parking, Semaphore westSem) {
		this.parking = parking;
		this.westSem = westSem;
		parkSem = parking.getSem();
	}

	public synchronized CarInPark get() throws InterruptedException {
		while (carsInQueue.isEmpty()) {
			wait();
		}
		CarInPark car = carsInQueue.removeFirst();
		westSem.release();
		return car;
	}

	public synchronized void put(CarInPark car) {
		carsInQueue.add(car);
		notifyAll();
		System.out.println("WESTKÖ ANTAL BILAR: " + carsInQueue.size());
	}

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
