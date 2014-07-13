package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.text.DateFormat;
import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

public class CronExpressionViewPart extends ViewPart {
	public CronExpressionViewPart() {
	}

	private FormToolkit toolkit;
	private ScrolledForm form;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		GridData gd = null;

		CronExpressionInputModel cronExpressionInputModel = new CronExpressionInputModel();

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Cron Expression Evaluation");
		Composite formBody = form.getBody();
		formBody.setLayout(layout);

		Label label = new Label(formBody, SWT.NULL);
		label.setText("Cron Expression:");

		Text cronExpressionText = new Text(formBody, SWT.BORDER);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		cronExpressionText.setLayoutData(gd);

		ControlDecoration controlDecoration = new ControlDecoration(
				cronExpressionText, SWT.LEFT | SWT.TOP);

		CronExpressionVerifier cronExpressionVerifier = new CronExpressionVerifier(
				controlDecoration, cronExpressionInputModel);
		CronExpressionResultModel cronExpressionResultModel = new CronExpressionResultModel();

		cronExpressionText.addVerifyListener(cronExpressionVerifier);

		Image image = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
				.getImage();
		controlDecoration.setImage(image);

		ExpressionEvaluationAction evalAction = new ExpressionEvaluationAction(
				cronExpressionInputModel, cronExpressionResultModel);
		evalAction.setText("evaluate");
		ActionContributionItem evalActionContributionItem = new ActionContributionItem(
				evalAction);

		evalActionContributionItem.fill(formBody);
		Control control = (Control) evalActionContributionItem.getWidget();
		toolkit.adapt(control, true, true);

		toolkit.createLabel(formBody, "Max results");

		Spinner spinner = new Spinner(formBody, SWT.BORDER);
		toolkit.adapt(spinner);
		toolkit.paintBordersFor(spinner);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

		Composite composite = new Composite(formBody, SWT.NONE);
		composite.setLayout(new FillLayout());
		gd = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1);
		composite.setLayoutData(gd);

		ListViewer resultListViewer = new ListViewer(composite, SWT.BORDER
				| SWT.V_SCROLL);

		IStructuredContentProvider contentProvider = new ArrayContentProvider();
		resultListViewer.setContentProvider(contentProvider);

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM, Locale.getDefault());
		DateFormatLabelProvider lp = new DateFormatLabelProvider(df);
		resultListViewer.setLabelProvider(lp);

		List list = resultListViewer.getList();
		toolkit.adapt(list, true, true);

		RunActionDefaultSelectionAdapter runActionDefaultSelectionAdapter = new RunActionDefaultSelectionAdapter(
				evalAction);
		cronExpressionText
				.addSelectionListener(runActionDefaultSelectionAdapter);

		DataBindingContext bindingContext = new DataBindingContext();
		IObservableValue viewerInput = ViewersObservables
				.observeInput(resultListViewer);
		IObservableValue result = BeansObservables.observeValue(
				cronExpressionResultModel, "nextFireTimes");
		bindingContext.bindValue(viewerInput, result);

		ISWTObservableValue maxNextFireCount = SWTObservables
				.observeSelection(spinner);
		IObservableValue observeValue = BeansObservables.observeValue(
				cronExpressionInputModel, "maxNextFireTimes");
		UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy();
		bindingContext.bindValue(maxNextFireCount, observeValue,
				updateValueStrategy, updateValueStrategy);

	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

}
