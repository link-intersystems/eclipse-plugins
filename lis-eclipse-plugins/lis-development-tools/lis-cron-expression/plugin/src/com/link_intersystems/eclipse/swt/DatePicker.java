package com.link_intersystems.eclipse.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

/**
 * DatePicker code obtained from: Bug 19945 DCR: date/time widget would be
 * useful https://bugs.eclipse.org/bugs/show_bug.cgi?id=19945#c12
 */
public class DatePicker extends Composite {

	private Text dateText = null;

	private Button pickButton = null;

	private Calendar date = null;

	private Shell pickerShell = null;

	private DatePickerPanel datePickerPanel = null;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public DatePicker(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	private void initialize() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.verticalSpacing = 2;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 2;
		gridLayout.makeColumnsEqualWidth = false;
		this.setLayout(gridLayout);
		setSize(new org.eclipse.swt.graphics.Point(95, 28));
		dateText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		dateText.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		dateText.setLayoutData(gridData1);
		pickButton = new Button(this, SWT.ARROW | SWT.DOWN);
		pickButton.setLayoutData(gridData);
		pickButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				Display display = Display.getCurrent();
				showDatePicker(display.getCursorLocation().x,
						display.getCursorLocation().y);
				Event event = new Event();
				event.widget = DatePicker.this;
				event.type = SWT.Modify;
				notifyListeners(SWT.Modify, event);
				updateDateText();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	public Calendar getDate() {
		if (date == null) {
			date = new GregorianCalendar();
		}
		return (Calendar) date.clone();
	}

	public void setDate(Calendar date) {
		this.propertyChangeSupport.firePropertyChange("date", this.date, this.date = (Calendar) date.clone());
		updateDateText();
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		this.layout();
		return new Point(this.getSize().x, this.getSize().y);
	}

	private void showDatePicker(int x, int y) {
		Display display = Display.getCurrent();
		pickerShell = new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP);
		pickerShell.setText("Shell");
		pickerShell.setLayout(new FillLayout());
		datePickerPanel = new DatePickerPanel(pickerShell, SWT.NONE);
		datePickerPanel.setDate(getDate());
		pickerShell.setSize(new Point(225, 180));
		pickerShell.setLocation(new Point(x, y));
		pickerShell.open();

		while (!pickerShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		setDate(datePickerPanel.getDate());
		pickerShell.dispose();
	}

	private void updateDateText() {
		dateText.setText(DateFormat.getDateInstance().format(getDate().getTime()));
		// dateText.setText(date.get(Calendar.DAY_OF_MONTH) + "/"
		// + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR));
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul> <li>ERROR_NULL_ARGUMENT - if the listener is null
	 *                </ul>
	 * @exception SWTException
	 *                <ul> <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
		dateText.addModifyListener(listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Modify, listener);
		dateText.removeModifyListener(listener);
	}

} // @jve:decl-index=0:visual-constraint="10,10"

class DatePickerPanel extends Composite {

	private Combo monthCombo = null;

	private Spinner yearSpinner = null;

	private Composite headerComposite = null;

	private Composite calendarComposite = null;

	private Calendar date = null;

	private DateFormatSymbols dateFormatSymbols = null;

	private Label[] calendarLabels = null;

	public DatePickerPanel(Composite parent, int style) {
		super(parent, style);
		initialize();
		updateCalendar();
	}

	private void initialize() {
		date = new GregorianCalendar();
		dateFormatSymbols = new DateFormatSymbols();
		calendarLabels = new Label[42];
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 3;
		gridLayout.verticalSpacing = 3;
		this.setLayout(gridLayout);
		setSize(new org.eclipse.swt.graphics.Point(277, 200));
		createCombo();
		createYearSpinner();
		createComposite();
		createComposite1();
		createCalendarData();
	}

