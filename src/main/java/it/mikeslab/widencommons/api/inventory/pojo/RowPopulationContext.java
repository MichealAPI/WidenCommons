package it.mikeslab.widencommons.api.inventory.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class RowPopulationContext {

    private final Map<Character, GuiElement> elements;
    private final Map<Character, ItemStack> cashedItems;
    private final String rowLayout;
    private final int row;
    private final int perRowLength;
    private final Inventory inventory;

}
