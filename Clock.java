class Clock {
	long elapsedTime;
	long lastTimeCheck;
	long deltaTime = 0;

	public Clock() {
		lastTimeCheck = System.currentTimeMillis();
		elapsedTime = 0;
	}

	public boolean update() {
		long currentTime = System.currentTimeMillis(); // get the current time
		deltaTime += currentTime - lastTimeCheck; // add to the elapsed time
		lastTimeCheck = currentTime;
		// System.out.println(deltaTime);
		if (deltaTime >= 1000) {
			deltaTime = 0;
			return true;
		}
		return false;
	}

	// return elapsed time in milliseconds
	public double getElapsedTime() {
		return elapsedTime / 1.0E9;
	}
}