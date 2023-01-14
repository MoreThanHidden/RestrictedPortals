package morethanhidden.restrictedportals.platform;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getConfigDimensionsResourceNames() {
        return ConfigHandler.dimensionsResourceNames;
    }

    @Override
    public String getConfigDimensionsNames() {
        return ConfigHandler.dimensionsNames;
    }

    @Override
    public String getConfigCraftItems() {
        return ConfigHandler.craftItems;
    }

    @Override
    public String getConfigBlockedMessage() {
        return ConfigHandler.blockedMessage;
    }

    @Override
    public String getConfigCraftedMessage() {
        return ConfigHandler.craftedMessage;
    }

    @Override
    public String getConfigDescription() {
        return ConfigHandler.description;
    }

    @Override
    public boolean getConfigPreventEPDeath() {
        return ConfigHandler.preventEPDeath;
    }
}
