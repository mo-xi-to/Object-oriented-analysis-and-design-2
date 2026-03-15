#include "YouTubeAppNoPattern.h"
#include <iostream>

YouTubeAppNoPattern::YouTubeAppNoPattern() {
    std::cout << "ВНИМАНИЕ: Создаю все тяжелые объекты сразу!" << std::endl;
    
    vids.push_back(std::make_unique<RealVideo>("Lofi Girl - Beats to study", "jfKfPfyJRdk"));
    vids.push_back(std::make_unique<RealVideo>("Домашний Кранчврап", "aX45GWFerJc"));
    vids.push_back(std::make_unique<RealVideo>("Rick Astley - Never Gonna Give You Up", "dQw4w9WgXcQ"));
    vids.push_back(std::make_unique<RealVideo>("Битва шефов / Ренат Агзамов VS Константин Ивлев. Новый выпуск", "yxSV3DM1Llk"));
    vids.push_back(std::make_unique<RealVideo>("Возьми телефон детка TOXI$ - TIKTOK ПОЛНАЯ ВЕРСИЯ", "bF1FmYhbph"));
    vids.push_back(std::make_unique<RealVideo>("Кухня | Сезон 1 | Серия 1 - 5", "XO310x_bRys"));
}

void YouTubeAppNoPattern::playVideo(int index) {
    if (index >= 0 && index < (int)vids.size()) {
        vids[index]->play();
    }
}