package com.example.timer.presentation.service

import androidx.wear.tiles.TileProviderService
import androidx.wear.tiles.builders.ActionBuilders
import androidx.wear.tiles.builders.DimensionBuilders
import androidx.wear.tiles.builders.LayoutElementBuilders
import androidx.wear.tiles.builders.ModifiersBuilders
import androidx.wear.tiles.builders.ResourceBuilders
import androidx.wear.tiles.builders.TileBuilders
import androidx.wear.tiles.builders.TimelineBuilders
import androidx.wear.tiles.readers.RequestReaders
import com.example.timer.presentation.ui.MainActivity
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class TileService : TileProviderService() {

    companion object {
        private const val RESOURCES_VERSION = "1"
    }

    override fun onTileRequest(requestParams: RequestReaders.TileRequest):
            ListenableFuture<androidx.wear.tiles.builders.TileBuilders.Tile> {

        val launchAction = ActionBuilders.LaunchAction.builder()
            .setAndroidActivity(
                ActionBuilders.AndroidActivity.builder()
                    .setPackageName(packageName)
                    .setClassName(MainActivity::class.java.name)
                    .build()
            )
            .build()


        val startText = LayoutElementBuilders.Text.builder()
            .setText("Start")
            .build()


        val clickableBox = LayoutElementBuilders.Box.builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.wrap())
            .setModifiers(
                ModifiersBuilders.Modifiers.builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.builder()
                            .setOnClick(launchAction)
                            .build()
                    )
                    .build()
            )
            .addContent(startText)
            .build()


        val layout = LayoutElementBuilders.Layout.builder()
            .setRoot(clickableBox)
            .build()


        val timelineEntry = TimelineBuilders.TimelineEntry.builder()
            .setLayout(layout)
            .build()


        val timeline = TimelineBuilders.Timeline.builder()
            .addTimelineEntry(timelineEntry)
            .build()


        val tile = TileBuilders.Tile.builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(timeline)
            .build()

        return Futures.immediateFuture(tile)
    }

    override fun onResourcesRequest(requestParams: RequestReaders.ResourcesRequest):
            ListenableFuture<ResourceBuilders.Resources> {

        val resources = ResourceBuilders.Resources.builder()
            .setVersion(RESOURCES_VERSION)
            .build()

        return Futures.immediateFuture<ResourceBuilders.Resources>(resources)
    }
}
