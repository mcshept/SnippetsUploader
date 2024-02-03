package dev.shept.snippets.views.addsnippet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Base64;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY_INLINE;

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
        buttonPrimary.addClickListener(event -> sendRequest(richTextEditor, textLarge, buttonSecondary));
        buttonSecondary.addClickListener(event -> copyTextToClipboard(textLarge.getText()));
        getContent().add(richTextEditor);
        getContent().add(buttonPrimary);
        getContent().add(textLarge);
        getContent().add(buttonSecondary);
    }

    private void sendRequest(RichTextEditor richTextEditor, Paragraph textLarge, Button buttonSecondary) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/snippet/create");
            String plainText = richTextEditor.getValue();

            String yamlString = serializeToYaml(plainText);
            byte[] serializedBytes = yamlString.getBytes();
            String serializedString = Base64.getEncoder().encodeToString(serializedBytes);
            String jsonPayload = "{\"content\":\"" + serializedString + "\"}";
            HttpEntity stringEntity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            JSONObject jsonObject = new JSONObject(responseBody);
            String uuid = jsonObject.getString("uuid");
            textLarge.setText("http://localhost:8080/" + uuid);
            buttonSecondary.setDisableOnClick(response.getCode() != 200);
        } catch (Exception ignored) {
        }
    }

    private static String serializeToYaml(String content) {
        try {
            Yaml yaml = new Yaml();
            return yaml.dump(content);
        } catch (Exception e) {
            throw new RuntimeException("Error during serialization", e);
        }
    }

    private void copyTextToClipboard(String text) {
        UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", text);
        Notification copySuccess = createSubmitSuccess("Copied URL successfully!");
        copySuccess.open();
    }

    public static Notification createSubmitSuccess(String text) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();

        Button viewBtn = new Button("View", clickEvent -> notification.close());
        viewBtn.getStyle().setMargin("0 0 0 var(--lumo-space-l)");

        var layout = new HorizontalLayout(icon, new Text(text), viewBtn,
                createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);

        return notification;
    }

    public static Button createCloseBtn(Notification notification) {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> notification.close());
        closeBtn.addThemeVariants(LUMO_TERTIARY_INLINE);

        return closeBtn;
    }

}
