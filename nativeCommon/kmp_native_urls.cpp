/**
 * Single source of truth for API base URLs (live vs staging).
 * Built for:
 *   - Android: NDK defines __ANDROID__ — JNI export is compiled into libnativelib.so.
 *   - iOS: Kotlin/Native cinterop links kmptest_string_from_cpp from libnativelib_ios.a.
 *
 * Staging is selected when the build passes -DSTAGING=1 (Android staging flavor, iOS -Pkmp.environment=staging).
 */

namespace {

#ifdef STAGING
constexpr const char kBaseUrl[] = "https://www.googuat.com";
#else
constexpr const char kBaseUrl[] = "https://www.goog.com";
#endif

} // namespace

extern "C" const char *kmptest_string_from_cpp(void) { return kBaseUrl; }

#ifdef __ANDROID__
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_appswallet_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF(kBaseUrl);
}
#endif
