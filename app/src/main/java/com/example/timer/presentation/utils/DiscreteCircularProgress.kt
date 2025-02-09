
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

@Composable
fun DiscreteCircularProgress(
    remainingTime: Long,         // 남은 시간 (초)
    totalTime: Long = 360L,      // 전체 시간 (초)
    totalSegments: Int = 30,     // 전체 세그먼트 수
    canvasSize: Dp = 200.dp,           // Canvas 크기 (원형이라 가정)
    filledColor: Color = Color.Red,         // 진행 바 색상
    backgroundColor: Color = Color.LightGray, // 배경 세그먼트 색상
) {
    val toRadians = { degrees : Float -> degrees * Math.PI / 180.0 }
    // 각 세그먼트가 나타내는 시간 (예: 360/30 = 12초)
    val segmentDuration = totalTime / totalSegments.toFloat()
    // 경과 시간 = 전체 시간 - 남은 시간
    val elapsedTime = totalTime - remainingTime
    // 채워진 세그먼트 수: 처음에는 30, 경과 시간에 따라 감소
    val filledSegments
        = (totalSegments -
          floor(elapsedTime / segmentDuration).toInt()).coerceAtLeast(0)
    // 각 세그먼트가 차지하는 각도 (360/30)
    val segmentAngle = 360f / totalSegments
    // 원형의 배치: Canvas의 중심과 반지름 계산
    val density = LocalDensity.current
    val canvasSizePx = with(density) { canvasSize.toPx() }
    val segmentSizePx = (Math.PI.toFloat() * canvasSizePx) / (totalSegments * 1.8f)
    val thickness = 20f
    // ✅ 세그먼트 크기 자동 조정
    val center = Offset(canvasSizePx / 2, canvasSizePx / 2)
    // 세그먼트가 화면 안쪽에 그려지도록 반지름을 조정 (세그먼트 크기의 절반 만큼 빼줌)
    val radius = (canvasSizePx / 2) - (thickness / 2)  // ✅ 중앙 정렬 유지

    Canvas(modifier = Modifier.size(canvasSize)) {
        // 배경 세그먼트: 전체 30개 네모가 원 둘레에 배치됨
        for (i in 0 until totalSegments) {
            // 각 세그먼트의 중심 각도 (시작은 270°에서 시작)
            val angleDeg = 270f + i * segmentAngle
            val angleRad = toRadians(angleDeg).toFloat()
            // 세그먼트 중심 위치 계산 (원 둘레 상의 점)
            val segmentCenter = Offset(
                x = center.x + radius * cos(angleRad),
                y = center.y + radius * sin(angleRad)
            )

            // 회전 각도: 해당 세그먼트의 중심 각도 + 90° (원에 접하도록)
            val rotationDegrees = angleDeg + 90f
            // 배경 세그먼트 그리기
            rotate(degrees = rotationDegrees, pivot = segmentCenter) {
                drawRoundRect(
                    color = backgroundColor,
                    topLeft = Offset(segmentCenter.x - segmentSizePx / 2,
                                     segmentCenter.y - thickness / 2 - radius * 0.01f),
                                        // ✅ 세그먼트가 원 안쪽에 그려지도록 조정
                    size = Size(segmentSizePx, thickness),
                    cornerRadius = CornerRadius(x = segmentSizePx / 2, y = thickness / 4)
                )
            }
        }
        // 진행 세그먼트: 채워진 세그먼트 수만큼 다른 색상으로 덮어쓰기
        for (i in 0 until filledSegments) {
            val angleDeg = 270f + i * segmentAngle
            val angleRad = toRadians(angleDeg).toFloat()
            val segmentCenter = Offset(
                x = center.x + radius * cos(angleRad),
                y = center.y + radius * sin(angleRad)
            )

            val rotationDegrees = angleDeg + 90f

            rotate(degrees = rotationDegrees, pivot = segmentCenter) {
                drawRoundRect(
                    color = filledColor,
                    topLeft = Offset(segmentCenter.x - segmentSizePx / 2,
                                     segmentCenter.y - thickness / 2 - radius * 0.01f),
                                        // ✅ 세그먼트가 원 안쪽에 그려지도록 조정
                    size = Size(segmentSizePx, thickness),
                    cornerRadius = CornerRadius(x = segmentSizePx / 2, y = thickness / 4)
                )
            }
        }
    }
}