package es.alba.sweet.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import es.alba.sweet.core.AMessage;
import es.alba.sweet.core.MessageType;
import es.alba.sweet.core.Output;

public class OutputTextViewer {

	private int			MAX_CHARACTERS	= 8000;

	private Color		black;
	private Color		orange;
	private Color		red;

	private StyledText	textViewer;

	public OutputTextViewer(Composite parent, Output output) {
		this.black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		this.orange = new Color(Display.getCurrent(), 255, 127, 0);
		this.red = new Color(Display.getCurrent(), 255, 0, 0);

		textViewer = new StyledText(parent, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		textViewer.setEditable(false);
		output.getMessages().forEach(a -> add(a));
	}

	public void add(AMessage message) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (textViewer.getText().length() + message.toString().length() > MAX_CHARACTERS) {
					textViewer.replaceTextRange(0, textViewer.getLine(0).length() + 1, "");
				}

				StyleRange range = style(message);
				textViewer.append(message.toString());
				textViewer.setStyleRange(range);
			}
		});
	}

	private StyleRange style(AMessage message) {
		int start = textViewer.getText().length();
		int length = message.toString().length();

		MessageType type = message.getType();
		switch (type) {
		case INFO:
			return new StyleRange(start, length, black, null);
		case WARNING:
			return new StyleRange(start, length, orange, null);
		case ERROR:
			return new StyleRange(start, length, red, null);
		default:
			return new StyleRange(start, length, black, null);
		}

	}
}
