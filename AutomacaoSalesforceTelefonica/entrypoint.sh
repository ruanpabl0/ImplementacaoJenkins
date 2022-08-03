#!/usr/bin/env bash

DISPLAY=:1
RESOLUTION=1920x1080x16

export DISPLAY=$DISPLAY

# start x11 server
Xvfb $DISPLAY -screen 0 1920x1080x16 &