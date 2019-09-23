package me.apposed.quantum.staff.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    ItemStack is;

    public ItemBuilder(Material material) {
        is = new ItemStack(material);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder setType(Material material) {
        is.setType(material);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        is.setDurability(durability);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name.replace("&", "§"));
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = is.getItemMeta();
        meta.setLore(lore);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String line) {
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(line);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean unsafe) {
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(enchantment, level, unsafe);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantments(HashMap<Enchantment, Integer> enchantments, boolean unsafe) {
        ItemMeta meta = is.getItemMeta();
        for (Enchantment enchantment : enchantments.keySet())
            meta.addEnchant(enchantment, enchantments.get(enchantment), unsafe);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        ItemMeta meta = is.getItemMeta();
        meta.removeEnchant(enchantment);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeEnchants(Enchantment... enchantments) {
        ItemMeta meta = is.getItemMeta();
        for (Enchantment enchantment : enchantments)
            meta.removeEnchant(enchantment);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag flags) {
        ItemMeta meta = is.getItemMeta();
        meta.addItemFlags(flags);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag flags) {
        ItemMeta meta = is.getItemMeta();
        meta.removeItemFlags(flags);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = is.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        is.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return is.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof ItemBuilder)
                return super.equals(obj);
            if (obj instanceof ItemStack) {
                ItemStack item = ((ItemStack) obj).clone();
                item.setAmount(is.getAmount());
                return item.equals(is);
            }
        }
        return false;
    }
}
