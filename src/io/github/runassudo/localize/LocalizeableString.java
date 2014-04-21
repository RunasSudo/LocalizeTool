package io.github.runassudo.localize;

public class LocalizeableString {

	public int location, length; // length in bytes
	public Encoding encoding;
	public String original;
	public String translation;

	public boolean edited;

	public LocalizeableString() {
	}

	public LocalizeableString(int location, int length, Encoding encoding,
			String original) {
		this.location = location;
		this.length = length;
		this.encoding = encoding;
		this.original = original;
		this.translation = original;
	}

	public static enum Encoding {

		UCS2LE("UCS-2 (LE)"), SHIFTJIS("Shift JIS");

		private String pretty;

		Encoding(String pretty) {
			this.pretty = pretty;
		}

		public String getPrettyName() {
			return pretty;
		}

	}

}
