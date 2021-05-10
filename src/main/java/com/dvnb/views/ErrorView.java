package com.dvnb.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class ErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private transient Label lbError;
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorView.class);

	public ErrorView() {
		setMargin(true);
		lbError = new Label();
		lbError.addStyleName(ValoTheme.LABEL_FAILURE);
		lbError.setSizeUndefined();
		addComponent(lbError);
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		lbError.setValue(String.format("View not found: %s", event.getViewName()));
		LOGGER.info("View not found: " + event.getViewName());
	}

}
