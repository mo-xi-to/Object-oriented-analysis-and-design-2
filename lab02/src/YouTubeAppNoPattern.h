#pragma once
#include <vector>
#include <memory>
#include "IVideo.h"
#include "RealVideo.h"

class YouTubeAppNoPattern {
private:
    std::vector<std::unique_ptr<RealVideo>> vids;

public:
    YouTubeAppNoPattern();
    void playVideo(int index);
};