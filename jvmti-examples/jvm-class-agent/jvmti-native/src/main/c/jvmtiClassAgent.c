#include <jvmti.h>
#include <stdio.h>
#include "jvmtiClassAgent.h"

static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities capa;

JNIEXPORT jint JNICALL
Agent_OnAttach(JavaVM* vm, char *options, void *reserved){
  return Agent_OnLoad(vm, options, reserved);
}

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
  printf("%s\n", error);
//  check_jvmti_error(jvmti, error, "Unable to get necessary JVMTI capabilities.");

  return JNI_OK;
}

JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {

}

JNIEXPORT void doSomeThing(int a){
}

JNICALL jint objectCountingCallback(jlong class_tag, jlong size, jlong* tag_ptr, jint length, void* user_data){
 int* count = (int*) user_data;
 *count += 1;
 return JVMTI_VISIT_OBJECTS;
}

JNIEXPORT int countInstances(jclass jclass){
  int count = 0;
  jvmtiHeapCallbacks callbacks;
  (void)memset(&callbacks, 0, sizeof(callbacks));
  callbacks.heap_iteration_callback = &objectCountingCallback;
  jvmtiError error = (*jvmti)->IterateThroughHeap(jvmti, 0, jclass, &callbacks, &count);
  return count;
}

// public interfaces
JNIEXPORT jint JNICALL Java_com_mageddo_jvmti_JvmtiClass_countInstances(JNIEnv *env, jclass thisClass, jclass klass){
  return countInstances(klass);
}

jobjectArray toObjectArray(JNIEnv *env, int classcount, jobject *objs){
  jobjectArray objectArray = (*env)->NewObjectArray(env, classcount, (*env)->FindClass(env, "java/lang/Class"), NULL);
  int i;
  for (i=0; i < classcount; i++) {
    (*env)->SetObjectArrayElement(env, objectArray, i, (objs[i]));
  }
  return objectArray;
}

// https://github.com/cheat-engine/cheat-engine/blob/master/Cheat%20Engine/Java/CEJVMTI/CEJVMTI/JavaServer.cpp
static jint classcount=0;
static jclass *classes=NULL;
static jobjectArray javaClasses=NULL;

JNIEXPORT jobjectArray JNICALL Java_com_mageddo_jvmti_JvmtiClass_findLoadedClasses(JNIEnv *env){
  if (classes){
    (*jvmti)->Deallocate(jvmti, (unsigned char *)classes);
  }
  if ( (*jvmti)->GetLoadedClasses(jvmti, &classcount, &classes) == JVMTI_ERROR_NONE) {
    javaClasses = toObjectArray(env, classcount, classes);
    return javaClasses;
  }
  return NULL;
}

JNIEXPORT void JNICALL Java_com_mageddo_jvmti_JvmtiClass_findClassMethods(
  JNIEnv *env,
  jclass klass,
  jclass classDefinitionClass,
  jobject classDefinition
  ){
  jmethodID *methods=NULL;
  jint count;

  if ((*jvmti)->GetClassMethods(jvmti, klass, &count, &methods) != JVMTI_ERROR_NONE){
    printf("error to get class methods");
  }

  jobjectArray methodNames = (*env)->NewObjectArray(env, count, (*env)->FindClass(env, "java/lang/String"), NULL);
  int i;
  for (i=0; i<count; i++){
    char *name=NULL, *sig=NULL, *gen=NULL;
    int len;
    jmethodID methodid = methods[i];
    if ((*jvmti)->GetMethodName(jvmti, methodid, &name, &sig, &gen)!=JVMTI_ERROR_NONE) {
      printf("error to get method name");
    }
    (*env)->SetObjectArrayElement(env, methodNames, i, (*env)->NewStringUTF(env, name));
//    jvmti->Deallocate((unsigned char *)methods);

  }
  jclass ent_clazz = (*env)->FindClass(env, "com/mageddo/jvmti/ClassDefinition");
  printf("methods found %d\n", count);
  jfieldID fid = (*env)->GetFieldID(env, ent_clazz, "methods", "[Ljava/lang/String;");
  printf("field id ");
  if(fid == NULL){
     printf("can't get field for class %s\n", classDefinitionClass);
     return ;
  }
  (*env)->SetObjectField(env, ent_clazz, fid, methodNames);
}