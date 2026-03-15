#include "YouTubeAppNoPattern.h"
#include <emscripten.h>

YouTubeAppNoPattern app;

extern "C" {
    EMSCRIPTEN_KEEPALIVE
    void selectVideo(int index) {
        app.playVideo(index);
    }
}

int main() {
    return 0;
}