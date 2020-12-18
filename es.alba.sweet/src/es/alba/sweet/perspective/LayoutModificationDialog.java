package es.alba.sweet.perspective;

import org.eclipse.nebula.widgets.opal.dialog.Dialog;
import org.eclipse.nebula.widgets.opal.dialog.Dialog.OpalDialogType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class LayoutModificationDialog {

	public LayoutModificationDialog() {
		final Dialog dialog = new Dialog();
		dialog.setTitle("Application Error");
		dialog.getMessageArea().setTitle("CRASH AND BURN !").//
				setText("The application has performed an illegal action. This action has been logged and reported.").//
				setIcon(Display.getCurrent().getSystemImage(SWT.ICON_ERROR));
		dialog.setButtonType(OpalDialogType.OK);
		dialog.getFooterArea().setExpanded(false).addCheckBox("Don't show me this error next time", true).setDetailText("More explanations to come...");
		dialog.getFooterArea().setFooterText("Your application crashed because a developer forgot to write a unit test");
		// .setIcon(new Image(null, OpalDialogType.class.getResourceAsStream("warning.png")));
		dialog.show();
	}
}
