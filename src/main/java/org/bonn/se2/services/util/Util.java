package org.bonn.se2.services.util;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.io.ByteArrayInputStream;

public class Util {

    public static Image convertToImg(final byte[] imageData) {
        StreamResource.StreamSource streamSource = (StreamResource.StreamSource) () ->
                (imageData == null) ? null : new ByteArrayInputStream(imageData);
        return new Image(null, new StreamResource(streamSource, "streamedSourceFromByteArray"));
    }

    public static HorizontalLayout createNoResultsWarning(String s) {
        HorizontalLayout noResultsLayout = new HorizontalLayout();
        noResultsLayout.setWidth("100%");
        noResultsLayout.setHeight("147px");
        noResultsLayout.addStyleName("search-components");

        Panel noResultsPanel = new Panel();
        noResultsPanel.setWidth("80%");
        noResultsPanel.setHeight("107px");
        noResultsLayout.addComponent(noResultsPanel);
        noResultsLayout.setComponentAlignment(noResultsPanel, Alignment.MIDDLE_CENTER);
        HorizontalLayout panelContent = new HorizontalLayout();
        panelContent.addStyleName("noresults-layout");
        panelContent.setSizeFull();
        Label noResultsLabel = new Label(VaadinIcons.INFO_CIRCLE_O.getHtml() + s, ContentMode.HTML);
        noResultsLabel.setWidth("100%");
        panelContent.addComponent(noResultsLabel);
        panelContent.setComponentAlignment(noResultsLabel, Alignment.MIDDLE_LEFT);
        noResultsPanel.setContent(panelContent);
        return noResultsLayout;

    }

}
