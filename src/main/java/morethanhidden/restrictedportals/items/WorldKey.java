package morethanhidden.restrictedportals.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class WorldKey extends Item {
    public WorldKey(String unlocalizedName) {
        super();
        this.setHasSubtypes(true);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setTextureName("restrictedportals:" + unlocalizedName);
    }
}
