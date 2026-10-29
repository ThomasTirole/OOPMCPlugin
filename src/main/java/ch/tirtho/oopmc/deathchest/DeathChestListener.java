package ch.tirtho.oopmc.deathchest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeathChestListener implements Listener {

    private final NamespacedKey ownerKey;
    private final NamespacedKey lootedKey;

    public DeathChestListener(JavaPlugin plugin) {
        this.ownerKey = new NamespacedKey(plugin, "death_chest_owner");
        this.lootedKey = new NamespacedKey(plugin, "death_chest_looted");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock();

        // Nothing to store, or no valid spot (void, build limit, suffocation) → native drop
        if (event.getDrops().isEmpty() || !canPlaceChest(block)) return;

        // Place a double chest if a side is free, otherwise a single chest
        BlockFace side = findFreeSide(block);
        if (side != null) {
            placeDoubleChest(block, side);
            markAsDeathChest(block.getRelative(side), player);
        } else {
            block.setType(Material.CHEST);
        }
        markAsDeathChest(block, player);

        // Fill the chest, drop what doesn't fit, and only then clear the native drops
        if (block.getState() instanceof Chest chest) {
            Map<Integer, ItemStack> leftovers = chest.getInventory().addItem(event.getDrops().toArray(new ItemStack[0]));
            for (ItemStack item : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) return;

        Block block = event.getClickedBlock();
        if (block == null || !(block.getState() instanceof Chest chest)) return;

        // Not a death chest, or already looted once → normal opening
        String owner = chest.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
        if (owner == null || chest.getPersistentDataContainer().has(lootedKey)) return;

        // Only the owner gets the quick loot
        Player player = event.getPlayer();
        if (!owner.equals(player.getUniqueId().toString())) return;

        event.setCancelled(true);
        List<Block> chestBlocks = getChestBlocks(chest);

        // Move everything into the player's inventory
        Inventory chestInventory = chest.getInventory();
        ItemStack[] contents = Arrays.stream(chestInventory.getContents())
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new);
        chestInventory.clear();
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(contents);

        if (leftovers.isEmpty()) {
            removeDeathChest(chestBlocks);
            player.sendMessage(Component.text("Tout ton stuff a été récupéré !", NamedTextColor.GREEN));
            return;
        }

        // Inventory full: put the rest back and allow normal opening from now on
        chestInventory.addItem(leftovers.values().toArray(new ItemStack[0]));
        markAsLooted(chestBlocks);
        player.sendMessage(Component.text("Inventaire plein ! Il reste " + leftovers.size()
                + " objet(s) différent(s) dans le coffre.", NamedTextColor.YELLOW));
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!inventory.isEmpty()) return;

        Chest chest = toChest(inventory.getHolder());
        if (chest != null && isDeathChest(chest)) {
            removeDeathChest(getChestBlocks(chest));
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest chest && isDeathChest(chest)) {
            event.setDropItems(false);
        }
    }

    private boolean canPlaceChest(Block block) {
        int y = block.getY();
        return y >= block.getWorld().getMinHeight()
                && y < block.getWorld().getMaxHeight()
                && block.isReplaceable();
    }

    private boolean isDeathChest(Chest chest) {
        return chest.getPersistentDataContainer().has(ownerKey);
    }

    private void markAsDeathChest(Block block, Player owner) {
        if (block.getState() instanceof Chest chest) {
            chest.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, owner.getUniqueId().toString());
            chest.update();
        }
    }

    private void markAsLooted(List<Block> blocks) {
        for (Block block : blocks) {
            if (block.getState() instanceof Chest chest) {
                chest.getPersistentDataContainer().set(lootedKey, PersistentDataType.BOOLEAN, true);
                chest.update();
            }
        }
    }

    private void removeDeathChest(List<Block> blocks) {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
    }

    private List<Block> getChestBlocks(Chest chest) {
        if (chest.getInventory().getHolder() instanceof DoubleChest doubleChest
                && doubleChest.getLeftSide() instanceof Chest left
                && doubleChest.getRightSide() instanceof Chest right) {
            return List.of(left.getBlock(), right.getBlock());
        }
        return List.of(chest.getBlock());
    }

    private Chest toChest(InventoryHolder holder) {
        if (holder instanceof DoubleChest doubleChest && doubleChest.getLeftSide() instanceof Chest left) {
            return left;
        }
        if (holder instanceof Chest chest) {
            return chest;
        }
        return null;
    }

    private boolean isFree(Block block) {
        return block.isReplaceable();
    }

    private BlockFace findFreeSide(Block block) {
        for (BlockFace direction : List.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH)) {
            if (isFree(block.getRelative(direction))) {
                return direction;
            }
        }
        return null;
    }

    private void placeDoubleChest(Block block, BlockFace side) {
        Block adjacentBlock = block.getRelative(side);
        block.setType(Material.CHEST);
        adjacentBlock.setType(Material.CHEST);
        BlockFace facing = switch (side) {
            case EAST -> BlockFace.NORTH;
            case WEST -> BlockFace.SOUTH;
            case NORTH -> BlockFace.WEST;
            case SOUTH -> BlockFace.EAST;
            default -> BlockFace.NORTH;
        };
        setChestHalf(block, facing, org.bukkit.block.data.type.Chest.Type.LEFT);
        setChestHalf(adjacentBlock, facing, org.bukkit.block.data.type.Chest.Type.RIGHT);
    }

    private void setChestHalf(Block block, BlockFace facing, org.bukkit.block.data.type.Chest.Type type) {
        org.bukkit.block.data.type.Chest data = (org.bukkit.block.data.type.Chest) block.getBlockData();
        data.setFacing(facing);
        data.setType(type);
        block.setBlockData(data);
    }
}