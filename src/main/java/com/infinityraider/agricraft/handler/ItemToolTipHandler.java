package com.infinityraider.agricraft.handler;

import com.infinityraider.agricraft.AgriCraft;
import com.infinityraider.agricraft.api.v1.items.IAgriClipperItem;
import com.infinityraider.agricraft.api.v1.items.IAgriRakeItem;
import com.infinityraider.agricraft.api.v1.items.IAgriTrowelItem;

import java.text.MessageFormat;
import java.util.Objects;

import com.infinityraider.agricraft.reference.AgriToolTips;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ItemToolTipHandler {

    private static final ItemToolTipHandler INSTANCE = new ItemToolTipHandler();

    public static ItemToolTipHandler getInstance() {
        return INSTANCE;
    }

    private ItemToolTipHandler() {}

    private static void addFormatted(ItemTooltipEvent event, String format, Object... objects) {
        event.getToolTip().add(new StringTextComponent(MessageFormat.format(format, objects)).mergeStyle(TextFormatting.DARK_AQUA));
    }

    private static void addCategory(ItemTooltipEvent event, String category) {
        event.getToolTip().add(new StringTextComponent(category + ":").mergeStyle(TextFormatting.DARK_AQUA));
    }

    private static void addParameter(ItemTooltipEvent event, String key, Object value) {
        event.getToolTip().add(new StringTextComponent(" - " + key + ": " + Objects.toString(value)).mergeStyle(TextFormatting.DARK_AQUA));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addRegistryInfo(ItemTooltipEvent event) {
        if (AgriCraft.instance.getConfig().registryTooltips()) {
            final Item item = event.getItemStack().getItem();
            addCategory(event, "Registry");
            addParameter(event, "id", item.getRegistryName());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addNbtInfo(ItemTooltipEvent event) {
        if (AgriCraft.instance.getConfig().nbtTooltips()) {
            addCategory(event, "NBT");
            if (event.getItemStack().hasTag()) {
                final CompoundNBT tag = event.getItemStack().getTag();
                for (String key : tag.keySet()) {
                    addParameter(event, key, tag.get(key));
                }
            } else {
                addFormatted(event, " - No NBT Tags");
            }
        }
    }

    /**
     * Adds tooltips to items that are trowels (implementing ITrowel).
     *
     * @param event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addTrowelTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof IAgriTrowelItem) {
            event.getToolTip().add(AgriToolTips.TROWEL);
            IAgriTrowelItem trowel = (IAgriTrowelItem) stack.getItem();
            trowel.getGenome(stack).map(genome -> {
                event.getToolTip().add(AgriToolTips.getPlantTooltip(genome.getPlant()));
                trowel.getGrowthStage(stack).ifPresent(stage -> event.getToolTip().add(AgriToolTips.getGrowthTooltip(stage)));
                return genome.getStats();
            }).ifPresent(stats -> stats.addTooltips(text -> event.getToolTip().add(text)));
        }
    }

    /**
     * Adds tooltips to items that are clippers (implementing IAgriClipperItem).
     *
     * @param event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addClipperTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof IAgriClipperItem) {
            event.getToolTip().add(AgriToolTips.CLIPPER);
        }
    }

    /**
     * Adds tooltips to items that are rakes (implementing IAgriRakeItem).
     *
     * @param event
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addRakeTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof IAgriRakeItem) {
            event.getToolTip().add(AgriToolTips.RAKE);
        }
    }

}
