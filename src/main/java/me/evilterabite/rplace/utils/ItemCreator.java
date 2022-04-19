package me.evilterabite.rplace.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemCreator {

    public static ItemStack create(Material material, String name, List<String> lore) {
        ItemStack item =  new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(Material material, String name, String lore) {
        ItemStack item =  new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(new ArrayList<>(Collections.singletonList(lore)));

        item.setItemMeta(meta);
        return item;
    }
}
