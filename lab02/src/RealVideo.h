#pragma once
#include "IVideo.h"

class RealVideo : public IVideo {
private:
    std::string title;
    std::string videoId;

public:
    RealVideo(std::string t, std::string id);
    void play() override;
    std::string getTitle() const override { return title; }
};