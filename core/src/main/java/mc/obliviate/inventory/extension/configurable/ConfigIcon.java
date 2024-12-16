package mc.obliviate.inventory.extension.configurable;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfigIcon extends Icon {

    private final Section section;

    public ConfigIcon(ItemStack item, Section section) {
        super(item);
        this.section = section;
    }

    public ConfigIcon(Material material, Section section) {
        super(material);
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}
