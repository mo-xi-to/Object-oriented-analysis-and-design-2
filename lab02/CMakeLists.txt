cmake_minimum_required(VERSION 3.10)
project(MyTubeWebProxy)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(PROXY_SOURCES
    src/main.cpp
    src/YouTubeApp.cpp
    src/VideoProxy.cpp
    src/RealVideo.cpp
)

set(NO_PATTERN_SOURCES
    src/mainNoPattern.cpp
    src/YouTubeAppNoPattern.cpp
    src/RealVideo.cpp
)

add_executable(MyTubeProxy ${PROXY_SOURCES})
add_executable(MyTubeNoPattern ${NO_PATTERN_SOURCES})

if(EMSCRIPTEN)
    set_target_properties(MyTubeProxy PROPERTIES SUFFIX ".js")
    target_link_options(MyTubeProxy PRIVATE 
        "-s" "EXPORTED_FUNCTIONS=['_selectVideo','_main']"
        "-s" "EXPORTED_RUNTIME_METHODS=['ccall','cwrap']"
        "-s" "NO_EXIT_RUNTIME=1"
        "-s" "ALLOW_MEMORY_GROWTH=1"
        "--no-entry"
    )

    set_target_properties(MyTubeNoPattern PROPERTIES SUFFIX ".js")
    target_link_options(MyTubeNoPattern PRIVATE 
        "-s" "EXPORTED_FUNCTIONS=['_selectVideo','_main']"
        "-s" "EXPORTED_RUNTIME_METHODS=['ccall','cwrap']"
        "-s" "NO_EXIT_RUNTIME=1"
        "-s" "ALLOW_MEMORY_GROWTH=1"
        "--no-entry"
    )
endif()

target_include_directories(MyTubeProxy PRIVATE src)
target_include_directories(MyTubeNoPattern PRIVATE src)

message(STATUS "Проект готов к сборке. Для WebAssembly используйте emcmake cmake.")