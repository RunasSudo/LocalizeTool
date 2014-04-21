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

		ASCII("US-ASCII"), UTF8("UTF-8"), SHIFTJIS("Shift_JIS"), UCS2LE(
				"UCS-2LE"), UCS2BE("UCS-2BE"), UTF16LE("UTF-16LE"), UTF16BE(
				"UTF-16BE");

		private String charset;

		Encoding(String charset) {
			this.charset = charset;
		}

		public String getCharset() {
			return charset;
		}

	}

}
