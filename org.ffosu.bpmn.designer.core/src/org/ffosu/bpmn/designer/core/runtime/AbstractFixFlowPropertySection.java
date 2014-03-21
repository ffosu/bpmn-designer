package org.ffosu.bpmn.designer.core.runtime;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractFixFlowPropertySection extends GFPropertySection implements IFixFlowSection{
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected Composite parent;
	protected DiagramEditor editor;
	private IWorkbenchWindow cachedWorkbenchWindow;
	
	public AbstractFixFlowPropertySection() {
		cachedWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (cachedWorkbenchWindow != null) {
			cachedWorkbenchWindow.getPartService().addPartListener(
				partActivationListener);
		}
	}
	
	protected abstract AbstractFixFlowComposite createSectionRoot();
	public abstract AbstractFixFlowComposite createSectionRoot(Composite parent, int style);
	protected abstract EObject getBusinessObjectForSelection(ISelection selection);
	
	public AbstractFixFlowComposite getSectionRoot() {
		AbstractFixFlowComposite sectionRoot = null;
		if (parent!=null && !parent.isDisposed()) {
			if (parent.getChildren().length==0) {
				sectionRoot = createSectionRoot();
				sectionRoot.setLayoutData(new GridLayout());
			}
			sectionRoot = (AbstractFixFlowComposite)parent.getChildren()[0];
		}
		return sectionRoot;
	}
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
		this.parent = parent;
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Object bpmn2Editor = part.getAdapter(DiagramEditor.class);
		if (bpmn2Editor instanceof DiagramEditor) {
			editor = (DiagramEditor)bpmn2Editor;
		}
	}

	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();
	}

	@Override
	public void aboutToBeHidden() {
		super.aboutToBeHidden();
	}

	@Override
	public void refresh() {
		EObject be = getBusinessObjectForSelection(getSelection());
		
		if (be!=null) {
			AbstractFixFlowComposite sectionRoot = getSectionRoot();
			if (sectionRoot!=null) {
				if (sectionRoot.getBusinessObject() != be) {
					sectionRoot.setDiagramEditor((DiagramEditor) getDiagramEditor());
					if (!parent.isLayoutDeferred())
						parent.setLayoutDeferred(true);
					sectionRoot.setBusinessObject(be);
					if (parent.isLayoutDeferred())
						parent.setLayoutDeferred(false);
				}
				sectionRoot.refresh();
			}
		}
	}

	private IPartListener partActivationListener = new IPartListener() {

		public void partActivated(IWorkbenchPart part) {
			Object bpmn2Editor = part.getAdapter(DiagramEditor.class);
			if (bpmn2Editor instanceof DiagramEditor) {
				editor = (DiagramEditor)bpmn2Editor;
			}
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};

	@Override
	public void dispose() {
		super.dispose();
		if (cachedWorkbenchWindow != null) {
			cachedWorkbenchWindow.getPartService().removePartListener(
				partActivationListener);
			cachedWorkbenchWindow = null;
		}
	}

	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	public void setTabbedPropertySheetPage(TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}

	public Composite getParent() {
		return parent;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public DiagramEditor getEditor() {
		return editor;
	}

	public void setEditor(DiagramEditor editor) {
		this.editor = editor;
	}

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		if (part instanceof ContentOutline) {
			ContentOutline outline = (ContentOutline)part;
			IContributedContentsView v = (IContributedContentsView)outline.getAdapter(IContributedContentsView.class);
			if (v!=null)
				part = v.getContributingPart();
		}
		editor = (DiagramEditor)part.getAdapter(DiagramEditor.class);
		
		if (editor!=null) {
			return true;
		}
		return false;
	}
}
