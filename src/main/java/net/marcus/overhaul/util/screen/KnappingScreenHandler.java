package net.marcus.overhaul.util.screen;

import net.marcus.overhaul.item.StoneKnappingItem;
import net.marcus.overhaul.util.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Arrays;

public class KnappingScreenHandler extends ScreenHandler{
    private final boolean[][] grid = new boolean[5][5];
    private final Inventory outputSlot = new SimpleInventory(1);
    private int decrement = 1;

    public KnappingScreenHandler(int syncId, PlayerInventory playerInventory, Item knappingItem) {
        super(ModScreenHandlers.KNAPPING_SCREEN_HANDLER, syncId);

        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col) {
                grid[row][col] = false;
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 48 + col * 18, 147 + row * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return !stack.getItem().equals(knappingItem);
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return !getStack().getItem().equals(knappingItem);
                    }
                });
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 48 + col * 18, 205) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return !stack.getItem().equals(knappingItem);
                }
                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                        return !getStack().getItem().equals(knappingItem);
                }
            });
        }
        this.addSlot(new Slot(outputSlot, 0, 160, 70) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return true;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                resetGrid();
            }
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack slotStack = slot.getStack();

            if (slotStack.getItem() instanceof StoneKnappingItem) {
                if (player.getMainHandStack().getItem() instanceof StoneKnappingItem) {
                    System.out.println("Cannot move StoneKnappingItem.");
                    return ItemStack.EMPTY;
                }
            }

            System.out.println("Quick-moving item from slot " + slotIndex + ": " + slotStack.getItem().getName().getString());

            // If the slot is the output slot (index 36)
            if (slotIndex == 36) {
                // Check if the item already exists in the player's inventory (slots 0-26)
                for (int i = 0; i < 27; i++) {
                    Slot inventorySlot = this.slots.get(i);
                    ItemStack inventoryStack = inventorySlot.getStack();
                    // If the item is found in the inventory and it can be stacked
                    if (!inventoryStack.isEmpty() && inventoryStack.getItem() == slotStack.getItem() && inventoryStack.getCount() < inventoryStack.getMaxCount()) {
                        resetGrid();
                        // Try to insert the item into that stack (combine them)
                        int spaceLeft = inventoryStack.getMaxCount() - inventoryStack.getCount();
                        int amountToAdd = Math.min(slotStack.getCount(), spaceLeft);
                        slotStack.decrement(amountToAdd);  // Decrease the stack in the output slot
                        inventoryStack.increment(amountToAdd);  // Increase the stack in the inventory
                        inventorySlot.markDirty();  // Mark the slot as dirty
                        if (slotStack.isEmpty()) {
                            slot.setStack(ItemStack.EMPTY);  // Empty the output slot if the stack is empty
                        }
                        return ItemStack.EMPTY;
                    }
                }

                // If the item was not found in the inventory, move it to the hotbar (slots 27-35)
                if (!this.insertItem(slotStack, 27, 36, false)) {
                    // If the hotbar is full, move it to the main inventory (slots 0-26)
                    if (!this.insertItem(slotStack, 0, 26, true)) {
                        System.out.println("Failed to move item from output slot to inventory");
                        return ItemStack.EMPTY;
                    }
                }
                slot.onTakeItem(player, slotStack);  // Trigger the transfer event
            }
            // If the slot is in the player's inventory or hotbar (slots 0-35)
            else if (slotIndex >= 0 && slotIndex < 36) {
                // Move the item from the player's inventory or hotbar to the output slot (index 36)
                if (this.slots.get(36).canInsert(slotStack)) {
                    if (!this.insertItem(slotStack, 36, 37, false)) {
                        return ItemStack.EMPTY;  // Return if the item cannot be moved to the output slot
                    }
                } else {
                    // If it can't be inserted into the output slot, move within the inventory or hotbar
                    if (slotIndex >= 0 && slotIndex < 27) {  // Main inventory (slots 0-26)
                        // Move to the hotbar (slots 27-35) starting from the leftmost slot (index 27)
                        if (!this.insertItem(slotStack, 27, 36, false)) {// 27 to 35 (left to right)
                            return ItemStack.EMPTY;
                        }
                    } else if (slotIndex >= 27 && slotIndex < 36) {  // Hotbar (slots 27-35)
                        // Move from hotbar to main inventory (slots 0-26)
                        if (!this.insertItem(slotStack, 0, 27, false)) {// 0 to 26 (left to right)
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            // If the stack is empty after moving, set the slot to empty
            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();  // Mark the slot as dirty
            }

            // If the stack size matches the original, return empty (no change)
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            // Call the slot's onTakeItem method
            slot.onTakeItem(player, slotStack);
        }
        return itemStack;
    }


    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        ItemStack outputStack = outputSlot.getStack(0);
        if (!outputStack.isEmpty()) {
            player.getInventory().offerOrDrop(outputStack);
        }
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public void setGrid(int x, int y, boolean value) {
        grid[x][y] = value;
    }

    public Inventory getOutputSlot() {
        return outputSlot;
    }

    public void handleGridClick(int row, int col, PlayerEntity player) {
        if (row >= 0 && row < 5 && col >= 0 && col < 5) {
            if (!grid[row][col]) {
                ItemStack itemInHand = player.getMainHandStack();
                if (decrement == 1) {
                    itemInHand.decrement(1);
                    decrement = 0;
                }
                grid[row][col] = true;
                Item result = matchesPattern(grid);
                if (result != null && outputSlot != null) {
                    outputSlot.setStack(0, new ItemStack(result));
                }
                if (result == null) {
                    outputSlot.setStack(0, ItemStack.EMPTY);
                }
            }
        }
    }

    private Item matchesPattern(boolean[][] grid) {
        for (KnappingPattern pattern : StoneKnappingPatterns.PATTERNS) {
            if (Arrays.deepEquals(grid, pattern.getPattern())) {
                return pattern.getResult();
            }
        }
        return null;
    }

    public void resetGrid() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = true;
            }
        }
    }
}
