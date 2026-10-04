/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.keyguard.ui.view.layout.sections

import androidx.constraintlayout.widget.ConstraintSet
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.android.systemui.SysuiTestCase
import com.android.systemui.axdynamicbar.ui.AxDynamicBarChipViewModel
import com.android.systemui.keyguard.domain.interactor.KeyguardClockInteractor
import com.android.systemui.res.R
import com.android.systemui.statusbar.KeyguardIndicationController
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
@SmallTest
class AxDynamicBarKeyguardChipSectionTest : SysuiTestCase() {
    @Mock private lateinit var viewModel: AxDynamicBarChipViewModel
    @Mock private lateinit var indicationController: KeyguardIndicationController
    @Mock private lateinit var clockInteractor: KeyguardClockInteractor

    private lateinit var underTest: AxDynamicBarKeyguardChipSection
    private lateinit var isKeyguardExpanded: MutableStateFlow<Boolean>
    private lateinit var isLowUdfps: MutableStateFlow<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        isKeyguardExpanded = MutableStateFlow(false)
        isLowUdfps = MutableStateFlow(false)
        whenever(viewModel.isKeyguardExpanded).thenReturn(isKeyguardExpanded)
        whenever(viewModel.isLowUdfps).thenReturn(isLowUdfps)
        underTest = AxDynamicBarKeyguardChipSection(mContext, viewModel, indicationController, clockInteractor)
    }

    @Test
    fun applyConstraints_collapsedNonLowUdfps_usesMatchConstraintWidth() {
        val chipConstraints = ConstraintSet().also(underTest::applyConstraints)
            .getConstraint(R.id.ax_dynamic_bar_keyguard_chip).layout
        assertThat(chipConstraints.mWidth).isEqualTo(ConstraintSet.MATCH_CONSTRAINT)
        assertThat(chipConstraints.startToEnd).isEqualTo(R.id.start_button)
        assertThat(chipConstraints.endToStart).isEqualTo(R.id.end_button)
    }

    @Test
    fun applyConstraints_collapsedLowUdfps_keepsWrapContentWidth() {
        isLowUdfps.value = true

        val chipConstraints = ConstraintSet().also(underTest::applyConstraints)
            .getConstraint(R.id.ax_dynamic_bar_keyguard_chip).layout
        assertThat(chipConstraints.mWidth).isEqualTo(ConstraintSet.WRAP_CONTENT)
        assertThat(chipConstraints.startToStart).isEqualTo(ConstraintSet.PARENT_ID)
        assertThat(chipConstraints.endToEnd).isEqualTo(ConstraintSet.PARENT_ID)
    }
}
