package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.quartz.CronExpression;

public class ExpressionEvaluationAction extends Action {

	private Date startAt = new Date();
	private CronExpressionInputModel cronExpressionInputModel;
	private CronExpressionResultModel cronExpressionResultModel;

	public ExpressionEvaluationAction(
			CronExpressionInputModel cronExpressionInputModel,
			CronExpressionResultModel cronExpressionResultModel) {
		this.cronExpressionInputModel = cronExpressionInputModel;
		this.cronExpressionResultModel = cronExpressionResultModel;
	}

	public void setStartDate(Date startDate) {
		this.startAt = (Date) startDate.clone();
	}

	@Override
	public void run() {
		CronExpression cronExpressionObj = cronExpressionInputModel
				.getCronExpression();
		if (cronExpressionObj == null) {
			return;
		}
		List<Date> nextFireDates = new ArrayList<Date>();
		Date startAt = this.startAt;
		int nextFireTimesCount = cronExpressionInputModel.getMaxNextFireTimes();
		for (int i = 0; i < nextFireTimesCount; i++) {
			Date nextValidTimeAfter = cronExpressionObj
					.getNextValidTimeAfter(startAt);
			nextFireDates.add(nextValidTimeAfter);
			startAt = nextValidTimeAfter;
		}
		cronExpressionResultModel.setNextFireTimes(nextFireDates);

	}
}
