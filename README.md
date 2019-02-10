# Analog Clock View
[![](https://jitpack.io/v/naz013/clock-view.svg)](https://jitpack.io/#naz013/clock-view)

<img src="https://github.com/naz013/clock-view/raw/master/app/src/main/ic_launcher-web.png" width="100" alt="Analog Clock View">

Simple analog clock view library for Android.

Inspired by this work - [UpLabs](https://www.uplabs.com/posts/ui-elements-alarm)
--------

Screenshot

<img src="https://github.com/naz013/emoji-rate-slider/raw/master/res/screenshot.png" width="400" alt="Screenshot">

Sample APP
--------
[Download](https://github.com/naz013/emoji-rate-slider/raw/master/app/release/app-release.apk)

[Google Play](https://play.google.com/store/apps/details?id=com.github.naz013.clockviewlibrary)


Download
--------
Download latest version with Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.naz013:clock-view:1.0.0'
}
```

Usage
-----
```xml
<com.github.naz013.clockview.ClockView
        android:id="@+id/clockView"
        android:layout_width="300dp"
        android:layout_height="wrap_content"
        app:cv_arrowsColor="#727889"
        app:cv_backgroundColor="#ffffff"
        app:cv_circlesColor="#807E8493"
        app:cv_hourLabelsColor="#727889"
        app:cv_labelTextSize="15sp"
        app:cv_shadowColor="#807E8493"
        app:cv_showCircles="true"
        app:cv_showHourArrow="true"
        app:cv_showHourLabels="true"
        app:cv_showMinuteArrow="true"
        app:cv_showSecondArrow="true"
        app:cv_showShadow="true" />
```


License
-------

    Copyright 2019 Nazar Suhovich

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
