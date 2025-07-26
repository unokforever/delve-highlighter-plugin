package com.delvehighlighter;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
    name = "Delve Highlighter",
    description = "Highlights unique items from the Delve boss loot interface and prevents accidental descending",
    tags = {"delve", "highlight", "loot", "boss", "pvm"}
)
public class DelveHighlighterPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private DelveHighlighterConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private DelveHighlighterOverlay overlay;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        // Handle game state changes if needed
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded)
    {
        // Detect when the Delve loot interface is opened
        if (widgetLoaded.getGroupId() == 919) // Delve loot interface widget group
        {
            log.debug("Delve loot interface detected");
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded menuEntryAdded)
    {
        if (!config.preventDescend())
        {
            return;
        }

        // Check if this is a "Descend" menu entry in the Delve and we have highlighted loot
        if (menuEntryAdded.getOption().equals("Descend") && hasHighlightedLoot())
        {
            log.debug("Deprioritizing Descend menu entry due to highlighted loot");
            
            // Add a dummy menu entry to push Descend to right-click
            client.getMenu()
                    .createMenuEntry(-1)
                    .setOption("Highlighted loot present!");

            // Make the original Descend option right-click only
            menuEntryAdded.getMenuEntry().setDeprioritized(true);
        }
    }

    private boolean hasHighlightedLoot()
    {
        Widget lootInterface = client.getWidget(919, 19);
        if (lootInterface == null || lootInterface.isHidden())
        {
            return false;
        }

        Widget[] children = lootInterface.getChildren();
        if (children == null)
        {
            return false;
        }

        for (Widget child : children)
        {
            if (child.getItemId() > 0)
            {
                int itemId = child.getItemId();
                
                // Check if this item should be highlighted
                switch (itemId)
                {
                    case 31109: // Mokhaiotl cloth
                        if (config.highlightMokhaiotlCloth()) return true;
                        break;
                    case 31088: // Avernic treads
                        if (config.highlightAvernicTreads()) return true;
                        break;
                    case 31111: // Demon tear
                        if (config.highlightDemonTear()) return true;
                        break;
                    case 31115: // Eye of ayak (uncharged)
                        if (config.highlightEyeOfAyak()) return true;
                        break;
                }
            }
        }

        return false;
    }

    @Provides
    DelveHighlighterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DelveHighlighterConfig.class);
    }
}