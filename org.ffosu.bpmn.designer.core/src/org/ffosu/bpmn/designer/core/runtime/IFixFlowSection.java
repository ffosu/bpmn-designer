package org.ffosu.bpmn.designer.core.runtime;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public interface IFixFlowSection {
	public boolean appliesTo(IWorkbenchPart part, ISelection selection);
}
