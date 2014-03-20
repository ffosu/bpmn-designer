package org.ffosu.bpmn.designer.ui.propertysheet;

import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;

public class FixLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getImage(Object element) {
		EObject object = getBusinessObject(element);
		if (object!=null) {
			CustomTaskDescriptor ctd = getCustomTaskDescriptor(object); 
			if (ctd!=null) {
				return PropertyUtil.getImage("CustomTask"); //$NON-NLS-1$
			}
			return PropertyUtil.getImage(object);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		EObject object = getBusinessObject(element);
		if (object!=null) {
			String text = ModelUtil.getDisplayName(object);
			// check if this is a CustomTask
			CustomTaskDescriptor ctd = getCustomTaskDescriptor(object); 
			if (ctd!=null) {
				// it is! build the property tab title from the CustomTask name
				// and the object's name (which could be the same)
				String name = ctd.getName();
				if (!text.equals(name))
					return name + ": " + text; //$NON-NLS-1$
			}
			return text;
		}
//		PictogramElement pe = BusinessObjectUtil.getPictogramElementForSelection((ISelection)element);
//		if (pe!=null && pe.getGraphicsAlgorithm()!=null) {
//			return ModelUtil.getLabel( pe.getGraphicsAlgorithm() );
//		}
		return null;
	}

	private EObject getBusinessObject(Object element) {
		if (element instanceof ISelection) {
			return BusinessObjectUtil.getBusinessObjectForSelection((ISelection)element);
		}
		else if (element instanceof EObject) {
			return (EObject) element;
		}
		return null;
		
	}
	
	private CustomTaskDescriptor getCustomTaskDescriptor(EObject object) {
		TargetRuntime rt = getTargetRuntime(object);
		if (rt!=null) {
			for (CustomTaskDescriptor ctd : rt.getCustomTasks()) {
				if (ctd.getFeatureContainer()!=null) {
					String id = ctd.getFeatureContainer().getId(object);
					if (id!=null) {
						return ctd;
					}
				}
			}
		}
		return null;
	}
	
	private TargetRuntime getTargetRuntime(EObject object) {
		DiagramEditor editor = ModelUtil.getDiagramEditor(object);
		if (editor!=null) {
			return (TargetRuntime) editor.getAdapter(TargetRuntime.class);
		}
		return null;
	}
}
