#!/bin/bash
#update function
echo aws lambda update-function-code \
    --function-name timetable-solver \
    --zip-file fileb://./target/function.zip
