package rip.helium.ui.main.tab;

import rip.helium.Helium;
import rip.helium.configuration.Configuration;
import rip.helium.notification.mgmt.NotificationManager;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.base.BaseTextEntry;
import rip.helium.ui.main.components.simple.SimpleTextButton;
import rip.helium.ui.main.components.tab.configuration.ContainerConfigurations;
import rip.helium.ui.main.components.tab.configuration.TextEntryConfigurations;
import rip.helium.utils.font.Fonts;

import java.awt.*;

/**
 * @author antja03
 */
public class TabConfiguration extends Tab {

    private final ContainerConfigurations configContainer;
    private final BaseTextEntry configNameEntry;

    private Configuration selectedConfiguration;

    public TabConfiguration(Interface theInterface) {
        super(theInterface);
        this.components.add(configContainer = new ContainerConfigurations(theInterface, 25, 0, 175, 200));

        this.components.add(configNameEntry = new TextEntryConfigurations(theInterface, Fonts.f18, 207, 5, 110, 10));

        this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Create", 12, new Color(200, 200, 200), 220, 20, 85, 10, button -> {
            if (!configNameEntry.getContent().isEmpty()) {
                if (Helium.instance.configurationManager.searchByIdentifier(configNameEntry.getContent()) == null) {
                    Configuration configuration = new Configuration(configNameEntry.getContent());
                    configuration.save();
                    Helium.instance.configurationManager.add(configuration);
                    NotificationManager.postInfo("Configurations", "Created a new configuration (" + configNameEntry.getContent() + ")");
                    configContainer.refresh();
                } else {
                    NotificationManager.postInfo("Configurations", "Attempted to create a config with an existing ID");
                }
            } else {
                NotificationManager.postInfo("Configurations", "Attempted to create a config with no ID");
            }
        }));

        this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Overwrite", 12, new Color(200, 200, 200), 220, 80, 85, 10, button -> {
            if (configContainer.getSelectedConfig() != null) {
                configContainer.getSelectedConfig().save();
                NotificationManager.postInfo("Configurations", configContainer.getSelectedConfig().getIdentifier() + ".conf was overwritten");
            }
        }));

        this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Load", 12, new Color(200, 200, 200), 220, 35, 85, 10, button -> {
            if (configContainer.getSelectedConfig() != null) {
                configContainer.getSelectedConfig().load();
                NotificationManager.postInfo("Configurations", configContainer.getSelectedConfig().getIdentifier() + ".conf was loaded");
            }
        }));

        /*/this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Make default", 12, new Color(200, 200, 200), 220, 80, 85, 10, button -> {
            if (configContainer.getSelectedConfig() != null) {
                Helium.instance.configurationManager.getConfigurationList().forEach(config -> config.setDefault(false));
                configContainer.getSelectedConfig().setDefault(true);
                Helium.instance.configurationManager.saveConfigurationFiles();
                Helium.instance.notificationManager.postInfo("Configurations", configContainer.getSelectedConfig().getIdentifier() + ".conf was made your default");
            }
        }));/*/

        this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Delete", 12, new Color(200, 200, 200), 220, 95, 85, 10, button -> {
            if (configContainer.getSelectedConfig() != null) {
                Helium.instance.configurationManager.delete(configContainer.getSelectedConfig());
                NotificationManager.postInfo("Configurations", configContainer.getSelectedConfig().getIdentifier() + ".conf was deleted");
                configContainer.refresh();
            }
        }));

        /*/this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Export to clipboard", 12, new Color(200, 200, 200), 220, 125, 85, 10, button -> {
            JsonObject configurationObject = new JsonObject();
            configurationObject.addProperty("default", false);
            for (Cheat cheat : Helium.instance.cheatManager.getCheatRegistry().values()) {
                JsonObject cheatObject = new JsonObject();
                JsonObject valueObject = new JsonObject();
                cheatObject.addProperty("state", cheat.getState());
                cheatObject.addProperty("bind", cheat.getBind());
                for (Property property : cheat.getPropertyRegistry().values()) {
                    valueObject.addProperty(property.getId(), property.getValueAsString());
                }
                cheatObject.add("values", valueObject);
                configurationObject.add(cheat.getId(), cheatObject);
            }

            String jsonAsText = new GsonBuilder().create().toJson(configurationObject);
            USystem.writeToClipboard(jsonAsText);
        }));/*/

       /*/ this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Import from clipboard", 12, new Color(200, 200, 200), 220, 140, 85, 10, button -> {
            try {
                JsonObject configurationObject = new GsonBuilder().create().fromJson(USystem.readClipboard(), JsonObject.class);

                if (configurationObject == null)
                    return;

                for(Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
                    if (entry.getValue() instanceof JsonObject) {
                        JsonObject cheatObejct = (JsonObject) entry.getValue();
                        Cheat cheat = Helium.instance.cheatManager.getCheatRegistry().getOrDefault(entry.getKey(), null);

                        if (cheat == null)
                            continue;

                        if (cheatObejct.has("state")) {
                            cheat.setState(cheatObejct.get("state").getAsBoolean(), false);
                        }

                        if (cheatObejct.has("bind")) {
                            cheat.setBind(cheatObejct.get("bind").getAsInt());
                        }

                        if (cheatObejct.has("values")) cheatObejct.getAsJsonObject("values").entrySet().forEach(valueEntry -> {
                            Property property = cheat.getPropertyRegistry().getOrDefault(valueEntry.getKey(), null);
                            if (property != null)
                                property.setValue(valueEntry.getValue().getAsString());
                        });
                    }
                }
            } catch (com.google.gson.JsonSyntaxException ex) {

            }
        }));/*/

        /*/this.components.add(new SimpleTextButton(theInterface, new Color(40, 40, 40), new Color(20, 20, 20), "Refresh", 12, new Color(200, 200, 200), 220, 170, 85, 10, button -> {
            Helium.instance.configurationManager.loadConfigurationFiles(true);
            configContainer.refresh();
        }));/*/
    }

    @Override
    public void onTick() {
        getInterface().setWidth(300);
        super.onTick();
    }

}
