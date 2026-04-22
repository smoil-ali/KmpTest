#ifndef KMP_NATIVE_URLS_H
#define KMP_NATIVE_URLS_H


#if defined(STAGING)
#define KMP_BASE_URL_STR "https://www.googuat.com"
#elif defined(PRODUCTION)
#define KMP_BASE_URL_STR "https://www.googProd.com"
#else
#define KMP_BASE_URL_STR "https://www.goog.com"
#endif

#ifdef __cplusplus
extern "C" {
#endif

const char *kmptest_string_from_cpp(void);

#ifdef __cplusplus
}
#endif

#endif
