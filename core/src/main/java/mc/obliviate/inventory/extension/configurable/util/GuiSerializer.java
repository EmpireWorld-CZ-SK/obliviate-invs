package mc.obliviate.inventory.extension.configurable.util;

import com.google.common.base.Preconditions;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import mc.obliviate.inventory.extension.configurable.ConfigurableGui;
import mc.obliviate.inventory.extension.configurable.DysfunctionalConfigIcon;
import mc.obliviate.inventory.extension.configurable.GuiConfigurationTable;
import mc.obliviate.util.placeholder.PlaceholderUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiSerializer {

    @SuppressWarnings("ConstantConditions")
    public static void putDysfunctionalIcons(@Nonnull ConfigurableGui gui, @Nonnull GuiConfigurationTable table, @Nonnull Section iconsSection, @Nullable PlaceholderUtil placeholderUtil, @Nonnull List<String> functionalSlots) {
        Preconditions.checkNotNull(gui, "dysfunctional icons could not put because gui was null!");
        Preconditions.checkNotNull(iconsSection, "null configuration section given!");

        for (final String sectionName : iconsSection.getRoutesAsStrings(false)) {
            final Section section = iconsSection.getSection(sectionName);

            if (functionalSlots.contains(sectionName)) continue;
            if (!section.contains(table.getSlotSectionName())) continue;
            if (!section.contains(table.getMaterialSectionName())) continue;

            final int slotNo = section.getInt(table.getSlotSectionName(), -1);
            if (slotNo >= 0) {
                gui.addItem(slotNo, new DysfunctionalConfigIcon(gui.getGuiCache().getConfigItem(section, placeholderUtil, table), section));
                continue;
            }

            final List<Integer> slots = parseSlotString(section.getString(table.getSlotSectionName()));
            if (!slots.isEmpty()) {
                slots.forEach(slot -> {
                    Section iconSection = iconsSection.getSection(sectionName);
                    gui.addItem(slot, new DysfunctionalConfigIcon(gui.getGuiCache().getConfigItem(section, placeholderUtil, table), iconSection));
                });
            }
        }
    }

    public static List<Integer> parseSlotString(String str) {
        if (str == null) return new ArrayList<>();
        str = str.replaceAll(" ", "");
        if (str.contains(",")) {
            return parseStringAsIntegerList(str);
        } else if (str.contains("-")) {
            return parseStringAsIntegerRange(str);
        }
        return new ArrayList<>();
    }

    private static List<Integer> parseStringAsIntegerRange(String str) {
        final String[] slots = str.split("-");
        if (slots.length != 2) return new ArrayList<>();
        int from, to;

        try {
            from = Integer.parseInt(slots[0]);
            to = Integer.parseInt(slots[1]);
        } catch (NumberFormatException ignore) {
            return new ArrayList<>();
        }

        if (from > to) return new ArrayList<>();

        final List<Integer> result = new ArrayList<>();
        while (from <= to) result.add(from++);
        return result;
    }

    private static List<Integer> parseStringAsIntegerList(String str) {
        final List<Integer> pageSlots = new ArrayList<>();
        final String[] slotStrings = str.split(",");

        for (final String slotText : slotStrings) {
            try {
                if (slotText.contains("-")) {
                    pageSlots.addAll(parseStringAsIntegerRange(slotText));
                } else {
                    pageSlots.add(Integer.parseInt(slotText));
                }
            } catch (NumberFormatException ignore) {
            }
        }
        return pageSlots;
    }

}
