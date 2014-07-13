package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CronExpressionResultModel extends AbstractModel {

	private List<Date> nextFireTimes = new ArrayList<Date>();

	public List<Date> getNextFireTimes() {
		return nextFireTimes;
	}

	public void setNextFireTimes(List<Date> nextFireTimes) {
		propertyChangeSupport.firePropertyChange("nextFireTimes",
				this.nextFireTimes, this.nextFireTimes = nextFireTimes);
	}

}
