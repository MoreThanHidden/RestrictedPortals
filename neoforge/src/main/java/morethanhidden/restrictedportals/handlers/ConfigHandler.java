package morethanhidden.restrictedportals.handlers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandler {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);

    public static class General {
        public final ModConfigSpec.BooleanValue preventEPDeath;
        public final ModConfigSpec.ConfigValue<String> blockedmessage;
        public final ModConfigSpec.ConfigValue<String> craftedmessage;
        public final ModConfigSpec.ConfigValue<String> description;
        public final ModConfigSpec.ConfigValue<String> craftItems;
        public final ModConfigSpec.ConfigValue<String> dimResName;
        public final ModConfigSpec.ConfigValue<String> dimNames;

        General(ModConfigSpec.Builder builder) {
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
            description = builder
                    .comment("Description for the Advancements")
                    .translation("restrictedportals.configgui.description")
                    .define("description", "Obtain a %item%");
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

    public static final ModConfigSpec spec = BUILDER.build();

}
