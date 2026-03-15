#include "VideoProxy.h"
#include <emscripten.h>
#include <iostream>

VideoProxy::VideoProxy(std::string t, std::string id) 
    : title(t), videoId(id), realVideo(nullptr) {}

void VideoProxy::play() {
    std::cout << "Вызван метод play() для: " << title << std::endl;
    
    if (!realVideo) {
        std::cout << "Виртуальный заместитель инициирует загрузку..." << std::endl;
        
        emscripten_run_script("console.log('Вызываю лоадер...'); window.showLoading();");

        realVideo = std::make_unique<RealVideo>(title, videoId);
    }
    realVideo->play();
}