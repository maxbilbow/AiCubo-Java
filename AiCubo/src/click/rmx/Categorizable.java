package click.rmx;


public interface Categorizable {
	default String getCategoryName() {
		return this.getClass().getSimpleName();
	}
}
