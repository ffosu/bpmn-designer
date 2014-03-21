package org.ffosu.bpmn.designer.ui.propertysheet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.ffosu.bpmn.designer.core.runtime.AbstractFixFlowComposite;
import org.ffosu.bpmn.designer.core.runtime.AbstractFixFlowPropertySection;

public class TTT extends AbstractFixFlowPropertySection {

	public TTT() {
		
	}

	@Override
	protected AbstractFixFlowComposite createSectionRoot() {
		return new TestComposite(parent, SWT.None);
	}

	@Override
	public AbstractFixFlowComposite createSectionRoot(Composite parent, int style) {
		return new TestComposite(parent, SWT.None);
	}

	@Override
	protected EObject getBusinessObjectForSelection(ISelection selection) {
		return null;
	}

}
