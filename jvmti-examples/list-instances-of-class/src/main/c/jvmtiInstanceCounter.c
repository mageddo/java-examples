#include <jvmti.h>
#include <stdio.h>
#include "jvmInstanceCounter.h"

static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities capa;

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *jvm,char *options,void *reserved) {
  printf("I'm a native Agent....\n");
  jvmtiError error;

//  jint result = (*jvm)->GetEnv((void **) &jvmti, JVMTI_VERSION_1_1);
  jint result = (*jvm)->GetEnv(jvm, (void **) &jvmti, JVMTI_VERSION_1_1);
  if (result != JNI_OK) {
    printf("ERROR: Unable to access JVMTI!\n");
  }
  (void)memset(&capa, 0, sizeof(jvmtiCapabilities));
  capa.can_tag_objects = 1;

  error = (*jvmti)->AddCapabilities(jvmti, &capa);
  printf("%s", error);
//  check_jvmti_error(jvmti, error, "Unable to get necessary JVMTI capabilities.");

  return JNI_OK;
}

JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {

}

JNIEXPORT void doSomeThing(int a){
}

JNICALL jint objectCountingCallback(jlong class_tag, jlong size, jlong* tag_ptr, jint length, void* user_data)
{
 int* count = (int*) user_data;
 *count += 1;
 return JVMTI_VISIT_OBJECTS;
}

JNIEXPORT jint JNICALL Java_com_mageddo_jvmti_JniHelloWorld_countInstances(JNIEnv *env, jclass thisClass, jclass klass){
  return countInstances(klass);
}

JNIEXPORT int countInstances(jclass jclass){
  int count = 0;
  jvmtiHeapCallbacks callbacks;
  (void)memset(&callbacks, 0, sizeof(callbacks));
  callbacks.heap_iteration_callback = &objectCountingCallback;
  jvmtiError error = (*jvmti)->IterateThroughHeap(jvmti, 0, jclass, &callbacks, &count);
  return count;
}

JNIEXPORT int sum(int a, int b){
  return a + b;
}