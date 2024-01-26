package dev.shept.views.addsnippet;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Add Snippet")
@Route(value = "dashboard/add")
@RouteAlias(value = "")
@Uses(Icon.class)
public class AddSnippetView extends Composite<VerticalLayout> {

    public AddSnippetView() {
        RichTextEditor richTextEditor = new RichTextEditor();
        Button buttonPrimary = new Button();
        Paragraph textLarge = new Paragraph();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        richTextEditor.setWidth("100%");
        richTextEditor.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        textLarge.setText("N/A");
        textLarge.setWidth("100%");
        textLarge.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        buttonSecondary.setText("Copy URL");
        buttonSecondary.setWidth("min-content");
        getContent().add(richTextEditor);
        getContent().add(buttonPrimary);
        getContent().add(textLarge);
        getContent().add(buttonSecondary);
    }
}
