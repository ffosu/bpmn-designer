package org.ffosu.bpmn.designer.core.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

public class FixFlowDefaultPropertySection extends AbstractFixFlowPropertySection {
	protected List<Class> appliesToClasses = new ArrayList<Class>();

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		if (super.appliesTo(part, selection)) {
			if (appliesToClasses.isEmpty()) {
				return true;
			}

			PictogramElement pe = BusinessObjectUtil.getPictogramElementForSelection(selection);
			if (pe instanceof ConnectionDecorator) {
				pe = ((ConnectionDecorator) pe).getConnection();
				// this is a special hack to allow selection of connection
				// decorator labels:
				// the connection decorator does not have a business object
				// linked to it,
				// but its parent (the connection) does.
				if (pe.getLink() == null && pe.eContainer() instanceof PictogramElement)
					pe = (PictogramElement) pe.eContainer();

				// check all linked BusinessObjects for a match
				if (pe.getLink() != null) {
					for (EObject eObj : pe.getLink().getBusinessObjects()) {
						if (appliesTo(eObj))
							return true;
					}
				}
			}
			EObject eObj = BusinessObjectUtil.getBusinessObjectForSelection(selection);
			if (eObj != null)
				return appliesTo(eObj);
		}
		return false;
	}
	
	public boolean appliesTo(EObject eObj) {
		for (Class c : appliesToClasses) {
			if (c.isInstance(eObj))
				return true;
		}
		return false;
	}
	
	public void addAppliesToClass(Class c) {
		appliesToClasses.add(c);
	}
	
	protected EObject getBusinessObjectForSelection(ISelection selection) {
		return BusinessObjectUtil.getBusinessObjectForSelection(selection);
	}

	@Override
	protected AbstractFixFlowComposite createSectionRoot() {
		return null;
	}

	@Override
	public AbstractFixFlowComposite createSectionRoot(Composite parent, int style) {
		return null;
	}
}
