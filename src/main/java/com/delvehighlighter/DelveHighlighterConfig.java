package com.delvehighlighter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("delvehighlighter")
public interface DelveHighlighterConfig extends Config
{
    @ConfigItem(
        keyName = "highlightEnabled",
        name = "Enable Highlighting",
        description = "Enable highlighting of Delve unique items"
    )
    default boolean highlightEnabled()
    {
        return true;
    }

    @ConfigItem(
        keyName = "highlightMokhaiotlCloth",
        name = "Highlight Mokhaiotl cloth",
        description = "Highlight the Mokhaiotl cloth unique drop (ID: 31109)"
    )
    default boolean highlightMokhaiotlCloth()
    {
        return true;
    }

    @ConfigItem(
        keyName = "highlightAvernicTreads",
        name = "Highlight Avernic treads",
        description = "Highlight the Avernic treads unique drop (ID: 31088)"
    )
    default boolean highlightAvernicTreads()
    {
        return true;
    }

    @ConfigItem(
        keyName = "highlightDemonTear",
        name = "Highlight Demon tear",
        description = "Highlight the Demon tear unique drop (ID: 31111)"
    )
    default boolean highlightDemonTear()
    {
        return true;
    }

    @ConfigItem(
        keyName = "highlightEyeOfAyak",
        name = "Highlight Eye of ayak (uncharged)",
        description = "Highlight the Eye of ayak (uncharged) unique drop (ID: 31115)"
    )
    default boolean highlightEyeOfAyak()
    {
        return true;
    }

    @ConfigItem(
        keyName = "highlightColor",
        name = "Highlight Color",
        description = "Color to use for highlighting items"
    )
    default java.awt.Color highlightColor()
    {
        return java.awt.Color.YELLOW;
    }

    @ConfigItem(
        keyName = "preventDescend",
        name = "Prevent Accidental Descend",
        description = "Make 'Descend' right-click only when highlighted loot is present"
    )
    default boolean preventDescend()
    {
        return true;
    }
}