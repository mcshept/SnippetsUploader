package dev.shept.snippets.views.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Base64;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY_INLINE;

@PageTitle("View")
@JsModule("./clipboard/clipboard.js")
@Route(value = "dashboard/view")
@Uses(Icon.class)
public class ViewView extends Composite<VerticalLayout> implements HasUrlParameter<String> {

    private TextArea textArea;

    public ViewView() {
        textArea = new TextArea();
        Button copyBtn = new Button("Copy", clickEvent -> copyTextToClipboard(textArea.getValue()));
        Button homeBtn = new Button("Home", clickEvent -> UI.getCurrent().navigate(""));
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        textArea.setReadOnly(true);
        textArea.setWidth("100%");
        textArea.getStyle().set("flex-grow", "1");
        textArea.getStyle().set("font-size", "var(--lumo-font-size-xl)");

        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(textArea);
        getContent().add(copyBtn);
        getContent().add(homeBtn);
    }

    @SneakyThrows
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        String serializedSnippet = new String(getSerializedSnippet(parameter));
        String unescapedYamlString = StringEscapeUtils.unescapeHtml4(serializedSnippet);
        String deserializedSnippet = deserializeFromYaml(unescapedYamlString)
                .replace("<pre>", "")
                .replace("</pre><p><br></p>", "")
                .replace("</pre>", "");
        textArea.setValue(deserializedSnippet);
    }

    private static String deserializeFromYaml(String yamlString) {
        try {
            Yaml yaml = new Yaml();
            return yaml.load(yamlString);
        } catch (Exception e) {
            throw new RuntimeException("Error during deserialization", e);
        }
    }

    @SneakyThrows
    private byte[] getSerializedSnippet(String uuid) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:8080/api/v1/snippet/get/" + uuid);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity);

        JSONObject jsonObject = new JSONObject(responseBody);
        String content = jsonObject.getString("content");

        return Base64.getDecoder().decode(content);
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

    public static Notification createReportError(String text) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        Icon icon = VaadinIcon.WARNING.create();
        Button retryBtn = new Button("Retry",
                clickEvent -> notification.close());
        retryBtn.getStyle().setMargin("0 0 0 var(--lumo-space-l)");

        var layout = new HorizontalLayout(icon, new Text(text), retryBtn,
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
