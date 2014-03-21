package org.ffosu.bpmn.designer.core.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class FixFlowRuntime {
	public static final String FIXFLOW_EXTENSION_ID = "org.ffosu.fixflow.runtime";
	protected static FixFlowRuntime currentRuntime;
	protected static String currentTargetRuntime;
	
	public FixFlowRuntime() {
		
	}

	public static List<FixFlowTabDescriptor> getTabDescriptors() {
		List<FixFlowTabDescriptor> tabDescriptors = new ArrayList<FixFlowTabDescriptor>();
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(FIXFLOW_EXTENSION_ID);
		for (IConfigurationElement element : config) {
			if (element.getName().equals("propertyTab")) { //$NON-NLS-1$
				/*if (currentRuntime.getId().equals(TargetRuntime.DEFAULT_RUNTIME_ID)) {
					// already done
					continue;
				}*/
				FixFlowTabDescriptor td = new FixFlowTabDescriptor(element);
				FixFlowSectionDescriptor sd = new FixFlowSectionDescriptor(td,element);
				tabDescriptors.add(td);
			}
		}
		return tabDescriptors;
	}


	public static FixFlowRuntime getCurrentRuntime() {
		return currentRuntime;
	}


	public static void setCurrentRuntime(FixFlowRuntime currentRuntime) {
		FixFlowRuntime.currentRuntime = currentRuntime;
	}

}
