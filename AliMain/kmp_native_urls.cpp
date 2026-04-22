#include "kmp_native_urls.h"

#ifdef __ANDROID__
#include <jni.h>
#endif

extern "C" const char *kmptest_string_from_cpp(void) { return KMP_BASE_URL_STR; }

#ifdef __ANDROID__
extern "C" JNIEXPORT jstring JNICALL
Java_com_appswallet_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject) {
    return env->NewStringUTF(kmptest_string_from_cpp());
}
#endif
