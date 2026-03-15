#pragma once
#include <string>

class IVideo {
public:
    virtual ~IVideo() = default;
    virtual void play() = 0;
    virtual std::string getTitle() const = 0;
};