package com.therxmv.churros.screenshot

import com.github.takahirom.robopreview.ComposablePreview
import com.github.takahirom.robopreview.ComposablePreviewScanner
import com.github.takahirom.robopreview.test
import com.github.takahirom.roborazzi.RoborazziRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.device.domain.RobolectricDeviceQualifiers

@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = RobolectricDeviceQualifiers.MediumPhone)
class PreviewScreenshotTest(private val preview: ComposablePreview<AndroidPreviewInfo>) {

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(outputDirectoryPath = "src/test/screenshots"),
    )

    @Test
    fun screenshot() = preview.test()

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun previews() = ComposablePreviewScanner()
            .scanPackageTrees("com.therxmv.churros")
            .getPreviews()
    }
}
