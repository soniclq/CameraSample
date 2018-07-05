# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

add_library( # Specifies the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/FFAudioEncoder.cpp
             src/main/cpp/VideoEncoder.cpp
             src/main/cpp/RenderVideo.cpp )

include_directories(src/main/cpp/  src/import/include/ )

SET(CMAKE_CXX_FLAGS "-Wno-error=format-security -Wno-error=pointer-sign")

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log)


find_library( # Sets the name of the path variable.
              egl-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              EGL)


# libavcodec
add_library( avcodec
             STATIC
             IMPORTED )

set_target_properties(  avcodec
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libavcodec.a)

# libavformat
add_library( avformat
             STATIC
             IMPORTED )
set_target_properties(  avformat
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libavformat.a)

# libavutil
add_library( avutil
             STATIC
             IMPORTED )
set_target_properties(  avutil
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libavutil.a)

#libswscale
add_library( swscale
             STATIC
             IMPORTED )
set_target_properties(  swscale
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libswscale.a)


#libswresample
add_library( swresample
             STATIC
             IMPORTED )
set_target_properties(  swresample
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libswresample.a)
#libyuv
add_library( yuv
             STATIC
             IMPORTED )
set_target_properties(  yuv
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libyuv.a)
#libfdk-aac
add_library( fdk-aac
             STATIC
             IMPORTED )
set_target_properties(  fdk-aac
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libfdk-aac.a)

#libx264
add_library( x264
             STATIC
             IMPORTED )

set_target_properties(  x264
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${CMAKE_SOURCE_DIR}/src/import/lib/libx264.a)

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                        ${log-lib}
                        ${egl-lib}
                        GLESv2
                       z
                       m
                       avformat
                       avcodec
                       avutil
                       swscale
                       swresample
                       yuv
                       fdk-aac
                       x264
                       )