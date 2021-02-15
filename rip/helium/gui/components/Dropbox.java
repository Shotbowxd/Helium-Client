package rip.helium.gui.components;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

/**
 * @author antja03
 */
public class Dropbox extends Gui {

    private boolean droppedDown;

    private final int posX;
    private final int posY;
    private final int width;
    private final int height;

    private final FontRenderer fontRenderer;

    private final int optionHeight;

    private final int backroundColor;
    private final int optionBackgroundColor;
    private final int selectedBackgroundColor;
    private final int selectedColor;
    private final int optionColor;	

    private final String[] options;
    private int currentOption;

    public Dropbox(int posX, int posY, int width, int height, FontRenderer fontRenderer, int defaultOption, String... options) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.fontRenderer = fontRenderer;

        this.currentOption = defaultOption;
        this.options = options;

        this.optionHeight = 15;
        this.backroundColor = new Color(10, 10, 255, 255).getRGB();
        this.optionBackgroundColor = new Color(18, 18, 18).getRGB();
        this.selectedBackgroundColor = new Color(18, 18, 18).getRGB();
        this.selectedColor = new Color(220, 220, 220).getRGB();
        this.optionColor = new Color(150, 150, 150).getRGB();
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX >= posX && mouseY >= posY && mouseX <= posX + width && mouseY <= posY + height)
            droppedDown = !droppedDown;
        else
            droppedDown = false;

        ArrayList<String> uninclusiveOptions = getOptionsWithoutSelected();
        for (String string : uninclusiveOptions) {
            if (mouseX >= posX
                    && mouseY >= posY + height + uninclusiveOptions.indexOf(string) * optionHeight
                    && mouseX <= posX + width
                    && mouseY <= posY + height + uninclusiveOptions.indexOf(string) * optionHeight + optionHeight) {

                for (int i = 0; i < options.length; i++) {
                    String option = options[i];
                    if (option.equals(string))
                        currentOption = i;
                }

            }
        }
    }

    public void drawDropbox(int mouseX, int mouseY) {
        if (!droppedDown) {
            drawRect(posX, posY, posX + width, posY + height, backroundColor);
        } else {
            ArrayList<String> uninclusiveOptions = getOptionsWithoutSelected();
            drawRect(posX, posY, posX + width, posY + height, backroundColor);
            drawRect(posX, posY + height, posX + width, posY + height + uninclusiveOptions.size() * optionHeight, optionBackgroundColor);
            for (String string : uninclusiveOptions) {
                this.drawCenteredString(fontRenderer, string, posX + width / 2, posY + height + 1 + uninclusiveOptions.indexOf(string) * optionHeight + fontRenderer.FONT_HEIGHT / 2, optionColor);
            }
        }

        this.drawCenteredString(fontRenderer, options[currentOption], posX + width / 2, posY + height / 2 - fontRenderer.FONT_HEIGHT / 2, selectedColor);
    }

    public ArrayList<String> getOptionsWithoutSelected() {
        ArrayList<String> optionsNA = new ArrayList<>();
        for (String string : options) {
            if (string != options[currentOption]) {
                optionsNA.add(string);
            }
        }
        return optionsNA;
    }

    public String getSelected() {
        return options[currentOption];
    }

    public void setSelected(String string) {
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (option.equals(string))
                currentOption = i;
        }
    }

}
