/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uni.hd.cag.eclipse.tools.docbook.DocbookPlugin;
import uni.hd.cag.eclipse.tools.docbook.stylesheets.StylesheetsListTree;
import uni.hd.cag.eclipse.tools.docbook.stylesheets.StylesheetsLoader;
import uni.hd.cag.eclipse.tools.swt.StringListEditor;
import uni.hd.cag.eclipse.tools.swt.TeaCompositeVBox;
import uni.hd.cag.eclipse.tools.swt.URIListEditor;

/**
 * @author rleys
 * 
 */
public class DocBookPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * 
	 */
	public DocBookPreferencePage() {
		super("Docbook", GRID);

		this.setPreferenceStore(DocbookPlugin.getDefault().getPreferenceStore());

	}

	/*
	 * /* (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		this.setDescription("Configure the sources for stylesheets (internal or external)");
		System.out.println("Initializing preferences of Plugin");
	}

	

	@Override
	protected Control createContents(Composite parent) {
		
		TeaCompositeVBox page = new TeaCompositeVBox(parent,0);
		

		//-- Create Fields from parent
		Control fieldsControl = super.createContents(page);
		
		
		//-- Create a tree to show all the stylesheets
		//-----------------------------
		StylesheetsListTree stylesheetsTree = new StylesheetsListTree(page);
		
		
		page.pack();
		return page;
	}

	@Override
	protected void createFieldEditors() {

		// -------- Show List Reload from ENV variable DOCBOOK_STYLESHEETS_REPS
		StringListEditor externalRepsEnvList = new StringListEditor("reps.external.env",
				"Stylesheets repositories extracted from Environment variable DOCBOOK_STYLESHEETS_REPS",this.getFieldEditorParent());
		externalRepsEnvList.setDisableControlButtons(true);
		
		this.addField(externalRepsEnvList);
		
		
		
		// externalEnvRepsLabel.setText("Stylesheets repositories extracted from Environment variable DOCBOOK_STYLESHEETS_REPS");

		// -------- Edit the list of additional repositories
		URIListEditor externalRepsAddedList = new URIListEditor("reps.external.added",
				"Stylesheets repositories added in preferences",this.getFieldEditorParent());
		externalRepsAddedList.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println("Changed list of added reps");
				
			}
		});
		this.addField(externalRepsAddedList);
	}

	@Override
	public boolean performOk() {
		
		// Reload Stylesheets
		StylesheetsLoader.getInstance().load();
		return super.performOk();
	}

	@Override
	protected void performApply() {
		// Reload Stylesheets
		StylesheetsLoader.getInstance().load();
		super.performApply();
	}
	
	

}
