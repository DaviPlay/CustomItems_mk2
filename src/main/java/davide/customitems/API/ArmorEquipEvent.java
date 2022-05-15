package davide.customitems.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorEquipEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final EquipMethod equipType;
    private final ArmorType type;
    private ItemStack oldArmorPiece, newArmorPiece;

    public ArmorEquipEvent(final Player player, final EquipMethod equipType, final ArmorType type, final ItemStack oldArmorPiece, final ItemStack newArmorPiece){
        super(player);
        this.equipType = equipType;
        this.type = type;
        this.oldArmorPiece = oldArmorPiece;
        this.newArmorPiece = newArmorPiece;
    }

    public ArmorType getType() {
        return type;
    }

    public EquipMethod getEquipType() {
        return equipType;
    }

    public ItemStack getNewArmorPiece() {
        return newArmorPiece;
    }

    public ItemStack getOldArmorPiece() {
        return oldArmorPiece;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public void setOldArmorPiece(ItemStack oldArmorPiece) {
        this.oldArmorPiece = oldArmorPiece;
    }

    public void setNewArmorPiece(ItemStack newArmorPiece) {
        this.newArmorPiece = newArmorPiece;
    }

    public boolean isCancel() {
        return cancel;
    }

    @NotNull
    @Override
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum EquipMethod{
        SHIFT_CLICK,
        DRAG,
        PICK_DROP,
        HOTBAR,
        HOTBAR_SWAP,
        DISPENSER,
        BROKE,
        DEATH,
    }

    public enum ArmorType{
        HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);

        private final int slot;

        ArmorType(int slot){
            this.slot = slot;
        }

        public static ArmorType matchType(final ItemStack itemStack){
            if(ArmorListener.isAirOrNull(itemStack)) return null;
            String type = itemStack.getType().name();
            if(type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.endsWith("_HEAD")) return HELMET;
            else if(type.endsWith("_CHESTPLATE") || type.equals("ELYTRA")) return CHESTPLATE;
            else if(type.endsWith("_LEGGINGS")) return LEGGINGS;
            else if(type.endsWith("_BOOTS")) return BOOTS;
            else return null;
        }

        public int getSlot(){
            return slot;
        }
    }
}
