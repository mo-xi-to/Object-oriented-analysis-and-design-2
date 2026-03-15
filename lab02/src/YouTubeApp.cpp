#include "YouTubeApp.h"
#include "VideoProxy.h"

YouTubeApp::YouTubeApp() {
    vids.push_back(std::make_unique<VideoProxy>("Lofi Girl - Beats to study", "jfKfPfyJRdk"));
    vids.push_back(std::make_unique<VideoProxy>("Домашний Кранчврап", "aX45GWFerJc"));
    vids.push_back(std::make_unique<VideoProxy>("Rick Astley - Never Gonna Give You Up", "dQw4w9WgXcQ"));
    vids.push_back(std::make_unique<VideoProxy>("Битва шефов / Ренат Агзамов VS Константин Ивлев. Новый выпуск", "yxSV3DM1Llk"));
    vids.push_back(std::make_unique<VideoProxy>("Возьми телефон детка TOXI$ - TIKTOK ПОЛНАЯ ВЕРСИЯ", "bF1FmYhbph"));
    vids.push_back(std::make_unique<VideoProxy>("Кухня | Сезон 1 | Серия 1 - 5", "XO310x_bRys"));
}

void YouTubeApp::playVideo(int index) {
    if (index >= 0 && index < (int)vids.size()) {
        vids[index]->play();
    }
}