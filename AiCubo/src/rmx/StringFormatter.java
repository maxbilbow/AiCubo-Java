package rmx;

public class StringFormatter {
	private String originalText;
	private String formattedText;
	
	public StringFormatter(String text) {
		this.newTextInput(text);
	}

	public StringFormatter() {
		this.newTextInput("");
	}
	
	public String getOriginalText() {
		return originalText;
	}

	public void newTextInput(String newText) {
		this.originalText = newText;
		this.setFormattedText(newText);
	}

	public String getFormattedText() {
		return formattedText;
	}

	/**
	 * Not to be called publically.
	 * @param formattedText
	 */
	protected void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}
	
	public String[] getWords() {
		return this.formattedText.split(" ");
	}
	
	public String getOriginal() {
		return this.originalText;
	}
	
	public void revertToOriginal() {
		this.newTextInput(this.originalText);
	}
	
	public void reverse() {
		String[] words = this.getWords();
		this.formattedText = words[words.length-1];
		for (int i=words.length-2; i>-1; --i) {
			this.formattedText += " " + words[i];
		}
	}
	
	@Override
	public String toString() {
		return this.formattedText;
	}
	
	public static StringFormatter testReverse() {
		StringFormatter sf = new StringFormatter();
		sf.newTextInput("One two three four five!");
		sf.reverse();
//		System.out.println(sf);
		return sf;
	}
}
