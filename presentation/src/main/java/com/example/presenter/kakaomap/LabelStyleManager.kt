package com.example.presenter.kakaomap

import com.example.presenter.R
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

object LabelStyleManager {

    operator fun invoke(type: LabelStyleType): LabelStyles {
        return when (type) {
            LabelStyleType.BASIC -> {
                LabelStyles.from(
                    LabelStyle.from(R.drawable.red_pin)
                        .setIconTransition(
                            com.kakao.vectormap.label.LabelTransition.from(
                                com.kakao.vectormap.label.Transition.None,
                                com.kakao.vectormap.label.Transition.None
                            )
                        )
                )
            }
        }
    }
}

enum class LabelStyleType {
    BASIC
}