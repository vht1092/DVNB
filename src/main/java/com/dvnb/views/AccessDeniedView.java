package com.dvnb.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "No permission to access this page";

	public AccessDeniedView() {
		setMargin(true);
		final Label lbl = new Label(MESSAGE);
		lbl.setStyleName(ValoTheme.LABEL_FAILURE);
		lbl.setSizeUndefined();
		addComponent(lbl);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("AccessDeniedView");
	}

}
