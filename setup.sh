#!/bin/bash
#create iam role
response=$( aws iam create-role --role-name timetable-solver-role --assume-role-policy-document "{\"Version\": \"2012-10-17\",\"Statement\": [{ \"Effect\": \"Allow\", \"Principal\": {\"Service\": \"lambda.amazonaws.com\"}, \"Action\": \"sts:AssumeRole\"}]}")
aws iam attach-role-policy --role-name timetable-solver-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
#extract arn
lambda_role_arn=$(jq -r ".Role.Arn" <<< "${response}")

echo "$lambda_role_arn"
#create function
echo aws lambda create-function \
    --function-name timetable-solver \
    --zip-file fileb://./target/function.zip \
    --handler na \
    --runtime provided \
    --role $lambda_role_arn \
    --timeout 15 \
    --memory-size 256 \
    --environment "Variables={DISABLE_SIGNAL_HANDLERS=true}"
