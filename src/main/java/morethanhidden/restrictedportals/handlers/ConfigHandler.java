package morethanhidden.restrictedportals.handlers;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);

    public static class General {
        public final ForgeConfigSpec.BooleanValue preventEPDeath;
        public final ForgeConfigSpec.ConfigValue<String> blockedmessage;
        public final ForgeConfigSpec.ConfigValue<String> craftedmessage;
        public final ForgeConfigSpec.ConfigValue<String> craftItems;
        public final ForgeConfigSpec.ConfigValue<String> dimResName;
        public final ForgeConfigSpec.ConfigValue<String> dimNames;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("options");
            preventEPDeath = builder
                    .comment("Prevents Death by Teleporting Player to Spawn if trying to enter an Ender Portal")
                    .translation("restrictedportals.configgui.preventdeath")
                    .define("preventdeath", true);
            blockedmessage = builder
                    .comment("Message that displays when Teleport is blocked")
                    .translation("restrictedportals.configgui.blockedmessage")
                    .define("blockedmessage", "Please obtain a %item% to enter the %dim%");
            craftedmessage = builder
                    .comment("Title for the Advancements")
                    .translation("restrictedportals.configgui.craftedmessage")
                    .define("craftedmessage", "%dim% Unlocked!");
            craftItems = builder
                    .comment("Comma seperated list of items that when crafted unlock the corresponding dimension")
                    .translation("restrictedportals.configgui.craftitems")
                    .define("craftitems", "minecraft:flint_and_steel,minecraft:ender_eye");
            dimResName = builder
                    .comment("Comma seperated list of Dimension Resource Names")
                    .translation("restrictedportals.configgui.dimresname")
                    .define("dimresname", "minecraft:the_nether,minecraft:the_end");
            dimNames = builder
                    .comment("Comma seperated list of Dimension Display Names")
                    .translation("restrictedportals.configgui.dimnames")
                    .define("dimnames", "Nether,End");
            builder.pop();
        }
    }

    public static final ForgeConfigSpec spec = BUILDER.build();

}
