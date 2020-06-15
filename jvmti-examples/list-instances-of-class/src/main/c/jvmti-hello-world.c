#include <jvmti.h>
#include <stdio.h>

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *jvm,char *options,void *reserved) {
  printf("I'm a native Agent....\n");
  return JNI_OK;
}

JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {

}

JNIEXPORT void doSomeThing(int a){
}