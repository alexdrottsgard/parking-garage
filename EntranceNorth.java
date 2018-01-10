package modul5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class EntranceNorth implements Runnable {
	private LinkedList<CarInPark> carsInQueue = new LinkedList<CarInPark>();
	private ParkingHouse parking;
	private Semaphore parkSem;
	private Semaphore northSem;

	public EntranceNorth(ParkingHouse parking, Semaphore northSem) {
		this.parking = parking;
		this.northSem = northSem;
		parkSem = parking.getSem();
	}

	public synchronized CarInPark get() throws InterruptedException {
		while (carsInQueue.isEmpty()) {
			wait();
		}
		CarInPark car = carsInQueue.removeFirst();
		northSem.release();
		return car;
	}

	public synchronized void put(CarInPark car) {
		carsInQueue.add(car);
		notifyAll();
		System.out.println("NORDKÖ ANTAL BILAR:" + carsInQueue.size());
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
