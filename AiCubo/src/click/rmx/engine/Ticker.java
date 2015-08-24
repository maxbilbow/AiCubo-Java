package click.rmx.engine;

import java.time.LocalTime;

public interface Ticker {
	public long tick();
	public void updateTick(long newTick);
}
