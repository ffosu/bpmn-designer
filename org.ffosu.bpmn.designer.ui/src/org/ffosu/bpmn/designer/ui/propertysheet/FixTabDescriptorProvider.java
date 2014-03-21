package org.ffosu.bpmn.designer.ui.propertysheet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AdvancedPropertySection;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;
import org.ffosu.bpmn.designer.core.runtime.FixFlowRuntime;
import org.ffosu.bpmn.designer.core.runtime.FixFlowSectionDescriptor;
import org.ffosu.bpmn.designer.core.runtime.FixFlowTabDescriptor;

public class FixTabDescriptorProvider implements ITabDescriptorProvider {
	private List<FixFlowTabDescriptor> tabDescriptorList = new ArrayList<FixFlowTabDescriptor>();
	Hashtable<EObject, List<FixFlowTabDescriptor>> tabDescriptorListMap = new Hashtable<EObject, List<FixFlowTabDescriptor>>();

	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part, ISelection selection) {
		// is the Tab Descriptor List already in our cache?
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForSelection(selection);
		if (businessObject != null) {
			tabDescriptorList = tabDescriptorListMap.get(businessObject);
			if (tabDescriptorList != null) {
				// Yes! return it.
				return tabDescriptorList.toArray(new ITabDescriptor[tabDescriptorList.size()]);
			}
		}

		// No, we need build the list: get the Target Runtime <propertyTab>
		// contributions
		// and merge with the Default Runtime Tab Descriptors
		TargetRuntime rt = TargetRuntime.getDefaultRuntime();
		Object bpmn2Editor = part.getAdapter(BPMN2Editor.class);
		if (bpmn2Editor instanceof BPMN2Editor) {
			rt = ((BPMN2Editor) bpmn2Editor).getTargetRuntime(this);
		}

		List<FixFlowTabDescriptor> desc = null;
		desc = FixFlowRuntime.getTabDescriptors();

		// do tab replacement
		ArrayList<FixFlowTabDescriptor> replaced = new ArrayList<FixFlowTabDescriptor>();
/*		for (FixFlowTabDescriptor d : desc) {
			String replacedId = d.getReplaceTab();
			if (replacedId != null) {
				String[] replacements = replacedId.split(" "); //$NON-NLS-1$
				// tab replacement is only done if the replacement tab has
				// section descriptors
				// that want the replacement to happen.
				for (String id : replacements) {
					for (Bpmn2SectionDescriptor s : (List<Bpmn2SectionDescriptor>) d.getSectionDescriptors()) {
						// if (s.appliesTo(part, selection))
						{
							// ask the section if it wants to replace this tab
							if (s.doReplaceTab(id, part, selection)) {
								// replace the tab whose ID is specified as
								// "replaceTab" in this tab.
								FixFlowTabDescriptor replacedTab = TargetRuntime.findTabDescriptor(id);
								if (replacedTab != null) {
									replaced.add(replacedTab);
									int i = desc.indexOf(replacedTab);
									if (i >= 0) {
										desc.set(i, d);
									}
								}
							}
						}
					}
				}
			}
		}*/
		if (replaced.size() > 0)
			desc.removeAll(replaced);

		for (int i = desc.size() - 1; i >= 0; --i) {
			FixFlowTabDescriptor d = desc.get(i);
			for (int j = i - 1; j >= 0; --j) {
				if (desc.get(j) == d) {
					desc.remove(i);
					break;
				}
			}
		}

		// remove empty tabs
		// also move the advanced tab to end of list
		ArrayList<FixFlowTabDescriptor> emptyTabs = new ArrayList<FixFlowTabDescriptor>();
		FixFlowTabDescriptor advancedPropertyTab = null;
		for (FixFlowTabDescriptor d : desc) {
			boolean empty = true;
			for (FixFlowSectionDescriptor s : (List<FixFlowSectionDescriptor>) d.getSectionDescriptors()) {
				if (s.appliesTo(part, selection)) {
					empty = false;
				}
				if (s.getSectionClass() instanceof AdvancedPropertySection) {
					advancedPropertyTab = d;
				}
			}
			if (empty) {
				emptyTabs.add(d);
			}
		}

//		if (emptyTabs.size() > 0)
//			desc.removeAll(emptyTabs);

		if (advancedPropertyTab != null) {
			if (desc.remove(advancedPropertyTab))
				desc.add(advancedPropertyTab);
		}

		// make copies of all tab descriptors to prevent cross-talk between
		// editors
		replaced.clear(); // we'll just reuse an ArrayList from before...
		for (FixFlowTabDescriptor td : desc) {
			// Note that the copy() makes the Tab Descriptor IDs and Section IDs
			// unique.
			// This is important because the TabbedPropertySheetPage uses these
			// IDs to
			// look up the Sections.
			replaced.add(td.copy());
		}

		// save this in the cache.
		if (businessObject != null) {
			tabDescriptorList = new ArrayList<FixFlowTabDescriptor>();
			tabDescriptorList.addAll(replaced);
			tabDescriptorListMap.put(businessObject, tabDescriptorList);
			return tabDescriptorList.toArray(new ITabDescriptor[tabDescriptorList.size()]);
		}

		return replaced.toArray(new ITabDescriptor[replaced.size()]);
	}

	/**
	 * This should be called by the editor during dispose() to remove all the
	 * items from the cache.
	 * 
	 * @param resource
	 *            - the EMF Resource containing the EObjects for which Property
	 *            Tab Descriptors were built.
	 */
	public void disposeTabDescriptors(Resource resource) {
		if (resource != null) {
			TreeIterator<EObject> iter = resource.getAllContents();
			while (iter.hasNext()) {
				EObject object = iter.next();
				tabDescriptorListMap.remove(object);
			}
		}
	}
}
