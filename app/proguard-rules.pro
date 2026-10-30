# TrendGram Security & ProGuard Obfuscation Configuration

# Preserve line numbers for secure error reports while obfuscating source file names
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Obfuscate classes and members aggressively
-repackageclasses ''
-allowaccessmodification

# Keep Data Models for proper serialization
-keep class com.example.model.** { *; }

# Keep Compose and Material 3 internals intact
-keep class androidx.compose.** { *; }

# Keep Media3 / ExoPlayer multimedia pipeline safe
-keep class androidx.media3.** { *; }

# Strip verbose debug logs in production release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
