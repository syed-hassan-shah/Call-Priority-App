-ignorewarnings
-dontoptimize
-dontobfuscate
-dontskipnonpubliclibraryclasses

-keepattributes **

-renamesourcefileattribute SourceFile

-assumenosideeffects class * implements org.slf4j.Logger {
    public *** trace(...);
    public *** debug(...);
}
-assumenosideeffects interface org.slf4j.Logger {
    public void trace(...);
    public void debug(...);

    public boolean isTraceEnabled(...);
    public boolean isDebugEnabled(...);
}

-assumenosideeffects class org.slf4j.LoggerFactory {
    public static ** getLogger(...);
}
 -printconfiguration /Users/hassanshah/Desktop/proguard/tmp/full-r8-config_call.txt