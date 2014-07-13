package com.link_intersystems.eclipse.tools.cron_expression.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;

public class RunActionDefaultSelectionAdapter extends SelectionAdapter {

	private IAction action;

	public RunActionDefaultSelectionAdapter(IAction action) {
		this.action = action;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				action.run();
			}
		});
	}
}
