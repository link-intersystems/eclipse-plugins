package com.link_intersystems.eclipse.tools.cron_expression.ui;

import java.text.DateFormat;
import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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

import com.link_intersystems.eclipse.swt.DatePicker;

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
		try {
			KeyStroke keyStroke = KeyStroke.getInstance("Ctrl"
					+ KeyStroke.KEY_DELIMITER + "Space");
			IContentProposalProvider proposalProvider = new CronExpressionProposalProvider();
			String autoActivationCharacters = "";
			ContentProposalAdapter contentProposalAdapter = new ContentProposalAdapter(
					cronExpressionText, new TextContentAdapter(),
					proposalProvider, keyStroke,
					autoActivationCharacters.toCharArray());
			contentProposalAdapter.setPopupSize(new Point(200, 150));
			contentProposalAdapter.setPropagateKeys(true);
			contentProposalAdapter
					.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		} catch (ParseException e) {
		}

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

		toolkit.createLabel(formBody, "Start date");

		DatePicker startDateTextField = new DatePicker(formBody, SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		
		toolkit.createLabel(formBody, "Next fire times");
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

		DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
		DateFormatLabelProvider lp = new DateFormatLabelProvider(dateTimeFormat);
		resultListViewer.setLabelProvider(lp);

		List list = resultListViewer.getList();
		toolkit.adapt(list, true, true);

		RunActionDefaultSelectionAdapter runActionDefaultSelectionAdapter = new RunActionDefaultSelectionAdapter(
				evalAction);
		cronExpressionText
				.addSelectionListener(runActionDefaultSelectionAdapter);

		DataBindingContext bindingContext = new DataBindingContext();

		bindNextFireTimes(bindingContext, cronExpressionResultModel,
				resultListViewer);

		bindMaxFireTimes(bindingContext, cronExpressionInputModel, spinner);

		bindStartDate(bindingContext, cronExpressionInputModel,
				startDateTextField);

	}

	private void bindStartDate(DataBindingContext bindingContext,
			CronExpressionInputModel cronExpressionInputModel,
			DatePicker startDateTextField) {
		IObservableValue startDateTextFieldValue = BeansObservables
				.observeValue(startDateTextField, "date");

		IObservableValue startDateModelValue = BeansObservables.observeValue(
				cronExpressionInputModel, "startDate");

		bindingContext.bindValue(startDateTextFieldValue, startDateModelValue);
	}

	private void bindMaxFireTimes(DataBindingContext bindingContext,
			CronExpressionInputModel cronExpressionInputModel, Spinner spinner) {
		ISWTObservableValue maxNextFireCount = SWTObservables
				.observeSelection(spinner);
		IObservableValue observeValue = BeansObservables.observeValue(
				cronExpressionInputModel, "maxNextFireTimes");
		UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy();
		bindingContext.bindValue(maxNextFireCount, observeValue,
				updateValueStrategy, updateValueStrategy);
	}

	private void bindNextFireTimes(DataBindingContext bindingContext,
			CronExpressionResultModel cronExpressionResultModel,
			ListViewer resultListViewer) {
		IObservableValue viewerInput = ViewersObservables
				.observeInput(resultListViewer);
		IObservableValue result = BeansObservables.observeValue(
				cronExpressionResultModel, "nextFireTimes");
		bindingContext.bindValue(viewerInput, result);
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
