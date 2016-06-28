package morethanhidden.restrictedportals.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.util.text.translation.I18n;

public class StringFormatter {

    public static IStatStringFormat format(final ItemStack item) {
        return new IStatStringFormat() {
            @Override
            public String formatString(String s) {
                return I18n.translateToLocal("achievement.rpunlock.desc") + " " + item.getDisplayName();

            }
        };
    }
}
