package com.brandon3055.draconicevolution.client.gui.toolconfig;

import com.brandon3055.brandonscore.client.gui.ButtonColourRect;
import com.brandon3055.brandonscore.client.utils.GuiHelper;
import com.brandon3055.brandonscore.inventory.PlayerSlot;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.itemconfig.IConfigurableItem;
import com.brandon3055.draconicevolution.handlers.ContributorHandler;
import com.brandon3055.draconicevolution.network.PacketContributor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.Arrays;

import static com.brandon3055.brandonscore.inventory.PlayerSlot.EnumInvCategory.*;

/**
 * Created by brandon3055 on 2/06/2016.
 */
public class GuiToolConfig extends GuiScreen {
    private EntityPlayer player;
    private ToolButton[] armor;
    private ToolButton[] inventory;
    private ToolButton[] offHand;
    private ButtonColourRect configButton;

    public GuiToolConfig(EntityPlayer player){
        this.player = player;
        this.armor = new ToolButton[player.inventory.armorInventory.length];
        this.inventory = new ToolButton[player.inventory.mainInventory.length];
        this.offHand = new ToolButton[player.inventory.offHandInventory.length];
    }

    @Override
    public void initGui() {
        super.initGui();
        int centerX = width / 2;
        int centerY = 40 + height / 2;
        buttonList.clear();
        int id = 0;

        for (int i = 0; i < player.inventory.armorInventory.length; i++){
            ItemStack stack = player.inventory.armorInventory[i];
            int x = - 81 + centerX;// + i % 2 * 80;
            int y = - 52 + centerY - i * 18;
            buttonList.add(armor[i] = new ToolButton(id, x, y, 18, 18, stack, new PlayerSlot(i, ARMOR)));
            id++;
        }

        for (int i = 0; i < player.inventory.mainInventory.length; i++){
            ItemStack stack = player.inventory.mainInventory[i];
            int x = - 81 + centerX + i % 9 * 18;
            int y = + 30 + centerY - i / 9 * 18;
            if (i < 9) {
                y += 1;
            }
            buttonList.add(inventory[i] = new ToolButton(id, x, y, 18, 18, stack, new PlayerSlot(i, MAIN)));
            id++;
        }

        for (int i = 0; i < player.inventory.offHandInventory.length; i++){
            ItemStack stack = player.inventory.offHandInventory[i];
            int x = + 24 + centerX;
            int y = - 52 + centerY + i * 18;
            buttonList.add(offHand[i] = new ToolButton(id, x, y, 18, 18, stack, new PlayerSlot(i, OFF_HAND)));
            id++;
        }

        buttonList.add(configButton = new ButtonColourRect(id, I18n.format("gui.de.toolConfig.hud.txt"), centerX + 23, centerY - 107, 59, 16, 0x88000000, 0xFF440066, 0xFF009900));


        if (ContributorHandler.contributors.containsKey(player.getName())) {
            ContributorHandler.Contributor contributor = ContributorHandler.contributors.get(player.getName());
            if (!contributor.isUserValid(player)) {
                return;
            }

            if (contributor.contributionLevel >= 1) {
                buttonList.add(new ButtonColourRect(44, "Contributor Wings: " + (contributor.contributorWingsEnabled ? "Enabled" : "Disabled"), centerX - 240, centerY - 100, 150, 20, 0x88000000, 0xFF440066, 0xFF009900));
            }
            if (contributor.contribution.toLowerCase().contains("patreon")) {
                buttonList.add(new ButtonColourRect(45, "Patreon Badge: " + (contributor.patreonBadgeEnabled ? "Enabled" : "Disabled"), centerX - 240, centerY + 22 - 100, 150, 20, 0x88000000, 0xFF440066, 0xFF009900));
            }
        }
    }

    //region Draw

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = 40 + height / 2;
        drawDefaultBackground();

        //region Draw Inventory Back

        GuiHelper.drawColouredRect(centerX - 90, centerY - 115, 180, 173, 0xAF000000);
        int border = 0xFF005555;
        GuiHelper.drawColouredRect(centerX - 90, centerY - 117, 180, 2, border);
        GuiHelper.drawColouredRect(centerX - 90, centerY + 56, 180, 2, border);
        GuiHelper.drawColouredRect(centerX - 90, centerY - 117, 2, 175, border);
        GuiHelper.drawColouredRect(centerX + 88, centerY - 117, 2, 175, border);

        //Draw Main Inventory //TODO Replace all "drawGradientRect" with GuiHelper.drawColouredRect
        int posX = centerX - 81;
        int posY = centerY - 24;
        this.drawGradientRect(posX - 1, posY - 1, posX + 163, posY + 74, 0xFF00FFFF, 0xFF00FFFF);
        this.drawGradientRect(posX, posY, posX + 162, posY + 73, 0xFF000000, 0xFF000000);
        posY = centerY + 30;
        this.drawGradientRect(posX - 1, posY, posX + 163, posY + 1, 0xFF00FFFF, 0xFF00FFFF);
        //Draw Off Hand
        posX = centerX + 24;
        posY = centerY - 52;
        this.drawGradientRect(posX - 1, posY - 1, posX + 19, posY + 19, 0xFF00FFFF, 0xFF00FFFF);
        this.drawGradientRect(posX, posY, posX + 18, posY + 18, 0xFF000000, 0xFF000000);
        //Draw Armor
        posX = centerX - 81;
        posY = centerY - 106;
        this.drawGradientRect(posX - 1, posY - 1, posX + 19, posY + 73, 0xFF00FFFF, 0xFF00FFFF);
        this.drawGradientRect(posX, posY, posX + 18, posY + 72, 0xFF000000, 0xFF000000);

        //endregion

        //region Draw Player

        int playerX = centerX;
        int playerY = centerY - 42;

