# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ChanYiChih/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses
-dontwarn com.squareup.okhttp.**

-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
    public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
    public *;
}

-keepclassmembers class **.R$* {
  public static <fields>;
}

-keep class **.R
-keep class **.R$* {
    <fields>;
}

-keep public interface **
-keep public enum **

-dontwarn java.nio.file.*
-dontwarn com.ext.animal_sniffer.IgnoreJRERequirement
-dontwarn com.ext.okio.**

-keepattributes EnclosingMethod