	/**
	 * This method initializes combo
	 * 
	 */
	private void createCombo() {
		monthCombo = new Combo(this, SWT.READ_ONLY);
		monthCombo.setItems(dateFormatSymbols.getMonths());
		monthCombo.remove(12);
		monthCombo.select(date.get(Calendar.MONTH));
		monthCombo.setVisibleItemCount(12);
		monthCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				date.set(Calendar.MONTH, monthCombo.getSelectionIndex());
				updateCalendar();
			}

		});
	}

	private void createYearSpinner() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData1.grabExcessVerticalSpace = false;
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.heightHint = -1;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		yearSpinner = new Spinner(this, SWT.BORDER | SWT.READ_ONLY);
		yearSpinner.setMinimum(1900);
		yearSpinner.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		yearSpinner.setDigits(0);
		yearSpinner.setMaximum(3000);
		yearSpinner.setLayoutData(gridData1);
		yearSpinner.setSelection(date.get(Calendar.YEAR));
		yearSpinner.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				date.set(Calendar.YEAR, yearSpinner.getSelection());
				updateCalendar();
			}

		});
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		String[] weekDays = dateFormatSymbols.getWeekdays();
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 7;
		gridLayout1.makeColumnsEqualWidth = true;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		headerComposite = new Composite(this, SWT.NONE);
		headerComposite.setLayoutData(gridData);
		headerComposite.setLayout(gridLayout1);
		GridData labelGridData = new org.eclipse.swt.layout.GridData();
		labelGridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		labelGridData.grabExcessHorizontalSpace = true;
		labelGridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		for (int i = 1; i < 8; ++i) {
			Label headerLabel = new Label(headerComposite, SWT.CENTER);
			headerLabel.setText(weekDays[i].substring(0, 3));
			headerLabel.setLayoutData(labelGridData);
		}
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 7;
		gridLayout2.makeColumnsEqualWidth = true;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalSpan = 2;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		calendarComposite = new Composite(this, SWT.BORDER);
		calendarComposite.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		calendarComposite.setLayout(gridLayout2);
		calendarComposite.setLayoutData(gridData1);
	}

	private void createCalendarData() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		for (int i = 0; i < 42; ++i) {
			Label headerLabel = new Label(calendarComposite, SWT.CENTER);
			headerLabel.setText("99");
			headerLabel.setLayoutData(gridData);
			calendarLabels[i] = headerLabel;
			headerLabel.addMouseListener(new MouseListener() {

				public void mouseDoubleClick(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				public void mouseDown(MouseEvent arg0) {
					unSellectAll();
					Label label = (Label) arg0.getSource();
					if (!label.getText().equals("")) {
						label.setBackground(Display.getDefault()
								.getSystemColor(SWT.COLOR_LIST_SELECTION));
						label.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_WHITE));
					}
				}

				public void mouseUp(MouseEvent arg0) {
					Label label = (Label) arg0.getSource();
					if (!label.getText().equals("")) {
						date.set(Calendar.YEAR, yearSpinner.getSelection());
						date.set(Calendar.MONTH, monthCombo.getSelectionIndex());
						date.set(Calendar.DAY_OF_MONTH,
								Integer.parseInt(label.getText()));
						DatePickerPanel.this.getShell().close();
					}
				}

			});
		}
	}

	private void updateCalendar() {
		unSellectAll();
		// Fill Labels
		Calendar cal = new GregorianCalendar(date.get(Calendar.YEAR),
				date.get(Calendar.MONTH), 1);
		int dayofWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

		for (int i = 0; i < dayofWeek; ++i) {
			calendarLabels[i].setText("");
		}

		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
			calendarLabels[i + dayofWeek - 1].setText("" + i);
		}

		for (int i = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + dayofWeek; i < 42; ++i) {
			calendarLabels[i].setText("");
		}

		calendarLabels[date.get(Calendar.DAY_OF_MONTH) + dayofWeek]
				.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_LIST_SELECTION));
		calendarLabels[date.get(Calendar.DAY_OF_MONTH) + dayofWeek]
				.setForeground(Display.getDefault().getSystemColor(
						SWT.COLOR_WHITE));

	}

	private void unSellectAll() {
		for (int i = 0; i < 42; ++i) {
			calendarLabels[i].setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_BLACK));
			calendarLabels[i].setBackground(Display.getCurrent()
					.getSystemColor(SWT.COLOR_WHITE));
		}
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
		updateCalendar();
	}

} // @jve:decl-index=0:visual-constraint="10,10"