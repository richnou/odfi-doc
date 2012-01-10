package uni.hd.cag.eclipse.tools.docbook.builder;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ToggleNatureAction implements IObjectActionDelegate {

	private ISelection selection;

	
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		
		
		//-- Running this action of toggling on all selections projects
		if (selection instanceof IStructuredSelection) {
			
			//-- Go through
			Iterator it = ((IStructuredSelection) selection).iterator();
			while (it.hasNext()) {
				
				//-- Get the object and try to make it a project
				Object element = it.next();
				IProject project = null;
				
				if (element instanceof IProject) {
					project = (IProject) element;
					
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
				
				//-- If we gfound a project, toggle the nature
				if (project != null) {
					toggleNature(project);
				}
			}
		}
	}

	/**
	 * Toggles sample nature on a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 */
	private void toggleNature(IProject project) {
		
		try {
			//-- The nature is in the project description
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			
			//-- If we have a Docbook nature, remove it.
			for (int i = 0; i < natures.length; ++i) {
				System.out.println("Project has nature: "+natures[i]);
				if (DocbookNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i,
							natures.length - i - 1);
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
			
					
					return;
				}
			}
			
			//-- If we come here we can add one

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = DocbookNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	

}
