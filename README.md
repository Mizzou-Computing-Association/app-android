# app-android
Android App for Tigerhacks
Written in Kotlin

### How to Contribute

Create a SNSSecrets Object in app/src/main/kotlin/tigerhacks.android.tigerhacksapp/service/notifications/: 

```Kotlin
object SNSSecrets {
    const val platformArn = "YOUR_PLATFORM_ARN_HERE"
    const val identityPoolId = "YOUR_CONTIGO_POOL_ID"
    const val topicArn = "YOUR_SUBSCRIPTION_ARN"
}
```
