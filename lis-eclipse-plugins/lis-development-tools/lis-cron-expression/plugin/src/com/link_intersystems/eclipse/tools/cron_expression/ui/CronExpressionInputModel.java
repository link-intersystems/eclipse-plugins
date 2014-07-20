package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.util.Calendar;

import org.quartz.CronExpression;

public class CronExpressionInputModel extends AbstractModel {

	private CronExpression cronExpression;

	private int maxNextFireTimes = 10;

	private Calendar startDate = Calendar.getInstance();

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		propertyChangeSupport.firePropertyChange("startDate", this.startDate,
				this.startDate = startDate);
	}

	public CronExpression getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(CronExpression cronExpression) {
		propertyChangeSupport.firePropertyChange("cronExpression",
				this.cronExpression, this.cronExpression = cronExpression);
	}

	public int getMaxNextFireTimes() {
		return maxNextFireTimes;
	}

	public void setMaxNextFireTimes(int maxNextFireTimes) {
		propertyChangeSupport
				.firePropertyChange("maxNextFireTimes", this.maxNextFireTimes,
						this.maxNextFireTimes = maxNextFireTimes);
	}

}
