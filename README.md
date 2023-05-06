# Introduction

The project is to send and open a social app, and the basis of the software is introduced as follows:

**1、User Matching**
It can find like-minded communication partners for users through soul matching and voice matching.
**2、Instant communication**
In instant messaging, it can ensure the stable delivery of conversation messages. Users can send text messages, emoji messages, picture messages, voice messages, file messages, etc. You can make voice calls and video calls with other users. Enables users to view and search message history. Users can view the recent instant messaging session records.

**3、Audio and video calls**
It can provide stable audio and video call service for users in voice matching and instant messaging to ensure stable conversation connection and good sound and picture quality. Yes,
**4、Server performance optimization**
Front-end and back-end data interaction through RESTFUL API. By optimizing the performance of the back-end service, we can shorten the database query time consumption and reduce the logic processing time to improve the service performance. Later, load balancing, content distribution center, message middleware and other components can be introduced in the architecture to further improve the performance of the application and provide users with excellent service availability.

# EaseIM

------

## Introduction

EaseIM is a complete chat app similar to WeChat, developed based on the EaseIM SDK. It includes features such as user registration, login, adding friends, one-on-one chat, group chat, sending text, emojis, voice messages, images, location messages, and real-time audio and video calls.

## Creating an Application and Getting an AppKey

Before running the demo, please create your own application and get the Appkey from the [EaseIM Management Console](https://console.easemob.com/user/register). Then configure the Appkey in AndroidManifest.xml.

> ```
> JavaCopy code<!-- Set the AppKey for the EaseIM application -->
> <meta-data android:name="EASEMOB_APPKEY"  android:value="Appkey obtained from creating the application" />
> ```
>
> For details on registering and creating applications, please visit the EaseIM website: [Register and Create Applications](http://docs-im.easemob.com/im/quickstart/guide/experience#注册并创建应用)

## How to Integrate the EaseIM SDK

Please visit the EaseIM website: [Introduction to the Android SDK and Importing It](http://docs-im.easemob.com/im/android/sdk/import)

## Experience EaseIM

Please visit the EaseIM website: [Scene Demos and Source Code Downloads](https://www.easemob.com/download/im)

## Application Architecture

The EaseIM app adopts the application architecture recommended by Google: ![img](https://developer.android.google.cn/topic/libraries/architecture/images/final-architecture.png) This architecture has the following advantages:

> (1) Decoupling of UI and business logic.</br> (2) Effective prevention of memory leaks in lifecycle components.</br> (3) Improved module testability.</br> (4) Improved application stability, effectively reducing the probability of the following exceptions occurring:</br>

Understanding the different parts of the architecture:

> (1) Activity/Fragment is the View layer, mainly responsible for displaying and refreshing data and triggering interaction events.</br> (2) ViewModel is a class used to save application UI data. It will continue to exist after configuration changes (Configuration Change).</br> (3) LiveData is an observable data holder class with lifecycle awareness.</br> (4) Repository mainly handles various data-related business requests (network requests and database queries). It can be regarded as part of the Model layer.</br> (5) Room is an abstraction layer added on top of SQLite to provide more powerful database access, and can directly return LiveData to listen for data returns.</br>
