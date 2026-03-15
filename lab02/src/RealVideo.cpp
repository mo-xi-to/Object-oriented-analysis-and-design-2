#include "RealVideo.h"
#include <emscripten.h>
#include <iostream>

RealVideo::RealVideo(std::string t, std::string id) : title(t), videoId(id) {
    std::cout << "[C++ RealVideo] Объект создан: " << title << std::endl;
}

void RealVideo::play() {
    std::cout << "[C++ RealVideo] Отправляю ID видео в браузер: " << videoId << std::endl;
    
    std::string js_command = "window.renderIframe('" + videoId + "');";
    emscripten_run_script(js_command.c_str());
}