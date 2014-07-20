package com.link_intersystems.eclipse.swt.events;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Text;

public class VerifyResultText implements CharSequence {

	private VerifyEvent verifyEvent;

	private String resultText;

	public VerifyResultText(VerifyEvent verifyEvent) {
		Validate.notNull(verifyEvent, "verifyEvent must not be null");
		this.verifyEvent = verifyEvent;
	}

	public int length() {
		return toString().length();
	}

	public char charAt(int index) {
		return toString().charAt(index);
	}

	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}

	@Override
	public String toString() {
		if (resultText == null) {
			Object source = verifyEvent.getSource();
			Text text = (Text) source;
			String oldText = text.getText();
			String leftText = oldText.substring(0, verifyEvent.start);
			String insertText = verifyEvent.text;
			String rightText = oldText.substring(verifyEvent.end,
					oldText.length());
			String resultText = leftText + insertText + rightText;
			resultText = resultText.trim();
			this.resultText = resultText;
		}
		return resultText;
	}
}
