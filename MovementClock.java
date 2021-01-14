
class MovementClock {
	long elapsedTime;
	long lastTimeCheck;

	public MovementClock() {
		lastTimeCheck = System.nanoTime();
		elapsedTime = 0;
	}

	public void update() {
		long currentTime = System.nanoTime(); // if the computer is fast you
												// need more precision
		elapsedTime = currentTime - lastTimeCheck;
		lastTimeCheck = currentTime;
	}

	// return elapsed time in milliseconds
	public double getElapsedTime() {
		return elapsedTime / 1.0E9;
	}
}