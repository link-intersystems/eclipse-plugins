package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.text.ParseException;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.quartz.CronExpression;

import com.link_intersystems.eclipse.swt.events.VerifyResultText;

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
		String verifyText = new VerifyResultText(e).toString();
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
