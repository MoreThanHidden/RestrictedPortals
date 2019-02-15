package morethanhidden.restrictedportals.handlers;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);

    public static class General {
        public final ForgeConfigSpec.BooleanValue consumeItems;
        public final ForgeConfigSpec.BooleanValue preventEPDeath;
        public final ForgeConfigSpec.ConfigValue<String> blockedmessage;
        public final ForgeConfigSpec.ConfigValue<String> craftedmessage;
        public final ForgeConfigSpec.ConfigValue<String> craftItems;
        public final ForgeConfigSpec.ConfigValue<String> dimIDs;
        public final ForgeConfigSpec.ConfigValue<String> dimNames;
        public final ForgeConfigSpec.ConfigValue<String> pblockwhitelist;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("options");
            consumeItems = builder
                    .comment("Consume items by right clicking on portal rather then crafting")
                    .translation("restrictedportals.configgui.consume")
                    .define("consumeItems", true);
            preventEPDeath = builder
                    .comment("Prevents Death by Teleporting Player to Spawn if trying to enter an Ender Portal")
                    .translation("restrictedportals.configgui.preventdeath")
                    .define("preventdeath", false);
            blockedmessage = builder
                    .comment("Message that displays when Teleport is blocked")
                    .translation("restrictedportals.configgui.blockedmessage")
                    .define("blockedmessage", "Please craft a %item% to enter the %dim%");
            craftedmessage = builder
                    .comment("Message that displays when Portal Item is Crafted")
                    .translation("restrictedportals.configgui.craftedmessage")
                    .define("craftedmessage", "%dim% Unlocked!");
            craftItems = builder
                    .comment("Comma seperated list of items that when crafted unlock the corresponding dimension")
                    .translation("restrictedportals.configgui.craftitems")
                    .define("craftitems", "minecraft:flint_and_steel,minecraft:ender_eye");
            dimIDs = builder
                    .comment("Comma seperated list of Dimension IDs to be blocked by items")
                    .translation("restrictedportals.configgui.dimids")
                    .define("dimids", "-1,1");
            dimNames = builder
                    .comment("Comma seperated list of Dimension Names to match IDs")
                    .translation("restrictedportals.configgui.dimnames")
                    .define("dimnames", "Nether,End");
            pblockwhitelist = builder
                    .comment("Blocks that the portal item will be consumed when right clicked on")
                    .translation("restrictedportals.configgui.pblockwhitelist")
                    .define("pblockwhitelist", "minecraft:portal,minecraft:end_portal");
            builder.pop();
        }
    }

    public static final ForgeConfigSpec spec = BUILDER.build();

}
