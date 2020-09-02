package org.bonn.se2.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class TextFieldWithButton extends CssLayout {

    private final AutocompleteTextField textField;
    private final Button button;

    public TextFieldWithButton() {
        setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        textField = new AutocompleteTextField();
        textField.setPlaceholder("Suchen...");
        textField.addStyleName("inline-icon");
        textField.setIcon(VaadinIcons.SEARCH);
        textField.setWidth("600px");

        button = new Button(VaadinIcons.CLOSE_SMALL);
        button.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS);

        button.addClickListener(e -> textField.setValue(""));

        addComponents(textField, button);
    }

    public AutocompleteTextField getTextField() {
        return textField;
    }

    public Button getButton() {
        return button;
    }

}
