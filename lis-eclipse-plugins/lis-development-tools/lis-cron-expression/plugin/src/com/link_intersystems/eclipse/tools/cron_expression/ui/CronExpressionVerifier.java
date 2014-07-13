package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.text.ParseException;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;
import org.quartz.CronExpression;

class CronExpressionVerifier implements VerifyListener {

	private CronExpression cronExpression;
	private ControlDecoration controlDecoration;
	private CronExpressionInputModel cronExpressionInputModel;

	public CronExpressionVerifier(ControlDecoration controlDecoration,
			CronExpressionInputModel cronExpressionInputModel) {
		this.controlDecoration = controlDecoration;
		this.cronExpressionInputModel = cronExpressionInputModel;
	}

	public void verifyText(VerifyEvent e) {
		Object source = e.getSource();
		Text text = (Text) source;
		String oldText = text.getText();
		String leftText = oldText.substring(0, e.start);
		String insertText = e.text;
		String rightText = oldText.substring(e.end, oldText.length());
		String verifyText = leftText + insertText + rightText;
		verifyText = verifyText.trim();
		if (verifyText.length() == 0) {
			setVerifyMessage(null);
			return;
		}
		try {
			cronExpression = new CronExpression(verifyText);
			cronExpressionInputModel.setCronExpression(cronExpression);
			setVerifyMessage(null);
		} catch (ParseException pe) {
			cronExpressionInputModel.setCronExpression(null);
			setVerifyMessage(pe.getMessage());
		}
	}

	private void setVerifyMessage(String message) {
		controlDecoration.setDescriptionText(message);
		controlDecoration.showHoverText(message);

		if (message == null) {
			controlDecoration.hide();
			controlDecoration.hideHover();
		} else {
			controlDecoration.show();
		}
	}

}
