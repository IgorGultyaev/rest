#!/bin/bash


curl -v -X POST -H "X-Login: user" -H "X-Password: secret" -H "Content-Type: application/json" -d '{"name": "[feature] issue", "content": "content"}' http://localhost:8080/api/issues