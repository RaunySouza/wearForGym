package br.com.rauny.wearforgym.core.model;

/**
 * @author raunysouza
 */
public class Time {

	private long time;
	private String label;

	public Time() {}

	public Time(long time, String label) {
		this.time = time;
		this.label = label;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
