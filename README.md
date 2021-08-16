# AlarmApp

Alarm App

## Features:
#### -swipe delete alarm item functionality
#### -schedule repeating alarms
#### -alarm off and alarm on functionality​
#### -snooze for 5 Minutes
#### -user friendly interface
#### -Unit Testing 
#### -UI Testing
#### And lots more

## Installation

### IDE
1. Download latest Android Studio from https://developer.android.com/studio/index.html
1. Follow Android Studio installation instruction.
1. Open Android Studio - Open Existing Android Project - find folder with project and click `OK`
1. Wait a while. Follow Android Studio instructions to install missing items.
1. Press `cmd/ctrl + shift + a` and type `AVD Manager` and press Enter.
1. Press `Create Virtual Device...` button.
1. Select `any device`
1. Select latest API level (in case if latest is not available then click `Download` and wait, it's going to take a while).
1. Click `Next`
1. Click `Finish`

Download zip and Unzip the file and open it in android studio,
Note: you need a basic knowledge of java or kotlin to understand this app’s source code.

Take a look at [Build and run app](https://developer.android.com/studio/run).

1. Open Android Studio

2. Import Project by Android Studio Menu > File > Import Project...
    **OR**
2. Open Project by Android Studio Menu > File > Open...

3. Goto Menu > File > Sync project with gradle Files

4. Wait for build gradle, i will take sometime to download mandatory things

5. After successful gradle building Goto Menu > Run > Run app OR by using windows shortcut **shift+F10**

## Troubleshoot
First of all, take a look at [troubleshooting guide](https://developer.android.com/studio/known-issues). Most setup issues can be solved by following this guide.
Goto Menu > File > Invalidate caches/Restart
**OR**
Goto Menu > Build > Clean Project and Menu > Build > Build Project
**OR**
Goto Menu > File > Invalidate caches/Restart

## How To Use
- navigate to the home page, click on the FAB(floating action button) “+” to create a new alarm.
- On click FAB(floating action button) “+” Display dialog for date selection and time selection
- swap to delete Alarm item from database

## Unit test
-review package(test directory)
-in package find repository directory which contain fakeRepository for unit test 
-in ui package find AlarmViewModelTest which contain all testcases of viewmodel 

## UI test using Hilt
-review package(Android test directory)
-in package find data.local directory which contain Alarm Dao test cases
-repository directory which contain fakeRepository for android test 
-ui.fragment contain all view testing testcases 

------
### For test Runner i use Hilt runner Custom you can find in AndroidTest HiltTestRunner named class and also it register in build.gradle (Project)in andorid body

## LiveDataUtilAndroidTest ->
-use for livedata testing 

## HiltExt ->
contain extensions which help us to launch fragments

### I have using Jetpack navigation component My motive to handle in future for example if we want to use add alarm as new fragment then we will easily handle navigation as well testing