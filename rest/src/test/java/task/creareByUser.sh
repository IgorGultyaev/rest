#!/bin/bash


curl -v -X POST -H "X-Login: user" -H "X-Password: secret" -H "Content-Type: application/json" -d '{"name": "[feature] task", "deadline": "2023-04-27T00:00:00Z"}' http://localhost:8080/api/issues


