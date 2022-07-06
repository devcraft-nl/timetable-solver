#!/bin/bash
aws iam detach-role-policy --role-name timetable-solver-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
aws iam delete-role --role-name timetable-solver-role
aws lambda delete-function --function-name timetable-solver
