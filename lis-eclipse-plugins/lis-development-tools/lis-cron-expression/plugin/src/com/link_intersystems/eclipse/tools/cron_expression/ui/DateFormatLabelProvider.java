package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.text.DateFormat;

import org.eclipse.jface.viewers.LabelProvider;

public class DateFormatLabelProvider extends LabelProvider {

	private DateFormat dateFormat;

	public DateFormatLabelProvider(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String getText(Object element) {
		return dateFormat.format(element);
	}
}
