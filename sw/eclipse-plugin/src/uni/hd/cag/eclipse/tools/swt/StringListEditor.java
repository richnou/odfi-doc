/**
 * 
 */
package uni.hd.cag.eclipse.tools.swt;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author rleys
 *
 */
public class StringListEditor extends ListEditor {

	
	protected boolean disableControlButtons = false;
	
	
	protected Composite parentComposite = null;
	
	/**
	 * 
	 */
	protected StringListEditor() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param name
	 * @param labelText
	 * @param parent
	 */
	public StringListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		this.parentComposite = parent;
	}


	
	public Composite getParent() {
		return parentComposite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
	 */
	@Override
	protected String createList(String[] items) {
		
		return TeaStringUtils.join(items, ";");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
	 */
	@Override
	protected String getNewInputObject() {
		
		//-- Show dialog for new Value
		//--  * Dialog has a special file button
		//---------------------------------
		InputDialog newstring = new InputDialog(this.getShell(), "Enter a new value","", "", null) {

			protected Text getTextComponent() {
				return this.getText();
			}
			
			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				// TODO Auto-generated method stub
				super.createButtonsForButtonBar(parent);
				
				//-- File Selection Button
				Button fileSelectionButton = this.createButton(this.getShell(), IDialogConstants.CLIENT_ID+1, "Choose File", false);
				fileSelectionButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						
						System.out.println("Opening File Selection");
						
						//-- Open File Selection View
						FileDialog dialog = new FileDialog (getShell(), SWT.OPEN);
						String [] filterNames = new String [] {"XMLFiles"};
						String [] filterExtensions = new String [] {"*.xml"};
						dialog.setFilterNames (filterNames);
						dialog.setFilterExtensions (filterExtensions);
						//dialog.b
						String selectedFile = dialog.open();
						
						//-- Put result in box
						getTextComponent().setText(selectedFile);
					}
				});
	
			}
			
			
		};
		newstring.setBlockOnOpen(true);
		
		if (newstring.open()==InputDialog.OK) {
			
			//-- return new value
			if (newstring.getValue().length()>0)
				return newstring.getValue();
			
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
	 */
	@Override
	protected String[] parseString(String stringList) {
		// TODO Auto-generated method stub
		return stringList.split(";");
	}

	/**
	 * Hides/Show the buttons to add and change order of the elements in the string list
	 * @return 
	 */
	public boolean isDisableControlButtons() {
		return disableControlButtons;
	}

	/**
	 * Hides/Show the buttons to add and change order of the elements in the string list
	 * @param disableControlButtons
	 */
	public void setDisableControlButtons(boolean disableControlButtons) {
		this.disableControlButtons = disableControlButtons;
		if (this.isDisableControlButtons()) {
			super.getAddButton().getParent().setSize(0, 0);
			super.getAddButton().getParent().setVisible(false);
		}
			
			
	}

	
	
}
