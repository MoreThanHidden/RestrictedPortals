package morethanhidden.restrictedportals.platform.services;

public interface IPlatformHelper {

    /**
     * Dimensions ResourceNames from the Config File
     * @return The Comma Separated List of Dimensions ResourceNames
     */
    String getConfigDimensionsResourceNames();

    /**
     * Dimensions Display Names from the Config File
     * @return The Comma Separated List of Dimensions Names
     */
    String getConfigDimensionsNames();

    /**
     * Craft Items from the Config File
     * @return The Comma Separated List of Dimensions Craft Items
     */
    String getConfigCraftItems();

    /**
     * Gets the Blocked message with %dim% and %item% substitutes from the Config File
     * @return Blocked message String
     */
    String getConfigBlockedMessage();


    /**
     * Gets the Achievement message with %dim% substituted from the Config File
     * @return Achievement message String
     */
    String getConfigCraftedMessage();

    /**
     * Gets the Achievement Description with %dim% and %item% substitutes from the Config File
     * @return Achievement Description String
     */
    String getConfigDescription();

    /**
     * Gets weather or not, the player should be teleported to their home / spawn due to Lava from the Config File
     * @return True if the Player should be teleported
     */
    boolean getConfigPreventEPDeath();

}