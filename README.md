# AlarmApp

Alarm App

Features:
-swipe delete alarm item functionality
-schedule repeating alarms
-alarm off and alarm on functionality​
-snooze for 5 Minutes
-user friendly interface
-Unit Testing 
-UI Testing
And lots more

Installation
We assume you've installed android studio already, Unzip the file and open it in android studio, sync and run..
Note: you need a basic knowledge of java or kotlin to understand this app’s source code.

How To Use
-After installing successfully on android studio, run on a virtual device or a physical device.
 navigate to the home page, click on the FAB(floating action button) “+” to create a new alarm.
-On click FAB(floating action button) “+” Display dialog for date selection and time selection
And choose your specific logo,then navigate to the manifest.xml file, look for android:roundIcon under application and change it to yours.

Unit test
-review package(test directory)
-in package find repository directory which contain fakeRepository for unit test 
-in ui package find AlarmViewModelTest which contain all testcases of viewmodel 

UI test using Hilt
-review package(Android test directory)
-in package find data.local directory which contain Alarm Dao test cases
-repository directory which contain fakeRepository for android test 
-ui.fragment contain all view testing testcases 

------
For test Runner i use Hilt runner Custom you can find in AndroidTest HiltTestRunner named class and also it register in build.gradle (Project)in andorid body

LiveDataUtilAndroidTest ->
-use for livedata testing 

HiltExt ->
contain extensions which help us to launch fragments