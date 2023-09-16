/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

public class Fonts extends MinecraftInstance {

    @FontDetails(fontName = "Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    private static final HashMap<FontInfo, IFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap<>();

    @FontDetails(fontName = "Bold", fontSize = 30)
        public static IFontRenderer bold30;
    @FontDetails(fontName = "Bold", fontSize = 40)
    public static IFontRenderer bold40;
    @FontDetails(fontName = "Bold", fontSize = 35)
    public static IFontRenderer bold35;
    @FontDetails(fontName = "Bold", fontSize = 45)
    public static IFontRenderer bold45;
    @FontDetails(fontName = "Bold", fontSize = 72)
    public static IFontRenderer bold72;

    @FontDetails(fontName = "Bold", fontSize = 95)
    public static IFontRenderer bold95;
    @FontDetails(fontName = "Bold", fontSize = 180)
    public static IFontRenderer bold180;

    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static IFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 30)
    public static IFontRenderer font30;
    @FontDetails(fontName = "Roboto Medium", fontSize = 25)
    public static IFontRenderer font25;

    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static IFontRenderer font40;
    @FontDetails(fontName = "Roboto Medium", fontSize = 80)
    public static IFontRenderer font80;


    @FontDetails(fontName = "Roboto Bold", fontSize =72)
    public static IFontRenderer fontBold72;
    @FontDetails(fontName = "Roboto Bold", fontSize = 120)
    public static IFontRenderer fontBold120;
    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static IFontRenderer fontBold180;


    @FontDetails(fontName = "SFUI Regular", fontSize = 18)
    public static IFontRenderer fontSFUI18;
    @FontDetails(fontName = "SFUI Regular", fontSize = 35)
    public static IFontRenderer fontSFUI35;
    @FontDetails(fontName = "SFUI Regular", fontSize = 40)
    public static IFontRenderer fontSFUI40;
    @FontDetails(fontName = "SFUI Regular", fontSize = 56)
    public static IFontRenderer fontSFUI56;
    @FontDetails(fontName = "SFUI Regular", fontSize = 120)
    public static IFontRenderer fontSFUI120;

    @FontDetails(fontName = "ComfortaaRegular35", fontSize = 35)
    public static IFontRenderer ComfortaaRegular35;
    @FontDetails(fontName = "ComfortaaRegular45", fontSize = 45)
    public static IFontRenderer ComfortaaRegular45;
    @FontDetails(fontName = "ComfortaaRegular60", fontSize = 60)
    public static IFontRenderer ComfortaaRegular60;

    @FontDetails(fontName = "Posterama", fontSize = 15)
    public static IFontRenderer posterama30;
    @FontDetails(fontName = "Posterama", fontSize = 18)
    public static IFontRenderer posterama35;
    @FontDetails(fontName = "Posterama", fontSize = 20)
    public static IFontRenderer posterama40;
    @FontDetails(fontName = "Posterama", fontSize = 25)
    public static IFontRenderer posterama50;

    @FontDetails(fontName = "yangzi", fontSize = 20)
    public static IFontRenderer icon50;
    @FontDetails(fontName = "yangzi", fontSize = 25)
    public static IFontRenderer icon80;

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

        icon80 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("hicon.ttf", 25)));
        icon50 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("hicon.ttf", 20)));

        posterama30 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("posterama.ttf", 15)));
        posterama35 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("posterama.ttf", 18)));
        posterama40 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("posterama.ttf", 20)));
        posterama50 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("posterama.ttf", 20)));

        font35 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(35)));
        font25 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(25)));
        font40 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(40)));
        font30 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(30)));
        font80 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(80)));

        fontBold72 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("Roboto-Bold.ttf", 72)));
        fontBold120 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("Roboto-Bold.ttf", 120)));
        fontBold180 = classProvider.wrapFontRenderer(new GameFontRenderer(getFont("Roboto-Bold.ttf", 180)));

        fontSFUI18 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(18)));
        fontSFUI35 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(35)));
        fontSFUI40 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(40)));
        fontSFUI56 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(56)));
        fontSFUI120 = classProvider.wrapFontRenderer(new GameFontRenderer(getSFUI(120)));

        bold35 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(35)));
        bold40 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(40)));
        bold45 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(45)));
        bold30 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(30)));
        bold72 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(72)));
        bold95 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(95)));
        bold180 = classProvider.wrapFontRenderer(new GameFontRenderer(getBold(180)));

        ComfortaaRegular35 = classProvider.wrapFontRenderer(new GameFontRenderer(getComfortaaRegular(35)));
        ComfortaaRegular45 = classProvider.wrapFontRenderer(new GameFontRenderer(getComfortaaRegular(45)));
        ComfortaaRegular60 = classProvider.wrapFontRenderer(new GameFontRenderer(getComfortaaRegular(60)));

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), classProvider.wrapFontRenderer(new GameFontRenderer(font)));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("PridePlus Font >> Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }
    public static IFontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                Object o = field.get(null);

                if (o instanceof IFontRenderer) {
                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (IFontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), minecraftFont);
    }

    public static FontInfo getFontDetails(final IFontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<FontInfo, IFontRenderer> entry : CUSTOM_FONT_RENDERERS.entrySet()) {
            if (entry.getValue() == fontRenderer)
                return entry.getKey();
        }

        return null;
    }
    private static Font getBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (FontFormatException | IOException e) {
            ClientUtils.getLogger().error("PridePlus Font >> Error loading Bold.");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getSFUI(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/sfuidisplayregular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (FontFormatException | IOException e) {
            ClientUtils.getLogger().error("PridePlus Font >> Error loading SFUI.");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getComfortaaRegular(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/ComfortaaRegular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ClientUtils.getLogger().error("PridePlus Font >> Error loading ComfortaaRegular.");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static List<IFontRenderer> getFonts() {
        final List<IFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                ClientUtils.getLogger().error("PridePlus Font >> Error loading fonts.");
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/"+fontName)).getInputStream();
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final FontFormatException | IOException e) {
            ClientUtils.getLogger().error("PridePlus Font >> Error loading " + fontName + ".");
            return new Font("default", Font.PLAIN, size);
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}