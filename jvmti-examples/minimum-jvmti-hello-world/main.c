#include <stdio.h>

int Agent_OnAttach(void* vm, char* options, void* reserved) {
    printf("Agent loaded\n");
    return 0;
}
