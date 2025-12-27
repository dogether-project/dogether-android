package site.dogether.presentation.composables.node

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.dogether.presentation.theme.ColorBgSurface

class SkeletonNode(
    var condition: Boolean,
    var color: Color,
    var widthDp: Dp,
    var heightDp: Dp,
    var radiusDp: Dp
) : DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        if (condition) {
            val finalWidth = if (widthDp > 0.dp) widthDp.toPx() else size.width
            val finalHeight = if (heightDp > 0.dp) heightDp.toPx() else size.height

            drawRoundRect(
                color = color,
                size = Size(finalWidth, finalHeight),
                cornerRadius = CornerRadius(radiusDp.toPx())
            )
        } else {
            drawContent()
        }
    }
}

data class SkeletonElement(
    val condition: Boolean,
    val color: Color,
    val widthDp: Dp,
    val heightDp: Dp,
    val radiusDp: Dp
) : ModifierNodeElement<SkeletonNode>() {

    override fun create(): SkeletonNode = SkeletonNode(
        condition = condition,
        color = color,
        widthDp = widthDp,
        heightDp = heightDp,
        radiusDp = radiusDp
    )

    override fun update(node: SkeletonNode) {
        node.condition = condition
        node.color = color
        node.widthDp = widthDp
        node.heightDp = heightDp
        node.radiusDp = radiusDp
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "skeleton"
        properties["condition"] = condition
        properties["color"] = color
        properties["widthDp"] = widthDp
        properties["heightDp"] = heightDp
        properties["radiusDp"] = radiusDp
    }
}

fun Modifier.skeleton(
    condition: Boolean,
    color: Color = ColorBgSurface,
    widthDp: Dp = 0.dp,
    heightDp: Dp = 0.dp,
    radiusDp: Dp = 8.dp
): Modifier = this.then(
    SkeletonElement(
        condition = condition,
        color = color,
        widthDp = widthDp,
        heightDp = heightDp,
        radiusDp = radiusDp
    )
)