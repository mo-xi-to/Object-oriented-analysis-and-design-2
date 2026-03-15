#pragma once
#include "IVideo.h"
#include "RealVideo.h"
#include <memory>

class VideoProxy : public IVideo {
private:
    std::string title;
    std::string videoId;
    std::unique_ptr<RealVideo> realVideo;

public:
    VideoProxy(std::string t, std::string id);
    void play() override;
    std::string getTitle() const override { return title; }
};