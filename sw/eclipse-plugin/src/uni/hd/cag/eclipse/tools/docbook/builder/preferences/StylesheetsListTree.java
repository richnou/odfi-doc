/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.odfi.collaboration.docbook.core.StylesheetRepository;
import org.odfi.collaboration.docbook.core.StylesheetsLoader;
import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet;

import uni.hd.cag.eclipse.tools.docbook.DocbookPlugin;

/**
 * This class is a GUi component presenting the list of stylesheets present in Loader
 * 
 * @author rtek
 *
 */
public class StylesheetsListTree extends Composite {

	private Tree listTree = null;
	
	/**
	 * @param parent
	 * @param style
	 */
	public StylesheetsListTree(Composite parent) {
		super(parent,SWT.NONE);
		
		listTree = new Tree(this,SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		listTree.setHeaderVisible(true);
		
		TreeColumn stylesheetNameColumn = new TreeColumn(listTree, SWT.LEFT);
		stylesheetNameColumn.setText("Name");
		stylesheetNameColumn.setWidth(300);
		TreeColumn stylesheetBaseElementColumn = new TreeColumn(listTree, SWT.CENTER);
		stylesheetBaseElementColumn.setText("Docbook Element");
		stylesheetBaseElementColumn.setWidth(300);
		
		
		layout();
		
	}
	
	public void refresh() {
		
		//listTree.clearAll(true);
		listTree.removeAll();
		
		//-- Fill up sheets
		for (StylesheetRepository repository : DocbookPlugin.getDefault().getStylesheetsLoader().getRepositories()) {
			
			//-- Add Tree Item for Repository
			TreeItem repositoryItem = new TreeItem(listTree,SWT.NONE);
			repositoryItem.setText(repository.getId().getValue());
			
			//-- Add Contained Stylesheets
			for (Stylesheet stylesheet : repository.getStylesheet()) {
				
				TreeItem stylesheetItem = new TreeItem(repositoryItem,SWT.NONE);
				stylesheetItem.setText(stylesheet.getName().getValue());
				
			}
			
		}
		
		listTree.setSize(listTree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
	}

}
