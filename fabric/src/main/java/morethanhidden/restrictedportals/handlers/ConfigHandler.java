package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class ConfigHandler {

    public static String dimensionsResourceNames = "minecraft:the_nether,minecraft:the_end";
    public static String dimensionsNames  = "Nether,End";
    public static String craftItems = "minecraft:flint_and_steel,minecraft:ender_eye";
    public static String blockedMessage = "Please obtain a %item% to enter the %dim%";
    public static String craftedMessage = "%dim% Unlocked!";
    public static String description = "Obtain a %item%";
    public static boolean preventEPDeath = true;

    public static void reloadConfig(){

        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get("config", Constants.MOD_ID + ".cfg"), StandardOpenOption.READ, StandardOpenOption.CREATE));
        } catch (IOException e) {
            Constants.LOGGER.warn("Missing Config File 'RestrictedPortals.cfg' will attempt to create it.");
        }

        properties.putIfAbsent("dimensionsResourceNames","minecraft:the_nether,minecraft:the_end");
        dimensionsResourceNames = properties.getProperty("dimensionsResourceNames");

        properties.putIfAbsent("dimensionsNames","Nether,End");
        dimensionsNames = properties.getProperty("dimensionsNames");

        properties.putIfAbsent("craftItems","minecraft:flint_and_steel,minecraft:ender_eye");
        craftItems = properties.getProperty("craftItems");

        properties.putIfAbsent("blockedMessage","Please obtain a %item% to enter the %dim%");
        blockedMessage = properties.getProperty("blockedMessage");

        properties.putIfAbsent("craftedMessage","%dim% Unlocked!");
        craftedMessage = properties.getProperty("craftedMessage");

        properties.putIfAbsent("description","Obtain a %item%");
        description = properties.getProperty("description");

        properties.putIfAbsent("preventEPDeath","true");
        preventEPDeath = Boolean.parseBoolean(properties.getProperty("preventEPDeath"));

        try {
            properties.store(Files.newOutputStream(Paths.get("config", Constants.MOD_ID + ".cfg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE), null);
        } catch (IOException e) {
            Constants.LOGGER.error("Failed to Create/Update Config: RestrictedPortals.cfg");
        }
    }
}
