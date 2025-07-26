package com.delvehighlighter;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DelveHighlighterOverlay extends Overlay
{
    private final Client client;
    private final DelveHighlighterConfig config;

    // Map of item IDs to their configuration method names
    private static final Map<Integer, String> DELVE_ITEMS = new HashMap<>();
    static {
        DELVE_ITEMS.put(31109, "MokhaiotlCloth");   // Mokhaiotl cloth
        DELVE_ITEMS.put(31088, "AvernicTreads");    // Avernic treads
        DELVE_ITEMS.put(31111, "DemonTear");        // Demon tear
        DELVE_ITEMS.put(31115, "EyeOfAyak");        // Eye of ayak (uncharged)
    }

    @Inject
    public DelveHighlighterOverlay(Client client, DelveHighlighterConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.highlightEnabled())
        {
            return null;
        }

        // Check if the Delve loot interface is open
        Widget lootInterface = client.getWidget(919, 19); // Delve loot contents widget
        if (lootInterface == null || lootInterface.isHidden())
        {
            return null;
        }

        // Get all widget items in the loot interface
        Widget[] children = lootInterface.getChildren();
        if (children == null)
        {
            return null;
        }

        for (Widget child : children)
        {
            if (child.getItemId() > 0)
            {
                highlightItem(graphics, child);
            }
        }

        return null;
    }

    private void highlightItem(Graphics2D graphics, Widget widget)
    {
        int itemId = widget.getItemId();
        
        if (!DELVE_ITEMS.containsKey(itemId))
        {
            return;
        }

        String itemConfigKey = DELVE_ITEMS.get(itemId);
        boolean shouldHighlight = false;

        // Check if this specific item should be highlighted based on config
        switch (itemConfigKey)
        {
            case "MokhaiotlCloth":
                shouldHighlight = config.highlightMokhaiotlCloth();
                break;
            case "AvernicTreads":
                shouldHighlight = config.highlightAvernicTreads();
                break;
            case "DemonTear":
                shouldHighlight = config.highlightDemonTear();
                break;
            case "EyeOfAyak":
                shouldHighlight = config.highlightEyeOfAyak();
                break;
        }

        if (shouldHighlight)
        {
            Rectangle bounds = widget.getBounds();
            graphics.setColor(config.highlightColor());
            graphics.setStroke(new BasicStroke(2));
            graphics.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}