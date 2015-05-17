package br.com.rauny.wearforgym.core.model;

/**
 * @author raunysouza
 */
public class Time {

	public enum Unit {
		SECONDS("SEGUNDOS"),
		MINUTES("MINUTOS")
		;

		private String name;

		private Unit(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private long time;
	private String label;
	private Unit unit;

	public Time() {}

	public Time(long time, String label, Unit unit) {
		this.time = time;
		this.label = label;
		this.unit = unit;
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

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return String.format("%s %s", label, unit.getName());
	}
}
