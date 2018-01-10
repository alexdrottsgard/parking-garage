package modul5;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class EntranceSouth implements Runnable {
	private LinkedList<CarInPark> carsInQueue = new LinkedList<CarInPark>();
	private ParkingHouse parking;
	private Semaphore parkSem;
	private Semaphore southSem;

	public EntranceSouth(ParkingHouse parking, Semaphore southSem) {
		this.parking = parking;
		this.southSem = southSem;
		parkSem = parking.getSem();
	}

	public synchronized CarInPark get() throws InterruptedException {
		while (carsInQueue.isEmpty()) {
			wait();
		}
		CarInPark car = carsInQueue.removeFirst();
		southSem.release();
		return car;
	}

	public synchronized void put(CarInPark car) {
		carsInQueue.add(car);
		notifyAll();
		System.out.println("SOUTHKÖ ANTAR BILAR: " + carsInQueue.size());
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
