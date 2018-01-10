package modul5;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Class which has a threadpool with 20 threads. It got semaphores for each parking entrance to make sure they don't get cars when full.
 * @author drottsgard
 *
 */
public class Creator implements Runnable {
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
	ParkingHouse parking = new ParkingHouse(executor);
	private EntranceNorth north;
	private EntranceSouth south;
	private EntranceWest west;
	private EntranceEast east;
	private Semaphore northSem = new Semaphore(5, true);
	private Semaphore southSem = new Semaphore(5, true);
	private Semaphore westSem = new Semaphore(5, true);
	private Semaphore eastSem = new Semaphore(5, true);
	private Random rand = new Random();

	/**
	 * Constructor which initiate and also gives a few threads tasks.
	 */
	public Creator() {
		north = new EntranceNorth(parking, northSem);
		south = new EntranceSouth(parking, southSem);
		west = new EntranceWest(parking, westSem);
		east = new EntranceEast(parking, eastSem);
		executor.execute(north);
		executor.execute(south);
		executor.execute(west);
		executor.execute(east);
		executor.execute(this);
	}

	/**
	 * run-method which creates new cars and puts them in correct entrance.
	 */
	@Override
	public void run() {
		String color = "blue";
		CarInPark carToPark;
		int index = 0;
		while (true) {
			try {
				Car car = new Car(color);
				carToPark = new CarInPark((rand.nextInt(100) + 1) * 1000, index, car, parking);
				switch (rand.nextInt(4)) {
				case 0:
					// System.out.println("Lägger till bil i ENTRANCE_NORTH");
					northSem.acquire();
					north.put(carToPark);
					if (northSem.availablePermits() <= 0) {
						System.out.println("Fullt med bilar i north, parkera elsewhere");
					}
					break;
				case 1:
					// System.out.println("Lägger till bil i ENTRANCE_SOUTH");
					southSem.acquire();
					south.put(carToPark);
					if (southSem.availablePermits() <= 0) {
						System.out.println("Fullt med bilar i south, parkera elsewhere");
					}
					break;
				case 2:
					// System.out.println("Lägger till bil i ENTRANCE_WEST");
					westSem.acquire();
					west.put(carToPark);
					if (westSem.availablePermits() <= 0) {
						System.out.println("Fullt med bilar i west, parkera elsewhere");
					}
					break;
				case 3:
					// System.out.println("Lägger till bil i ENTRANCE_EAST");
					eastSem.acquire();
					east.put(carToPark);
					if (eastSem.availablePermits() <= 0) {
						System.out.println("Fullt med bilar i east, parkera elsewhere");
					}
					break;
				}

				Thread.sleep(rand.nextInt(5) + 1 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
		}

	}

	public static void main(String[] args) {
		new Creator();
	}

}
