package morethanhidden.restrictedportals.platform;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.platform.services.IPlatformHelper;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getConfigDimensionsResourceNames() {
        return ConfigHandler.GENERAL.dimResName.get();
    }

    @Override
    public String getConfigDimensionsNames() {
        return ConfigHandler.GENERAL.dimNames.get();
    }

    @Override
    public String getConfigCraftItems() {
        return ConfigHandler.GENERAL.craftItems.get();
    }

    @Override
    public String getConfigBlockedMessage() {
        return ConfigHandler.GENERAL.blockedmessage.get();
    }

    @Override
    public String getConfigCraftedMessage() {
        return ConfigHandler.GENERAL.craftedmessage.get();
    }

    @Override
    public String getConfigDescription() {
        return ConfigHandler.GENERAL.description.get();
    }

    @Override
    public boolean getConfigPreventEPDeath() {
        return ConfigHandler.GENERAL.preventEPDeath.get();
    }
}