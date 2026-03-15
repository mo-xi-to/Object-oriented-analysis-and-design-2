#pragma once
#include <vector>
#include <memory>
#include "IVideo.h"

class YouTubeApp {
private:
    std::vector<std::unique_ptr<IVideo>> vids;

public:
    YouTubeApp();
    void playVideo(int index);
};