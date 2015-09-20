package click.rmx.engine;

public interface Ticker {
	public long tick();
	public void updateTick(long newTick);
}
