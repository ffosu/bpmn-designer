/*******************************************************************************
 * Copyright (c) 2011, 2012 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
package org.ffosu.bpmn.designer.core.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;

public class FixFlowSectionDescriptor extends AbstractSectionDescriptor {

		protected String id;
		protected String tab;
		protected AbstractPropertySection sectionClass;
		protected List<Class> appliesToClasses = new ArrayList<Class>();
		protected String enablesFor;
		protected String filterClassName;
		protected FixFlowPropertySectionFilter filter;
		
		public FixFlowSectionDescriptor(FixFlowTabDescriptor td, IConfigurationElement e) {
			tab = td.getId();
			id = tab + ".section"; //$NON-NLS-1$

			try {
				String className = e.getAttribute("class"); //$NON-NLS-1$
				if ("default".equals(className)) { //$NON-NLS-1$
					sectionClass = new FixFlowDefaultPropertySection();
					/*if (e.getAttribute("features")!=null) { //$NON-NLS-1$
						String[] properties = e.getAttribute("features").split(" "); //$NON-NLS-1$ //$NON-NLS-2$
						((AbstractFixFlowPropertySection)sectionClass).setProperties(properties);
					}*/
				}
				else {
					sectionClass = (AbstractPropertySection) e.createExecutableExtension("class"); //$NON-NLS-1$
				}
				filterClassName = e.getAttribute("filter"); //$NON-NLS-1$
				if (filterClassName==null || filterClassName.isEmpty())
					filterClassName = "org.ffosu.bpmn.designer.core.runtime.FixFlowPropertySectionFilter"; //$NON-NLS-1$
				filter = (FixFlowPropertySectionFilter) Class.forName(filterClassName).getConstructor(null).newInstance(null);
				enablesFor = e.getAttribute("enablesFor"); //$NON-NLS-1$
				String type = e.getAttribute("type"); //$NON-NLS-1$
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			td.getSectionDescriptors().add(this);
		}
		
		public FixFlowSectionDescriptor(FixFlowTabDescriptor td, FixFlowSectionDescriptor that) {
			tab = td.getId();
			id = tab + ".section" + hashCode(); //$NON-NLS-1$
			this.sectionClass = that.sectionClass;
			this.appliesToClasses.addAll(that.appliesToClasses);
			this.enablesFor = that.enablesFor;
			this.filterClassName = that.filterClassName;
			this.filter = that.filter;
		}
		
		@Override
		public String getId() {
			return id;
		}

		@Override
		public ISection getSectionClass() {
			return sectionClass;
		}

		@Override
		public String getTargetTab() {
			return tab;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor#getEnablesFor()
		 * Returns the value of the "enablesFor" attribute of the configuration element in plugin.xml
		 * This is an integer value representing the number of items that must be selected for this
		 * Property Tab to be enabled.
		 */
		@Override
		public int getEnablesFor() {
			try {
				return Integer.parseInt(enablesFor);
			}
			catch (Exception ex) {
				
			}
			return super.getEnablesFor();
		}

		@Override
		public IFilter getFilter() {
			return new IFilter() {

				@Override
				public boolean select(Object toTest) {
					return false;
				}
				
			};
		}

		@Override
		public List getInputTypes() {
			return super.getInputTypes();
		}

		/**
		 * @param replacedId
		 * @param part
		 * @param selection
		 * @return
		 */
		public boolean doReplaceTab(String replacedId, IWorkbenchPart part, ISelection selection) {
			if (sectionClass instanceof IBpmn2PropertySection) {
				return ((IBpmn2PropertySection)sectionClass).doReplaceTab(replacedId, part, selection);
			}
			return appliesTo(part,selection);
		}
		
	}