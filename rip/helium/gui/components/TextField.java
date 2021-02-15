package rip.helium.gui.components;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import rip.helium.utils.client.SystemUtils;
import rip.helium.utils.client.Timer;
import rip.helium.utils.render.Render2DUtils;

public class TextField extends Gui {
    private final FontRenderer fontRenderer;

    private final int posX;
    private final int posY;
    private final int width;
    private final int height;

    private String defaultContent;
    private String typedContent;

    private final int defaultTextColor;
    private final int typedTextColor;

    private final int backgroundColor;
    private final int borderColor;
    private final int focusedBackgroundColor;
    private final int focusedBorderColor;
    private final double borderWidth;

    private final int charLimit;
    private FieldType type;

    private boolean focused;

    private char character;
    private int key;
    private int ticksKeyDown;
    private final Timer keyStopwatch;

    private String replaceAll;

    private int startIndex;

    private boolean hidden;

    public TextField(FontRenderer fontRenderer, int posX, int posY, int width, int height) {
        this.fontRenderer = fontRenderer;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.defaultContent = "";
        this.typedContent = "";

        this.defaultTextColor = new Color(180, 180, 180).getRGB();
        this.typedTextColor = new Color(230, 230, 230).getRGB();

        this.backgroundColor = new Color(15, 15, 15, 255).getRGB();
        this.borderColor = new Color(10, 10, 10, 255).getRGB();
        this.focusedBackgroundColor = new Color(10, 10, 10, 255).getRGB();
        this.focusedBorderColor = 0xff32CD32;
        this.borderWidth = 1;

        this.charLimit = -1;
        this.type = FieldType.ANY;

        this.focused = false;

        key = 0;
        ticksKeyDown = 0;
        keyStopwatch = new Timer();

        replaceAll = "";

        startIndex = 0;
        hidden = false;
    }

    public void initTextField() {
        this.focused = false;
        ticksKeyDown = 0;
    }

    public void updateTextField() {
        if (key != 0) {
            if (Keyboard.isKeyDown(key))
                ticksKeyDown++;
            else {
                key = 0;
                ticksKeyDown = 0;
                return;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!focused)
            return;

        if (keyCode == Keyboard.KEY_ESCAPE) {
            focused = false;
            return;
        }

        if (keyCode != key) {
            character = typedChar;
            key = keyCode;
            ticksKeyDown = 0;
            keyStopwatch.updateLastTime();
        }

        if (keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE) {
            backspace();
            return;
        }

        if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))) {
            String clipboardContent = SystemUtils.readClipboard();

            typedContent += clipboardContent;
            return;
        }

        String charAsString = Character.toString(typedChar);

        if (typedChar != ' ') {
            switch (type) {
                case LETTERS: {
                    if (!Character.isLetter(typedChar))
                        return;
                    break;
                }
                case NUMBERS: {
                    if (!Character.isDigit(typedChar))
                        return;
                    break;
                }
                case NUMBER_LETTERS: {
                    if (!Character.isLetter(typedChar)
                            && !Character.isDigit(typedChar))
                        return;
                    break;
                }
                case NUMBERS_LETTERS_SPECIAL: {
                    boolean specialChar = false;
                    String specialChars = "-/*!@#$%^&*()\"{}_[]|\\?/<>,.";
                    for (int i = 0; i < specialChars.length(); i++) {
                        char special = specialChars.charAt(i);
                        if (typedChar == special)
                            specialChar = true;
                    }

                    if (!Character.isLetter(typedChar)
                            && !Character.isDigit(typedChar)
                            && !specialChar)
                        return;
                    break;
                }
                case ANY:
                    break;
                default:
                    break;
            }
        }

        if (charLimit != -1 && typedContent.length() >= charLimit)
            return;

        typedContent += charAsString;

        if (fontRenderer.getStringWidth(typedContent) >= width - 4)
            startIndex++;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        focused = mouseX >= posX && mouseY >= posY && mouseX <= posX + width && mouseY <= posY + height;
    }

    public void drawTextField() {
        if (hidden)
            return;

        Render2DUtils.drawRect(posX, posY,
                posX + width, posY + height,
                focused ? focusedBackgroundColor : backgroundColor);

        String content = getReplacedContent().substring(startIndex);

        fontRenderer.drawString(typedContent.isEmpty() ? defaultContent : (replaceAll != "" ? content.replaceAll(".", replaceAll) : content),
                posX + 1, posY + height / 2 - fontRenderer.FONT_HEIGHT / 2,
                typedContent.isEmpty() ? defaultTextColor : typedTextColor);

        if (focused) {
            Render2DUtils.drawRect(
                    typedContent.isEmpty() ? posX + 2 : posX + 2 + fontRenderer.getStringWidth(content),
                    posY + height / 2 + fontRenderer.FONT_HEIGHT / 2,
                    typedContent.isEmpty() ? posX + 5 : posX + 5 + fontRenderer.getStringWidth(content),
                    posY + height / 2 + fontRenderer.FONT_HEIGHT / 2 + 1,
                    0xffffffff);
        }

        //in draw so it can go faster than 20/s lol
        if (ticksKeyDown == 10) {
            keyStopwatch.updateLastTime();
        } else if (ticksKeyDown > 5 && ticksKeyDown < 25) {
            if (keyStopwatch.hasPassed(100)) {
                try {
                    keyTyped(character, key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                keyStopwatch.updateLastTime();
            }
        } else if (ticksKeyDown > 25) {
            if (keyStopwatch.hasPassed(25)) {
                try {
                    keyTyped(character, key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                keyStopwatch.updateLastTime();
            }
        }
    }

    public void backspace() {
        if (!typedContent.isEmpty()) {
            typedContent = typedContent.substring(0, typedContent.length() - 1);
            if (startIndex > 0)
                startIndex--;
        }
    }

    public String getReplacedContent() {
        return replaceAll != "" ? typedContent.replaceAll(".", replaceAll) : typedContent;
    }

    public String getTypedContentFormatted() {
        return trimStringToWidth(getReplacedContent(), width - 2);
    }

    public String trimStringToWidth(String string, int width) {
        if (fontRenderer.getStringWidth(string) > width) {
            return trimStringToWidth(string.substring(0, string.length() - 1), width);
        }

        return string;
    }

    public String getTypedContent() {
        return typedContent;
    }

    public void setTypedContent(String typedContent) {

        this.typedContent = typedContent;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getReplaceAll() {
        return replaceAll;
    }

    public void setReplaceAll(String replaceAll) {
        this.replaceAll = replaceAll;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
