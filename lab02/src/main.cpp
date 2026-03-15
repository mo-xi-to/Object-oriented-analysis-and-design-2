#include "YouTubeApp.h"
#include <emscripten.h>

YouTubeApp app;

extern "C" {
    EMSCRIPTEN_KEEPALIVE
    void selectVideo(int index) {
        app.playVideo(index);
    }
}

int main() {
    return 0;
}