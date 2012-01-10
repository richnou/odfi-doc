/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder.properties;

import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.PropertyPage;

import uni.hd.cag.eclipse.tools.docbook.DocbookPlugin;
import uni.hd.cag.eclipse.tools.docbook.stylesheets.StylesheetsLoader;
import uni.hd.cag.eclipse.tools.swt.TeaStringUtils;

/**
 * @author rleys
 * 
 */
public class DocbookBuilderPropertyPage extends PropertyPage {

	protected Table stylesheetsTable;

	/**
	 * 
	 */
	public DocbookBuilderPropertyPage() {
		this.setTitle("Docbook Builder Configuration");
		this.setDescription("Choose the Document type you would like to output from the stylesheet list");
	}

	@Override
	public boolean isValid() {
		// this.getElement()

		/*
		 * IAdaptable adaptable = this.getElement();
		 * System.out.println("Properties on: "+adaptable); if (adaptable
		 * instanceof IFile) {
		 * 
		 * //-- System.out.println("Properties on a File: "+adaptable); IFile
		 * file = (IFile) adaptable;
		 * 
		 * 
		 * return false; }
		 */

		return super.isValid();
	}

	@Override
	protected Control createContents(Composite parent) {

		// -- Our Page's Composite
		Composite page = new Composite(parent, SWT.NONE);
		page.setLayout(new GridLayout());

		// The actual value of choosen stylesheets
		// --------------
		IFile file = (IFile) this.getElement();
		String choosenStylesheetsProperty = "";
		try {
			choosenStylesheetsProperty = file
					.getPersistentProperty(new QualifiedName(
							DocbookPlugin.PLUGIN_ID, "stylesheet"));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// -- Convert to a list
		LinkedList<String> choosenStylesheets = new LinkedList<String>();
		if (choosenStylesheetsProperty != null) {
			choosenStylesheets.addAll(Arrays.asList(choosenStylesheetsProperty
					.split(" ")));
		}

		// Prepare table
		// -----------------
		this.stylesheetsTable = new Table(page, SWT.BORDER | SWT.CHECK);
		this.stylesheetsTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		// this.stylesheetsTable.setItemCount(StylesheetsLoader.getInstance().getPossibleStylesheetsArray().length);
		this.stylesheetsTable.setHeaderVisible(true);

		// -- Column 0 selects with checkbox
		TableColumn c0 = new TableColumn(this.stylesheetsTable, SWT.CENTER);
		c0.setText("Selected");
		c0.setWidth(100);
		c0.setResizable(true);

		// -- Column 1 shows name
		TableColumn c1 = new TableColumn(this.stylesheetsTable, SWT.CENTER);
		c1.setText("Stylesheet uid");
		c1.setWidth(200);
		c1.setResizable(true);

		// -- Add a row per available stylesheet
		for (String str : StylesheetsLoader.getInstance()
				.getPossibleStylesheetsArray()) {

			TableItem it = new TableItem(this.stylesheetsTable, NONE);
			if (choosenStylesheets.contains(str))
				it.setChecked(true);
			else
				it.setChecked(false);
			it.setText(1, str);

		}

		// this.stylesheetsTable.layout();

		/*
		 * 
		 * 
		 * 
		 * 
		 * // Choice //----------------------- stylesheetChoice = new Text(page,
		 * SWT.NONE); stylesheetChoice.setLayoutData(new GridData(SWT.LEFT,
		 * SWT.CENTER, true, false, 1, 1));
		 * stylesheetChoice.setMessage("Stylesheet repository id + stylesheet name"
		 * );
		 * stylesheetChoice.setText(choosenStylesheet==null?"":choosenStylesheet
		 * );
		 * 
		 * ContentProposalAdapter proposal = new
		 * ContentProposalAdapter(stylesheetChoice,new
		 * TextContentAdapter(),StylesheetsLoader
		 * .getInstance().getPossibleStylesheetsProvider(),null,null);
		 * 
		 * stylesheetSelectionCombo = new Combo(page, SWT.NONE);
		 * stylesheetSelectionCombo.setLayoutData(new GridData(SWT.FILL,
		 * SWT.CENTER, true, false, 1, 1));
		 * stylesheetSelectionCombo.setItems(StylesheetsLoader
		 * .getInstance().getPossibleStylesheetsArray());
		 * stylesheetSelectionCombo.add("", 0);
		 * 
		 * // Preselect in combo box if already chosen int count = 0; for(String
		 * str : StylesheetsLoader.getInstance().getPossibleStylesheetsArray())
		 * { if (str.equals(choosenStylesheet)) {
		 * stylesheetSelectionCombo.select(count+1); } count++; }
		 * 
		 * // Stylesheet list //------------------------- StylesheetsListTree
		 * stylesheetsTree = new StylesheetsListTree(page); GridData
		 * gd_stylesheetsTree = new GridData(SWT.LEFT, SWT.TOP, true, true, 1,
		 * 1); gd_stylesheetsTree.heightHint = 255;
		 * stylesheetsTree.setLayoutData(gd_stylesheetsTree);
		 * stylesheetsTree.setSize(stylesheetsTree.computeSize(SWT.DEFAULT,
		 * SWT.DEFAULT));
		 */
		page.layout();
		return page;
	}

	@Override
	protected void performApply() {
		// TODO Auto-generated method stub
		super.performApply();

		// Record new value of selected Stylesheet
		IFile file = (IFile) this.getElement();

		// GO through table and recover all set stylesheets
		LinkedList<String> stylesheets = new LinkedList<String>();
		for (TableItem item : this.stylesheetsTable.getItems()) {
			if (item.getChecked())
				stylesheets.add(item.getText(1));
		}

		try {
			file.setPersistentProperty(new QualifiedName(
					DocbookPlugin.PLUGIN_ID, "stylesheet"), TeaStringUtils
					.join(stylesheets.toArray(new String[stylesheets.size()]),
							" "));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
