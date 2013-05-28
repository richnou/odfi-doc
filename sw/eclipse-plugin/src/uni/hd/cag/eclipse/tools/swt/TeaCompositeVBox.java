/**
 * 
 */
package uni.hd.cag.eclipse.tools.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author rleys
 *
 */
public class TeaCompositeVBox extends Composite {

	/**
	 * @param parent
	 * @param style
	 */
	public TeaCompositeVBox(Composite parent, int style) {
		super(parent, style);
		
		//-- Add A Vertical row layout
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		this.setLayout(layout);
		
	}

}
