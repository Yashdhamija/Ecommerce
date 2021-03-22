package bean;

public class CounterBean {
	int counter;
	public CounterBean() {
		counter = 0;
	}
	
	public int updateCounter() {
		return counter++;
	}
	
	public int resetCounter() {
		return this.counter = 0;
	}
	
	public int getCounter() {
		return this.counter;
	}
}
