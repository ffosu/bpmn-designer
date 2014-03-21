package org.ffosu.bpmn.designer.ui.propertysheet;

import org.eclipse.swt.widgets.Composite;
import org.ffosu.bpmn.designer.core.runtime.AbstractFixFlowComposite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class TestComposite extends AbstractFixFlowComposite {

	public TestComposite(Composite parent, int style) {
		super(parent, style);
		
		Label label = new Label(this, SWT.NONE);
		label.setBounds(10, 10, 54, 12);
		label.setText("哈哈");
	}
}