        this.drawGradientRect(playerX - 21, playerY - 65, playerX + 21, playerY + 9, 0xFF00FFFF, 0xFF00FFFF);
        this.drawGradientRect(playerX - 20, playerY - 64, playerX + 20, playerY + 8, 0xFF000000, 0xFF000000);

        GuiInventory.drawEntityOnScreen(playerX, playerY, 30, playerX - mouseX, playerY - 50 - mouseY, player);
        //endregion

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiButton button : buttonList){
            if (button instanceof ToolButton){
                ((ToolButton)button).drawToolTips(mc, mouseX, mouseY);
            }
        }

        if (GuiHelper.isInRect(centerX + 23, centerY - 107, 59, 16, mouseX, mouseY)) {
            drawHoveringText(Arrays.asList(I18n.format("gui.de.toolConfig.hudDesc.txt")), mouseX, mouseY);
        }
    }

    //endregion

    //region Interaction

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == configButton.id) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHudConfig(this));
        }
        else if (button instanceof ToolButton){
            ToolButton toolButton = (ToolButton) button;

            if (toolButton.stack != null && toolButton.stack.getItem() instanceof IConfigurableItem){
                mc.displayGuiScreen(new GuiConfigureTool(this, player, ((ToolButton) button).stack, ((ToolButton) button).slot));
            }
        }
        if (ContributorHandler.contributors.containsKey(player.getName())) {
            ContributorHandler.Contributor contributor = ContributorHandler.contributors.get(player.getName());
            if (!contributor.isUserValid(player)) return;

            if (button.id == 44) {
                contributor.contributorWingsEnabled = !contributor.contributorWingsEnabled;
                button.displayString = "Contributor Wings: " + (contributor.contributorWingsEnabled ? "Enabled" : "Disabled");
            } else if (button.id == 45) {
                contributor.patreonBadgeEnabled = !contributor.patreonBadgeEnabled;
                button.displayString = "Patreon Badge: " + (contributor.patreonBadgeEnabled ? "Enabled" : "Disabled");
            }

            DraconicEvolution.network.sendToServer(new PacketContributor(player.getName(), contributor.contributorWingsEnabled, contributor.patreonBadgeEnabled));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
        }
    }

    //endregion

    //region Helpers

    public boolean isItemValid(ItemStack stack){
        return stack != null && stack.getItem() instanceof IConfigurableItem;
    }

    //endregion

    public class ToolButton extends GuiButton {
        public ItemStack stack = null;
        public final PlayerSlot slot;

        public ToolButton(int buttonId, int x, int y, int widthIn, int heightIn, ItemStack stack, PlayerSlot slot) {
            super(buttonId, x, y, widthIn, heightIn, "");
            this.stack = stack;
            this.slot = slot;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
           // super.drawButton(mc, mouseX, mouseY);

            if (stack != null){
                if (hovered && stack.getItem() instanceof IConfigurableItem ){
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    int j1 = xPosition;
                    int k1 = yPosition;
                    GlStateManager.colorMask(true, true, true, false);
                    this.drawGradientRect(j1, k1, j1 + 18, k1 + 18, 0x3000ffff, 0x3000ffff);
                    GlStateManager.colorMask(true, true, true, true);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                }

                mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer, stack, xPosition + 1, yPosition + 1);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, xPosition + 1, yPosition + 1, stack.stackSize > 1 ? String.valueOf(stack.stackSize) : "");
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.colorMask(true, true, true, false);
            if (stack == null || !(stack.getItem() instanceof IConfigurableItem)){
                this.drawGradientRect(xPosition, yPosition, xPosition + 18, yPosition + 18, 0xB0000000, 0xB0000000);
                GuiHelper.drawColouredRect(xPosition + 1, yPosition + 1, 1, 16, 0x55FF0000);
                GuiHelper.drawColouredRect(xPosition + 16, yPosition + 1, 1, 16, 0x55FF0000);
                GuiHelper.drawColouredRect(xPosition + 2, yPosition + 1, 14, 1, 0x55FF0000);
                GuiHelper.drawColouredRect(xPosition + 2, yPosition + 16, 14, 1, 0x55FF0000);
            } else {
//                GuiHelper.drawColouredRect(xPosition + 1, yPosition + 1, 1, 16, 0x5500FF00);
//                GuiHelper.drawColouredRect(xPosition + 16, yPosition + 1, 1, 16, 0x5500FF00);
//                GuiHelper.drawColouredRect(xPosition + 2, yPosition + 1, 14, 1, 0x5500FF00);
//                GuiHelper.drawColouredRect(xPosition + 2, yPosition + 16, 14, 1, 0x5500FF00);
                GuiHelper.drawColouredRect(xPosition, yPosition, 1, 18, 0x8800FF00);
                GuiHelper.drawColouredRect(xPosition + 17, yPosition, 1, 18, 0x8800FF00);
                GuiHelper.drawColouredRect(xPosition + 1, yPosition, 16, 1, 0x8800FF00);
                GuiHelper.drawColouredRect(xPosition + 1, yPosition + 17, 16, 1, 0x8800FF00);
            }
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

        }

        public void drawToolTips(Minecraft mc, int mouseX, int mouseY){
            if (hovered && stack != null && stack.getItem() instanceof IConfigurableItem) {
                renderToolTip(stack, mouseX, mouseY);
            }
        }

        @Override
        public void playPressSound(SoundHandler soundHandlerIn) {
            if (stack != null && stack.getItem() instanceof IConfigurableItem) {
                super.playPressSound(soundHandlerIn);
            }
            else if (stack != null){
                mc.thePlayer.addChatComponentMessage(new TextComponentTranslation("chat.toolConfig.thatItemNotConfigurable.msg").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
            }
        }
    }
}
