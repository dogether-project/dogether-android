package site.dogether.presentation.composables.node

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.dogether.presentation.theme.ColorBgSurface

class SkeletonNode(
    var condition: Boolean,
    var color: Color,
    var widthDp: Dp,
    var heightDp: Dp,
    var radiusDp: Dp
) : DrawModifierNode, LayoutModifierNode, Modifier.Node() {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val width = if (condition && widthDp > 0.dp) widthDp.roundToPx() else null
        val height = if (condition && heightDp > 0.dp) heightDp.roundToPx() else null

        val placeable = measurable.measure(
            constraints.copy(
                minWidth = width ?: constraints.minWidth,
                maxWidth = width ?: constraints.maxWidth,
                minHeight = height ?: constraints.minHeight,
                maxHeight = height ?: constraints.maxHeight
            )
        )

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun ContentDrawScope.draw() {
        if (condition) {
            drawRoundRect(
                color = color,
                size = size,
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
        node.invalidateMeasurement()
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