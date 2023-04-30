#!/bin/bash


curl -v -X POST -H "Content-Type: application/json" -d '{"name": "task", "deadline": "2023-04-27T00:00:00Z"}' http://localhost:8080/api/tasks
